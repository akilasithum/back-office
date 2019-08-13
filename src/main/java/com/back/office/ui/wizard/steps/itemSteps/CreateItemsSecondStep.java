package com.back.office.ui.wizard.steps.itemSteps;

import com.back.office.entity.ItemDetails;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.UserNotification;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CreateItemsSecondStep implements WizardStep {

    protected TextField catalogFld;
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

        activateDateFld = new DateField("Activate Date");
        activateDateFld.setRequiredIndicatorVisible(true);

        deListed = new ComboBox("De listed");
        deListed.setItems("Yes","No");
        deListed.setSelectedItem("No");
        deListed.setEmptySelectionAllowed(false);
        deListed.setRequiredIndicatorVisible(true);
        form.addComponents(catalogFld,activateDateFld,deListed);
        fillFieldsIfAddedAlready();
        return form;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("item");
        ItemDetails item;
        if(obj != null && obj instanceof ItemDetails){
            item = (ItemDetails)obj;
            if(item.getCatalogue() != null) catalogFld.setValue(item.getCatalogue());
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
        if(activateDateFld.getValue() != null) {
            date = Date.from(activateDateFld.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        String deListedStr = String.valueOf(deListed.getValue());
        if(date == null || deListedStr == null || catalogue == null || catalogue.isEmpty()){
            UserNotification.show("Warning","Fill all the required fields.","warning",UI.getCurrent());
            return false;
        }
        else{
            Object obj = UI.getCurrent().getSession().getAttribute("item");
            ItemDetails item;
            if(obj != null){
                item = (ItemDetails)obj;
            }
            else{
                UserNotification.show("Error","Something wrong. Please close window and try again","error",UI.getCurrent());
                return false;
            }
            item.setCatalogue(catalogue);

            item.setActivateDate(BackOfficeUtils.getDateStringFromDate(date));
            item.setDeListed(deListedStr);
            UI.getCurrent().getSession().setAttribute("item",item);
            return true;
        }
    }

    @Override
    public boolean onBack() {
        return true;
    }
}
