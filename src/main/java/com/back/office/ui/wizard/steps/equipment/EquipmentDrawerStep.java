package com.back.office.ui.wizard.steps.equipment;

import com.back.office.entity.AircraftDetails;
import com.back.office.entity.EquipmentDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

public class EquipmentDrawerStep implements WizardStep {

    protected TextField noOfDrawersFld;
    private TextField manufacturerFld;
    private TextField weightFld;

    @Override
    public String getCaption() {
        return "Drawer Details";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();

        noOfDrawersFld = new TextField("No of Drawers");
        noOfDrawersFld.setRequiredIndicatorVisible(true);
        form.addComponent(noOfDrawersFld);

        manufacturerFld = new TextField("Manufacturer");
        manufacturerFld.setRequiredIndicatorVisible(true);
        form.addComponent(manufacturerFld);

        weightFld = new TextField("Drawer Weight (Grams)");
        weightFld.setRequiredIndicatorVisible(true);
        form.addComponent(weightFld);
        fillFieldsIfAddedAlready();
        return form;
    }

    @Override
    public boolean onAdvance() {
        return saveEntries();
    }

    @Override
    public boolean onBack() {
        return saveEntries();
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("equipment");
        EquipmentDetails item;
        if(obj != null && obj instanceof EquipmentDetails){
            item = (EquipmentDetails)obj;
            if(item.getNoOfDrawers() != 0) noOfDrawersFld.setValue(String.valueOf(item.getNoOfDrawers()));
            if(item.getDrawerManufacturer() != null) manufacturerFld.setValue(item.getDrawerManufacturer());
            if(item.getDraweWeight() != 0) weightFld.setValue(String.valueOf(item.getDraweWeight()));
        }
    }

    private boolean saveEntries(){
        Object obj = UI.getCurrent().getSession().getAttribute("equipment");
        EquipmentDetails item;
        if(obj != null){
            item = (EquipmentDetails)obj;
        }
        else{
            Notification.show("Something wrong. Please close window and try again", Notification.Type.ERROR_MESSAGE);
            return false;
        }
        int noOfDrawers =  BackOfficeUtils.getInt(noOfDrawersFld.getValue());
        String manufacturer = manufacturerFld.getValue();
        Float weight = BackOfficeUtils.getFloat(weightFld.getValue());
        if(noOfDrawers == 0 || manufacturer == null || "null".equals(manufacturer) || weight == 0){
            Notification.show("Fill all required fields", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        item.setNoOfDrawers(noOfDrawers);
        item.setDrawerManufacturer(manufacturerFld.getValue());
        item.setDraweWeight(weight);
        UI.getCurrent().getSession().setAttribute("equipment",item);
        return true;
    }
}
