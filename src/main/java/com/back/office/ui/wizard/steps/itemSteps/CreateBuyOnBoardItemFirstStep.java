package com.back.office.ui.wizard.steps.itemSteps;

import com.back.office.entity.ItemDetails;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.UserNotification;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;

public class CreateBuyOnBoardItemFirstStep extends CreateItemsFirstStep {

    protected ComboBox bobCategoryCombo;

    @Override
    public Component getContent() {
        verticalLayout = (FormLayout) super.getContent();
        bobCategoryCombo = new ComboBox("Category");
        bobCategoryCombo.setRequiredIndicatorVisible(true);
        bobCategoryCombo.setItems(BackOfficeUtils.getCategoryFromServiceType("BOB"));
        verticalLayout.addComponent(bobCategoryCombo);
        availableForCompensation.setVisible(false);
        fillFieldsIfAddedAlready();
        return verticalLayout;
    }

    @Override
    protected void fillFieldsIfAddedAlready(){
        super.fillFieldsIfAddedAlready();
        if(editObj != null && editObj instanceof ItemDetails){
            ItemDetails item = (ItemDetails)editObj;
            if(item.getBobCategory() != null)
            bobCategoryCombo.setValue(item.getBobCategory());
        }
    }

    @Override
    public boolean onAdvance() {
        boolean isAdvance = super.onAdvance();
        if(isAdvance){
            String category = String.valueOf(bobCategoryCombo.getValue());
            if(category == null || "null".equals(category)){
                UserNotification.show("Warning","Fill all the required fields.","warning", UI.getCurrent());
                return false;
            }
            else{
                Object obj = UI.getCurrent().getSession().getAttribute("item");
                ItemDetails item;
                if(obj != null){
                    item = (ItemDetails)obj;
                }
                else item = new ItemDetails();
                item.setBobCategory(category);
                UI.getCurrent().getSession().setAttribute("item",item);
            }
            return true;
        }
        else {
            return false;
        }
    }
}
