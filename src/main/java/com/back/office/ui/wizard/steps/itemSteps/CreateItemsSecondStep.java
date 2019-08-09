package com.back.office.ui.wizard.steps.itemSteps;

import com.back.office.entity.ItemDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CreateItemsSecondStep implements WizardStep {

    protected TextField catalogFld;
    protected TextField weightFld;
    protected DateField activateDateFld;
    protected ComboBox deListed;

    @Override
    public String getCaption() {
        return "Secondary Details";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();
        catalogFld = new TextField("Catalogue No");
        catalogFld.setRequiredIndicatorVisible(true);

        weightFld = new TextField("Weight (Grams)");
        weightFld.setRequiredIndicatorVisible(true);

        activateDateFld = new DateField("Activate Date");
        activateDateFld.setRequiredIndicatorVisible(true);

        deListed = new ComboBox("De listed");
        deListed.setItems("Yes","No");
        deListed.setSelectedItem("No");
        deListed.setEmptySelectionAllowed(false);
        deListed.setRequiredIndicatorVisible(true);
        form.addComponents(catalogFld,weightFld,activateDateFld,deListed);
        fillFieldsIfAddedAlready();
        return form;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("item");
        ItemDetails item;
        if(obj != null && obj instanceof ItemDetails){
            item = (ItemDetails)obj;
            if(item.getCatalogue() != null) catalogFld.setValue(item.getCatalogue());
            weightFld.setValue(String.valueOf(item.getWeight()));
            if(item.getActivateDate() != null) {
                Date input = BackOfficeUtils.convertStringToDate(item.getActivateDate());
                LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                activateDateFld.setValue(date);
            }
            if(item.getDeListed() != null)deListed.setValue(item.getDeListed());
        }
    }

    @Override
    public boolean onAdvance() {
        Date date = null;
        String catalogue = catalogFld.getValue();
        String weight = weightFld.getValue();
        if(activateDateFld.getValue() != null) {
            date = Date.from(activateDateFld.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        String deListedStr = String.valueOf(deListed.getValue());
        if(weight == null || weight.isEmpty() ||  date == null || deListedStr == null || catalogue == null || catalogue.isEmpty()){
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
            item.setCatalogue(catalogue);
            if(isFloat(weight)){
                item.setWeight(Float.parseFloat(weight));
            }
            else {
                Notification.show("Weight should be a decimal value without texts", Notification.Type.ERROR_MESSAGE);
                return false;
            }

            item.setActivateDate(BackOfficeUtils.getDateStringFromDate(date));
            item.setDeListed(deListedStr);
            UI.getCurrent().getSession().setAttribute("item",item);
            return true;
        }
    }

    private boolean isFloat(String val){
        try{
            float f = Float.parseFloat(val);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean onBack() {
        return true;
    }
}
