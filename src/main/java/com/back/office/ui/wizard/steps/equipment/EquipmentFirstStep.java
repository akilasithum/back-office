package com.back.office.ui.wizard.steps.equipment;

import com.back.office.entity.EquipmentDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

public class EquipmentFirstStep  implements WizardStep {

    protected ComboBox equipmentTypeFld;
    private TextField manufacturerFld;
    private TextField weightFld;

    @Override
    public String getCaption() {
        return "Main Details";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();
        equipmentTypeFld = new ComboBox("Equipment Type");
        equipmentTypeFld.setItems("Half Cart","Full Cart","Containers");
        equipmentTypeFld.setEmptySelectionAllowed(false);
        equipmentTypeFld.setRequiredIndicatorVisible(true);
        form.addComponent(equipmentTypeFld);

        manufacturerFld = new TextField("Manufacturer");
        manufacturerFld.setRequiredIndicatorVisible(true);
        form.addComponent(manufacturerFld);

        weightFld = new TextField("Weight (Grams)");
        weightFld.setRequiredIndicatorVisible(true);
        form.addComponent(weightFld);
        fillFieldsIfAddedAlready();
        return form;
    }

    @Override
    public boolean onAdvance() {
        String eqType = String.valueOf(equipmentTypeFld.getValue());
        String manufacturer = manufacturerFld.getValue();
        Float weight = BackOfficeUtils.getFloat(weightFld.getValue());
        if(eqType == null || "null".equals(eqType) || manufacturer == null || "null".equals(manufacturer) || weight == 0){
            Notification.show("Fill all required fields", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else{
            Object obj = UI.getCurrent().getSession().getAttribute("equipment");
            EquipmentDetails item;
            if(obj != null){
                item = (EquipmentDetails)obj;
            }
            else item = new EquipmentDetails();

            item.setEquipmentType(eqType);
            item.setCartManufacturer(manufacturer);
            item.setCartWeight(weight);
            UI.getCurrent().getSession().setAttribute("equipment",item);
        }
        return true;
    }

    @Override
    public boolean onBack() {
        return false;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("equipment");
        EquipmentDetails item;
        if(obj != null && obj instanceof EquipmentDetails){
            item = (EquipmentDetails)obj;
            equipmentTypeFld.setValue(item.getEquipmentType());
            manufacturerFld.setValue(item.getCartManufacturer());
            weightFld.setValue(String.valueOf(item.getCartWeight()));
        }
    }
}
