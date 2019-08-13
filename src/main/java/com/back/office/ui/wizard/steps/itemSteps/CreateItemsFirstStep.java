package com.back.office.ui.wizard.steps.itemSteps;

import com.back.office.entity.ItemDetails;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.UserNotification;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.Arrays;

public class CreateItemsFirstStep implements WizardStep {

    protected TextField itemNameFld;
    protected TextField itemCode;
    protected TextField categoryFld;
    protected FormLayout verticalLayout;
    protected Object editObj;
    protected ComboBox availableForCompensation;

    @Override
    public String getCaption() {
        return "Main Details";
    }

    @Override
    public Component getContent() {
        verticalLayout = new FormLayout();

        categoryFld = new TextField("Category");
        String setupType = (String) UI.getCurrent().getSession().getAttribute("setupSubMenu");
        categoryFld.setValue(setupType);
        categoryFld.setVisible(false);

        itemCode = new TextField("Item No");
        itemCode.setRequiredIndicatorVisible(true);

        itemNameFld = new TextField("Item Description");
        itemNameFld.setRequiredIndicatorVisible(true);

        availableForCompensation = new ComboBox("Available for Compensation");
        availableForCompensation.setItems(Arrays.asList("Sales & Compensation","Sales only","Compensation only"));
        availableForCompensation.setSelectedItem("Sales & Compensation");
        availableForCompensation.setEmptySelectionAllowed(false);
        if(setupType == null || setupType.equalsIgnoreCase("Bags") || setupType.equalsIgnoreCase("Order Now")){
            availableForCompensation.setVisible(false);
        }

        verticalLayout.addComponents(categoryFld,itemCode,itemNameFld,availableForCompensation);
        verticalLayout.setMargin(true);
        verticalLayout.setWidth("70%");
        fillFieldsIfAddedAlready();
        return verticalLayout;
    }

    protected void fillFieldsIfAddedAlready(){
        editObj = UI.getCurrent().getSession().getAttribute("item");
        ItemDetails item;
        if(editObj != null && editObj instanceof ItemDetails){
            item = (ItemDetails)editObj;
            categoryFld.setValue(item.getCategory());
            itemCode.setValue(item.getItemCode());
            itemNameFld.setValue(item.getItemName());
        }
    }

    @Override
    public boolean onAdvance() {
        String category = String.valueOf(categoryFld.getValue());
        String itemNo = itemCode.getValue();
        String itemName = itemNameFld.getValue();
        if(category == null || "null".equals(category) ||
                itemName == null || itemName.isEmpty() || itemNo == null || itemNo.isEmpty()){
            UserNotification.show("Warning","Fill all the required fields.","warning",UI.getCurrent());
            return false;
        }
        else{
            Object obj = UI.getCurrent().getSession().getAttribute("item");
            ItemDetails item;
            if(obj != null){
                item = (ItemDetails)obj;
            }
            else item = new ItemDetails();
            item.setCategory(category);
            item.setItemCode(itemNo);
            item.setItemName(itemName);
            item.setAvailableForCompensation(String.valueOf(availableForCompensation.getValue()));
            UI.getCurrent().getSession().setAttribute("item",item);
        }
        return true;
    }

    @Override
    public boolean onBack() {
        return true;
    }
}
