package com.back.office.ui.wizard.steps.itemSteps;

import com.back.office.entity.ItemDetails;
import com.back.office.framework.MyImageUpload;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.vaadin.teemu.wizards.WizardStep;

public class CreateItemsFifthStep implements WizardStep {

    protected MyImageUpload previewField;

    @Override
    public String getCaption() {
        return "Upload image";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();
        previewField = new MyImageUpload();
        previewField.setAcceptFilter("image/*");
        form.addComponent(previewField);
        showImageIfAvailable();
        return form;
    }

    private void showImageIfAvailable(){
        Object obj = UI.getCurrent().getSession().getAttribute("item");
        ItemDetails item;
        if(obj != null && obj instanceof ItemDetails) {
            item = (ItemDetails) obj;
            if(item.getImage() != null) previewField.setValue(item.getImage());
        }
    }

    @Override
    public boolean onAdvance() {
        if(previewField.getValue() == null) {
            Notification.show("Fill all the required fields.", Notification.Type.WARNING_MESSAGE);
            return false;
        }
        else{
            Object obj = UI.getCurrent().getSession().getAttribute("item");
            ItemDetails item;
            if(obj != null){
                item = (ItemDetails)obj;
            }
            else{
                Notification.show("Something wrong. Please close window and try again", Notification.Type.ERROR_MESSAGE);
                return false;
            }
            item.setImage(previewField.getValue());
            UI.getCurrent().getSession().setAttribute("item",item);
        }
        return true;
    }

    @Override
    public boolean onBack() {
        if(previewField.getValue() != null) {
            Object obj = UI.getCurrent().getSession().getAttribute("item");
            ItemDetails item;
            if (obj != null) {
                item = (ItemDetails) obj;
            } else {
                Notification.show("Something wrong. Please close window and try again", Notification.Type.ERROR_MESSAGE);
                return false;
            }
            item.setImage(previewField.getValue());
        }
        return true;
    }
}
