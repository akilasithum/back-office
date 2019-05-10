package com.back.office.ui.wizard.steps.aircraft;

import com.back.office.entity.AircraftDetails;
import com.back.office.entity.ItemDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;


public class AircraftFrontGalleyStep implements WizardStep {

    FormLayout formLayout;

    @Override
    public String getCaption() {
        return "Galley Type - Front";
    }

    @Override
    public Component getContent() {
        formLayout = BackOfficeUtils.getFormLayout();
        fillFieldsIfAddedAlready();
        return formLayout;
    }

    @Override
    public boolean onAdvance() {

        return saveEntries();
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("aircraft");
        AircraftDetails item;
        if(obj != null && obj instanceof AircraftDetails){
            item = (AircraftDetails)obj;
            ((ComboBox)formLayout.getComponent(0)).setValue(item.getFrontFullCarts());
            ((ComboBox)formLayout.getComponent(1)).setValue(item.getFrontHalfCarts());
            ((ComboBox)formLayout.getComponent(2)).setValue(item.getFrontContainers());
        }
    }

    private boolean saveEntries(){
        Object obj = UI.getCurrent().getSession().getAttribute("aircraft");
        AircraftDetails item;
        if(obj != null){
            item = (AircraftDetails)obj;
        }
        else{
            Notification.show("Something wrong. Please close window and try again", Notification.Type.ERROR_MESSAGE);
            return false;
        }
        item.setFrontFullCarts(Integer.parseInt(((ComboBox)formLayout.getComponent(0)).getValue().toString()));
        item.setFrontHalfCarts(Integer.parseInt(((ComboBox)formLayout.getComponent(1)).getValue().toString()));
        item.setFrontContainers(Integer.parseInt(((ComboBox)formLayout.getComponent(2)).getValue().toString()));
        UI.getCurrent().getSession().setAttribute("aircraft",item);
        return true;
    }

    @Override
    public boolean onBack() {
        return saveEntries();
    }
}
