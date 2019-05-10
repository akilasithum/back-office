package com.back.office.ui.wizard.steps.equipment;

import com.back.office.entity.EquipmentDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

public class EquipmentCommonSteps implements WizardStep {

    protected TextField packTypeFld;
    protected TextField packDescFld;
    protected TextField noOfSealsFld;

    @Override
    public String getCaption() {
        return "Common Details";
    }

    @Override
    public Component getContent() {

        FormLayout form = new FormLayout();

        packTypeFld = new TextField("Pack Type");
        packTypeFld.setRequiredIndicatorVisible(true);
        form.addComponent(packTypeFld);

        packDescFld = new TextField("Pack Description");
        packDescFld.setRequiredIndicatorVisible(true);
        form.addComponent(packDescFld);

        noOfSealsFld = new TextField("No of Seals");
        noOfSealsFld.setRequiredIndicatorVisible(true);
        form.addComponent(noOfSealsFld);
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
            if(item.getPackType() != null) packTypeFld.setValue(item.getPackType());
            if(item.getPackDescription() != null) packDescFld.setValue(item.getPackDescription());
            if(item.getNoOfSeals() != 0) noOfSealsFld.setValue(String.valueOf(item.getNoOfSeals()));
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
        int noOfSeals =  BackOfficeUtils.getInt(noOfSealsFld.getValue());
        String packType = packTypeFld.getValue();
        String packDesc = packDescFld.getValue();
        if(noOfSeals == 0 || packType == null || "null".equals(packType) || packDesc == null || "null".equals(packDesc)){
            Notification.show("Fill all required fields", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        item.setNoOfSeals(noOfSeals);
        item.setPackType(packType);
        item.setPackDescription(packDesc);
        UI.getCurrent().getSession().setAttribute("equipment",item);
        return true;
    }
}
