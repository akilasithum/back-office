package com.back.office.ui.wizard.steps.flight;

import com.back.office.db.DBConnection;
import com.back.office.entity.Flight;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

public class FlightCommonStep implements WizardStep {

    protected TextField countryFld;
    protected ComboBox aircraftRegNoCombo;
    protected DBConnection connection;

    @Override
    public String getCaption() {
        return "Common Details";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();
        connection = DBConnection.getInstance();
        countryFld = new TextField("Country");
        countryFld.setRequiredIndicatorVisible(true);
        aircraftRegNoCombo = new ComboBox("Aircraft reg No");
        aircraftRegNoCombo.setRequiredIndicatorVisible(true);
        aircraftRegNoCombo.setItems(connection.getAircraftRegNos());
        form.addComponents(countryFld, aircraftRegNoCombo);
        fillFieldsIfAddedAlready();
        return form;
    }

    @Override
    public boolean onAdvance() {
        String country = countryFld.getValue();
        String aircraftReg = String.valueOf(aircraftRegNoCombo.getValue());
        if(country == null || country.isEmpty() || aircraftReg == null || aircraftReg.equalsIgnoreCase("null") ||aircraftReg.isEmpty()){
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
            item.setAircraftRegNo(aircraftReg);
            //item.setBaseStation(UI.getCurrent().getSession().getAttribute("baseStation").toString());
            item.setBaseStation("YYZ");
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
            if(item.getAircraftRegNo() != null) aircraftRegNoCombo.setValue(item.getAircraftRegNo());
        }
    }
}
