package com.back.office.ui.wizard.steps.itemSteps;

import com.back.office.entity.ItemDetails;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

public class CreateItemsFourthStep implements WizardStep {

    protected TextField NFCIdField;
    protected TextField barCodeField;

    @Override
    public String getCaption() {
        return "Technical Details";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();
        NFCIdField = new TextField("RFID");
        NFCIdField.setRequiredIndicatorVisible(true);
        form.addComponent(NFCIdField);

        barCodeField = new TextField("Barcode");
        barCodeField.setRequiredIndicatorVisible(true);
        form.addComponent(barCodeField);
        fillFieldsIfAddedAlready();
        return form;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("item");
        ItemDetails item;
        if(obj != null && obj instanceof ItemDetails){
            item = (ItemDetails)obj;
            if(item.getNfcId() != null)NFCIdField.setValue(item.getNfcId());
            if(item.getBarcode() != null)barCodeField.setValue(item.getBarcode());
        }
    }

    @Override
    public boolean onAdvance() {
        String nfcId = NFCIdField.getValue();
        String barcodeVal = barCodeField.getValue();
        if(nfcId == null || nfcId.isEmpty() || barcodeVal == null || barcodeVal.isEmpty()){
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
            item.setNfcId(nfcId);
            item.setBarcode(barcodeVal);
            UI.getCurrent().getSession().setAttribute("item",item);
        }
        return true;
    }

    @Override
    public boolean onBack() {
        return true;
    }
}
