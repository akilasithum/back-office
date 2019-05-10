package com.back.office.ui.wizard.steps.flight;

import com.back.office.entity.Flight;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

public class FlightCommonStep implements WizardStep {

    protected TextField countryFld;

    @Override
    public String getCaption() {
        return "Common Details";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();

        countryFld = new TextField("Country");
        countryFld.setRequiredIndicatorVisible(true);
        form.addComponent(countryFld);
        fillFieldsIfAddedAlready();
        return form;
    }

    @Override
    public boolean onAdvance() {
        String country = countryFld.getValue();
        if(country == null || country.isEmpty()){
            Notification.show("Fill all required fields", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else{
            Object obj = UI.getCurrent().getSession().getAttribute("flight");
            Flight item;
            if(obj != null){
                item = (Flight)obj;
            }
            else item = new Flight();

            item.setCountry(country);
            item.setBaseStation(UI.getCurrent().getSession().getAttribute("baseStation").toString());
            UI.getCurrent().getSession().setAttribute("flight",item);
        }
        return true;
    }

    @Override
    public boolean onBack() {
        return false;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("flight");
        Flight item;
        if(obj != null && obj instanceof Flight){
            item = (Flight)obj;
            if(item.getCountry() != null) countryFld.setValue(item.getCountry());
        }
    }
}
