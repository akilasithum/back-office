package com.back.office.ui.wizard.steps.itemSteps;

import com.back.office.entity.ItemDetails;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.UserNotification;
import com.vaadin.ui.*;
import org.vaadin.teemu.wizards.WizardStep;

import java.util.List;

public class CreateItemsThirdStep implements WizardStep {

    protected ComboBox costCurrencyFld;
    protected TextField costPriceFld;
    protected ComboBox baseCurrencyFld;
    protected TextField basePriceFld;
    protected ComboBox secondCurrencyFld;
    protected TextField secondPriceFld;

    @Override
    public String getCaption() {
        return "Price Details";
    }

    @Override
    public Component getContent() {
        FormLayout form = new FormLayout();
        List<String> currencyValues = BackOfficeUtils.getCurrencyDropDownValues(true);
        baseCurrencyFld = new ComboBox("Base Currency");
        baseCurrencyFld.setItems(currencyValues);
        baseCurrencyFld.setEmptySelectionAllowed(false);
        baseCurrencyFld.setRequiredIndicatorVisible(true);
        form.addComponent(baseCurrencyFld);

        basePriceFld = new TextField("Base Price");
        basePriceFld.setRequiredIndicatorVisible(true);
        form.addComponent(basePriceFld);

        secondCurrencyFld = new ComboBox("Second Currency");
        secondCurrencyFld.setItems(currencyValues);
        secondCurrencyFld.setEmptySelectionAllowed(false);
        secondCurrencyFld.setRequiredIndicatorVisible(true);
        form.addComponent(secondCurrencyFld);

        secondPriceFld = new TextField("Second Price");
        secondPriceFld.setRequiredIndicatorVisible(true);
        form.addComponent(secondPriceFld);

        costCurrencyFld = new ComboBox("Cost Currency");
        costCurrencyFld.setRequiredIndicatorVisible(true);
        costCurrencyFld.setItems(currencyValues);
        costCurrencyFld.setEmptySelectionAllowed(false);
        form.addComponent(costCurrencyFld);

        costPriceFld = new TextField("Cost Price");
        costPriceFld.setRequiredIndicatorVisible(true);
        form.addComponent(costPriceFld);
        fillFieldsIfAddedAlready();
        return form;
    }

    private void fillFieldsIfAddedAlready(){
        Object obj = UI.getCurrent().getSession().getAttribute("item");
        ItemDetails item;
        if(obj != null && obj instanceof ItemDetails){
            item = (ItemDetails)obj;
            baseCurrencyFld.setValue(item.getBaseCurrency());
            basePriceFld.setValue(String.valueOf(item.getBasePrice()));
            secondCurrencyFld.setValue(item.getSecondCurrency());
            secondPriceFld.setValue(String.valueOf(item.getSecondPrice()));
            costCurrencyFld.setValue(item.getCostCurrency());
            costPriceFld.setValue(String.valueOf(item.getCostPrice()));
        }
    }

    @Override
    public boolean onAdvance() {
        String baseCurrency = String.valueOf(baseCurrencyFld.getValue());
        String basePrice = basePriceFld.getValue();
        String secondaryCurrency = String.valueOf(secondCurrencyFld.getValue());
        String secondaryPrice = secondPriceFld.getValue();
        String costCurrency = String.valueOf(costCurrencyFld.getValue());
        String costPrice = costPriceFld.getValue();
        if(baseCurrency == null || "null".equals(baseCurrency) || basePrice == null || basePrice.isEmpty()
                || secondaryCurrency == null || "null".equals(secondaryCurrency) || secondaryPrice == null || secondaryPrice.isEmpty()
         || costCurrency == null || "null".equals(costCurrency) || costPrice == null || costPrice.isEmpty()){
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
            item.setBaseCurrency(baseCurrency);
            item.setBasePrice(Float.parseFloat(basePrice));
            item.setCostCurrency(costCurrency);
            item.setCostPrice(Float.parseFloat(costPrice));
            item.setSecondCurrency(secondaryCurrency);
            item.setSecondPrice(Float.parseFloat(secondaryPrice));
            UI.getCurrent().getSession().setAttribute("item",item);
            return true;
        }
    }

    @Override
    public boolean onBack() {
        return true;
    }
}
