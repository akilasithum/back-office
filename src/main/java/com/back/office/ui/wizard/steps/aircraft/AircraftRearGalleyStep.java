package com.back.office.ui.wizard.steps.aircraft;

import com.back.office.entity.AircraftDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

public class AircraftRearGalleyStep implements WizardStep {

    FormLayout formLayout;

    @Override
    public String getCaption() {
        return "Galley Type - Rear";
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
            ((ComboBox)formLayout.getComponent(0)).setValue(item.getRearFullCarts());
            ((ComboBox)formLayout.getComponent(1)).setValue(item.getRearHalfCarts());
            ((ComboBox)formLayout.getComponent(2)).setValue(item.getRearContainers());
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
        item.setRearFullCarts(Integer.parseInt(((ComboBox)formLayout.getComponent(0)).getValue().toString()));
        item.setRearHalfCarts(Integer.parseInt(((ComboBox)formLayout.getComponent(1)).getValue().toString()));
        item.setRearContainers(Integer.parseInt(((ComboBox)formLayout.getComponent(2)).getValue().toString()));
        UI.getCurrent().getSession().setAttribute("aircraft",item);
        return true;
    }

    @Override
    public boolean onBack() {
        return saveEntries();
    }

}