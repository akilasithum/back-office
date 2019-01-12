package com.back.office.ui;

import com.back.office.entity.CurrencyDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class CurerncyDetailsView extends CommonPageDetails {

    private final String CURRENCY_CODE = "Currency Code";
    private final String CURRENCY_DESC = "Currency Description";
    private final String CURRENCY_RATE = "Currency Rate";
    private final String CURRENCY_TYPE = "Currency Type";
    private final String PRIORITY_ORDER = "Priority Order";
    private final String EFFECTIVE_DATE = "Effective Date";

    TextField currencyCodeFld;
    TextField currencyDescFld;
    TextField currencyRateFld;
    ComboBox currencyTypeFld;
    ComboBox priorityOrderFld;
    DateField effectiveDateFld;

    public CurerncyDetailsView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        mainUserInputLayout.addComponent(firstRow);

        currencyCodeFld = new TextField(CURRENCY_CODE);
        currencyCodeFld.setInputPrompt(CURRENCY_CODE);
        currencyCodeFld.setRequired(true);
        firstRow.addComponent(currencyCodeFld);

        currencyDescFld = new TextField(CURRENCY_DESC);
        currencyDescFld.setInputPrompt(CURRENCY_DESC);
        firstRow.addComponent(currencyDescFld);

        currencyRateFld = new TextField(CURRENCY_RATE);
        currencyRateFld.setInputPrompt(CURRENCY_RATE);
        currencyRateFld.setRequired(true);
        firstRow.addComponent(currencyRateFld);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        MarginInfo marginInfo = new MarginInfo(true,false,true,false);
        secondRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(secondRow);

        currencyTypeFld = new ComboBox(CURRENCY_TYPE);
        currencyTypeFld.addItem("Cash");
        currencyTypeFld.addItem("Voucher");
        currencyTypeFld.setTextInputAllowed(false);
        currencyTypeFld.setNullSelectionAllowed(false);
        currencyTypeFld.select("Cash");
        currencyTypeFld.setRequired(true);
        secondRow.addComponent(currencyTypeFld);

        priorityOrderFld = new ComboBox(PRIORITY_ORDER);
        priorityOrderFld.addItem("Base");
        priorityOrderFld.addItem("Standard");
        priorityOrderFld.setTextInputAllowed(false);
        priorityOrderFld.setNullSelectionAllowed(false);
        priorityOrderFld.select("Base");
        currencyTypeFld.setRequired(true);
        secondRow.addComponent(priorityOrderFld);

        effectiveDateFld = new DateField(EFFECTIVE_DATE);
        effectiveDateFld.setRequired(true);
        secondRow.addComponent(effectiveDateFld);

        userFormLayout.setWidth("60%");
        mainTableLayout.setWidth("70%");
        headerLayout.setWidth("70%");
    }

    @Override
    protected IndexedContainer generateContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(CURRENCY_CODE, String.class, null);
        container.addContainerProperty(CURRENCY_DESC, String.class, null);
        container.addContainerProperty(CURRENCY_RATE, Float.class, null);
        container.addContainerProperty(CURRENCY_TYPE, String.class, null);
        container.addContainerProperty(PRIORITY_ORDER, String.class, null);
        container.addContainerProperty(EFFECTIVE_DATE, String.class, null);
        List<CurrencyDetails> currencyDetails = connection.getAllCurrencies();
        for(CurrencyDetails details : currencyDetails){
            Item item = container.addItem(details.getCurrencyCodeId());
            item.getItemProperty(CURRENCY_CODE).setValue(details.getCurrencyCode());
            item.getItemProperty(CURRENCY_DESC).setValue(details.getCurrencyDesc());
            item.getItemProperty(CURRENCY_RATE).setValue(details.getCurrencyRate());
            item.getItemProperty(CURRENCY_TYPE).setValue(details.getCurrencyType());
            item.getItemProperty(PRIORITY_ORDER).setValue(details.getPriorityOrder());
            item.getItemProperty(EFFECTIVE_DATE).setValue(details.getEffectiveDate());
        }
        return container;
    }

    @Override
    protected void insertDetails() {
        String isValidated = validateFields();
        if(isValidated != null){
            Notification.show(isValidated);
        }
        else{
            String effectiveDateStr = BackOfficeUtils.getDateStringFromDate(effectiveDateFld.getValue());
            String val = idField.getValue();
            int currencyCodeIdVal = (val != null && !val.isEmpty())? Integer.parseInt(val) : 0;
            CurrencyDetails details = new CurrencyDetails();
            details.setCurrencyCodeId(currencyCodeIdVal);
            details.setCurrencyCode(currencyCodeFld.getValue());
            details.setCurrencyDesc(currencyDescFld.getValue());
            details.setCurrencyRate(Float.parseFloat(currencyRateFld.getValue()));
            details.setCurrencyType(currencyTypeFld.getValue().toString());
            details.setPriorityOrder(priorityOrderFld.getValue().toString());
            details.setEffectiveDate(effectiveDateStr);
            addOrUpdateDetails(details);
        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
            Item item = container.getItem(target);
            currencyCodeFld.setValue(item.getItemProperty(CURRENCY_CODE).getValue().toString());
            currencyDescFld.setValue(item.getItemProperty(CURRENCY_DESC).getValue() != null ?
                    item.getItemProperty(CURRENCY_DESC).getValue().toString() : "");
            currencyRateFld.setValue(item.getItemProperty(CURRENCY_RATE).getValue().toString());
            currencyTypeFld.setValue(item.getItemProperty(CURRENCY_TYPE).getValue());
            priorityOrderFld.setValue(item.getItemProperty(PRIORITY_ORDER).getValue());
            effectiveDateFld.setValue(BackOfficeUtils.convertStringToDate(item.getItemProperty(EFFECTIVE_DATE).getValue().toString()));
            idField.setValue(target.toString());
            addButton.setCaption("Edit");
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = CURRENCY_CODE;
        this.pageHeader = "Currency Details";
        this.className = "com.back.office.entity.CurrencyDetails";
    }

    @Override
    protected void updateTable(boolean isEdit, Object detail, int newId) {
        CurrencyDetails details = (CurrencyDetails) detail;
        IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
        Item item;
        if(isEdit){
            item  = container.getItem(details.getCurrencyCodeId());
        }
        else{
            item  = container.addItem(newId);
        }
        item.getItemProperty(CURRENCY_CODE).setValue(details.getCurrencyCode());
        item.getItemProperty(CURRENCY_DESC).setValue(details.getCurrencyDesc());
        item.getItemProperty(CURRENCY_RATE).setValue(details.getCurrencyRate());
        item.getItemProperty(CURRENCY_TYPE).setValue(details.getCurrencyType());
        item.getItemProperty(PRIORITY_ORDER).setValue(details.getPriorityOrder());
        item.getItemProperty(EFFECTIVE_DATE).setValue(details.getEffectiveDate());
    }
}
