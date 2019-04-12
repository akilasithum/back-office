package com.back.office.ui.wizard.steps.itemSteps;

import com.back.office.entity.ItemDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

public class CreateItemsFirstStep implements WizardStep {

    protected TextField itemNameFld;
    protected ComboBox serviceTypeFld;
    protected TextField itemCode;
    protected ComboBox categoryFld;

    @Override
    public String getCaption() {
        return "Main Details";
    }

    @Override
    public Component getContent() {
        FormLayout verticalLayout = new FormLayout();
        serviceTypeFld = new ComboBox("Service Type");
        serviceTypeFld.setItems("BOB","DTF","DTP","VRT");
        serviceTypeFld.setEmptySelectionAllowed(false);
        serviceTypeFld.setRequiredIndicatorVisible(true);
        serviceTypeFld.addValueChangeListener( valueChangeEvent -> {
            if(serviceTypeFld.getValue() != null && !serviceTypeFld.getValue().toString().isEmpty()){
                categoryFld.setItems(BackOfficeUtils.getCategoryFromServiceType(serviceTypeFld.getValue().toString()));
            }
        });

        categoryFld = new ComboBox("Category");
        categoryFld.setEmptySelectionAllowed(false);
        categoryFld.setRequiredIndicatorVisible(true);

        itemCode = new TextField("Item No");
        itemCode.setRequiredIndicatorVisible(true);

        itemNameFld = new TextField("Item Description");
        itemNameFld.setRequiredIndicatorVisible(true);

        verticalLayout.addComponents(serviceTypeFld,categoryFld,itemCode,itemNameFld);
        verticalLayout.setMargin(true);
        fillFieldsIfAddedAlready();
        return verticalLayout;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("item");
        ItemDetails item;
        if(obj != null && obj instanceof ItemDetails){
            item = (ItemDetails)obj;
            serviceTypeFld.setValue(item.getServiceType());
            categoryFld.setValue(item.getCategory());
            itemCode.setValue(item.getItemCode());
            itemNameFld.setValue(item.getItemName());
        }
    }

    @Override
    public boolean onAdvance() {
        String serviceType = String.valueOf(serviceTypeFld.getValue());
        String category = String.valueOf(categoryFld.getValue());
        String itemNo = itemCode.getValue();
        String itemName = itemNameFld.getValue();
        if(serviceType == null || "null".equals(serviceType) || category == null || "null".equals(category) ||
                itemName == null || itemName.isEmpty() || itemNo == null || itemNo.isEmpty()){
            Notification.show("Fill all the required fields.", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else{
            Object obj = UI.getCurrent().getSession().getAttribute("item");
            ItemDetails item;
            if(obj != null){
                item = (ItemDetails)obj;
            }
            else item = new ItemDetails();

            item.setServiceType(serviceType);
            item.setCategory(category);
            item.setItemCode(itemNo);
            item.setItemName(itemName);
            UI.getCurrent().getSession().setAttribute("item",item);
        }
        return true;
    }

    @Override
    public boolean onBack() {
        return true;
    }
}
