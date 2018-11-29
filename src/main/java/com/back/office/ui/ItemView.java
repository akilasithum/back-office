package com.back.office.ui;

import com.back.office.entity.CurrencyDetails;
import com.back.office.entity.ItemDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;


public class ItemView extends CommonPageDetails {

    private final String ITEM_NAME = "Item Name";
    private final String SERVICE_TYPE = "Service Type";
    private final String CATEGORY = "Category";
    private final String CATELOGUE = "Catalogue";
    private final String WEIGHT = "Weight";
    private final String COST_CURRENCY = "Cost Currency";
    private final String COST_PRICE = "Cost Price";
    private final String BASE_CURRENCY = "Base Currency";
    private final String BASE_PRICE = "Base Price";
    private final String ACTIVATE_DATE = "Activate Date";

    protected TextField itemNameFld;
    protected ComboBox serviceTypeFld;
    protected ComboBox categoryFld;
    protected TextField catelogFld;
    protected TextField weightFld;
    protected ComboBox costCurrencyFld;
    protected TextField costPriceFld;
    protected ComboBox baseCurrencyFld;
    protected TextField basePriceFld;
    protected DateField activateDateFld;

    public ItemView(){
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

        itemNameFld = new TextField(ITEM_NAME);
        itemNameFld.setInputPrompt(ITEM_NAME);
        itemNameFld.setRequired(true);
        firstRow.addComponent(itemNameFld);

        serviceTypeFld = new ComboBox(SERVICE_TYPE);
        serviceTypeFld.addItem("BOB");
        serviceTypeFld.addItem("DTF");
        serviceTypeFld.addItem("DTP");
        serviceTypeFld.addItem("VRT");
        serviceTypeFld.setNullSelectionAllowed(false);
        serviceTypeFld.setRequired(true);
        firstRow.addComponent(serviceTypeFld);

        categoryFld = new ComboBox(CATEGORY);
        categoryFld.setNullSelectionAllowed(false);
        categoryFld.setRequired(true);
        serviceTypeFld.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                if(serviceTypeFld.getValue() != null && !serviceTypeFld.getValue().toString().isEmpty()){
                    categoryFld.removeAllItems();
                    categoryFld.addItems(BackOfficeUtils.getCategoryFromServiceType(serviceTypeFld.getValue().toString()));
                }
            }
        });
        firstRow.addComponent(categoryFld);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        mainUserInputLayout.addComponent(secondRow);

        catelogFld = new TextField(CATELOGUE);
        catelogFld.setInputPrompt(CATELOGUE);
        secondRow.addComponent(catelogFld);

        weightFld = new TextField(WEIGHT);
        weightFld.setInputPrompt(WEIGHT);
        secondRow.addComponent(weightFld);

        activateDateFld = new DateField(ACTIVATE_DATE);
        activateDateFld.setRequired(true);
        secondRow.addComponent(activateDateFld);

        HorizontalLayout thirdRow = new HorizontalLayout();
        thirdRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        thirdRow.setSpacing(true);
        thirdRow.setSizeFull();
        mainUserInputLayout.addComponent(thirdRow);

        costCurrencyFld = new ComboBox(COST_CURRENCY);
        costCurrencyFld.addItems(getCurrencyDropDownValues());
        costCurrencyFld.setNullSelectionAllowed(false);
        costCurrencyFld.setRequired(true);
        thirdRow.addComponent(costCurrencyFld);

        costPriceFld = new TextField(COST_PRICE);
        costPriceFld.setInputPrompt(COST_PRICE);
        costPriceFld.setRequired(true);
        thirdRow.addComponent(costPriceFld);

        HorizontalLayout forthRow = new HorizontalLayout();
        forthRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        forthRow.setSpacing(true);
        forthRow.setSizeFull();
        //mainUserInputLayout.addComponent(forthRow);

        baseCurrencyFld = new ComboBox(BASE_CURRENCY);
        baseCurrencyFld.addItems(getCurrencyDropDownValues());
        baseCurrencyFld.setNullSelectionAllowed(false);
        baseCurrencyFld.setRequired(true);
        thirdRow.addComponent(baseCurrencyFld);

        basePriceFld = new TextField(BASE_PRICE);
        basePriceFld.setInputPrompt(COST_PRICE);
        basePriceFld.setRequired(true);
        thirdRow.addComponent(basePriceFld);

    }

    @Override
    protected IndexedContainer generateContainer(){
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(ITEM_NAME, String.class, null);
        container.addContainerProperty(SERVICE_TYPE, String.class, null);
        container.addContainerProperty(CATEGORY, String.class, null);
        container.addContainerProperty(CATELOGUE, String.class, null);
        container.addContainerProperty(WEIGHT, Float.class, null);
        container.addContainerProperty(COST_CURRENCY, String.class, null);
        container.addContainerProperty(COST_PRICE, Float.class, null);
        container.addContainerProperty(BASE_CURRENCY, String.class, null);
        container.addContainerProperty(BASE_PRICE, Float.class, null);
        container.addContainerProperty(ACTIVATE_DATE, String.class, null);
        List<ItemDetails> currencyDetails = connection.getAllItems();
        for(ItemDetails details : currencyDetails){
            Item item = container.addItem(details.getItemId());
            item.getItemProperty(ITEM_NAME).setValue(details.getItemName());
            item.getItemProperty(SERVICE_TYPE).setValue(details.getServiceType());
            item.getItemProperty(CATEGORY).setValue(details.getCategory());
            item.getItemProperty(CATELOGUE).setValue(details.getCatalogue());
            item.getItemProperty(WEIGHT).setValue(details.getWeight());
            item.getItemProperty(COST_CURRENCY).setValue(details.getCostCurrency());
            item.getItemProperty(COST_PRICE).setValue(details.getCostPrice());
            item.getItemProperty(BASE_CURRENCY).setValue(details.getBaseCurrency());
            item.getItemProperty(BASE_PRICE).setValue(details.getBasePrice());
            item.getItemProperty(ACTIVATE_DATE).setValue(details.getActivateDate());
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
            int itemIdVal = (idField.getValue() == null || idField.getValue().isEmpty()) ? 0 : Integer.parseInt(idField.getValue());
            ItemDetails itemDetails = new ItemDetails();
            itemDetails.setItemId(itemIdVal);
            itemDetails.setItemName(itemNameFld.getValue());
            itemDetails.setServiceType(serviceTypeFld.getValue().toString());
            itemDetails.setCategory(categoryFld.getValue().toString());
            itemDetails.setCatalogue(catelogFld.getValue());
            itemDetails.setWeight(Float.parseFloat(weightFld.getValue()));
            itemDetails.setActivateDate(BackOfficeUtils.getDateStringFromDate(activateDateFld.getValue()));
            itemDetails.setCostCurrency(costCurrencyFld.getValue().toString());
            itemDetails.setCostPrice(Float.parseFloat(costPriceFld.getValue()));
            itemDetails.setBaseCurrency(baseCurrencyFld.getValue().toString());
            itemDetails.setBasePrice(Float.parseFloat(basePriceFld.getValue()));
            addOrUpdateDetails(itemDetails);

        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
            Item item = container.getItem(target);

            idField.setValue(target.toString());
            itemNameFld.setValue(item.getItemProperty(ITEM_NAME).getValue().toString());
            serviceTypeFld.setValue(item.getItemProperty(SERVICE_TYPE).getValue());
            categoryFld.setValue(item.getItemProperty(CATEGORY).getValue().toString());
            catelogFld.setValue(item.getItemProperty(CATELOGUE).getValue().toString());
            activateDateFld.setValue(BackOfficeUtils.convertStringToDate(item.getItemProperty(ACTIVATE_DATE).getValue().toString()));
            weightFld.setValue(item.getItemProperty(WEIGHT).getValue().toString());
            costCurrencyFld.setValue(item.getItemProperty(COST_CURRENCY).getValue().toString());
            costPriceFld.setValue(item.getItemProperty(COST_PRICE).getValue().toString());
            baseCurrencyFld.setValue(item.getItemProperty(BASE_CURRENCY).getValue().toString());
            basePriceFld.setValue(item.getItemProperty(BASE_PRICE).getValue().toString());
            addButton.setCaption("Edit");
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = ITEM_NAME;
        this.pageHeader = "Item Details";
        this.className = "com.back.office.common.ItemDetails";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        ItemDetails itemDetails = (ItemDetails) details;
        IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
        Item item;
        if(isEdit){
            item  = container.getItem(itemDetails.getItemId());
        }
        else{
            item  = container.addItem(newId);
        }
        item.getItemProperty(ITEM_NAME).setValue(itemDetails.getItemName());
        item.getItemProperty(SERVICE_TYPE).setValue(itemDetails.getServiceType());
        item.getItemProperty(CATEGORY).setValue(itemDetails.getCategory());
        item.getItemProperty(CATELOGUE).setValue(itemDetails.getCatalogue());
        item.getItemProperty(WEIGHT).setValue(itemDetails.getWeight());
        item.getItemProperty(ACTIVATE_DATE).setValue(itemDetails.getActivateDate());
        item.getItemProperty(COST_CURRENCY).setValue(itemDetails.getCostCurrency());
        item.getItemProperty(COST_PRICE).setValue(itemDetails.getCostPrice());
        item.getItemProperty(BASE_CURRENCY).setValue(itemDetails.getBaseCurrency());
        item.getItemProperty(BASE_PRICE).setValue(itemDetails.getBasePrice());
    }

    private List<String> getCurrencyDropDownValues(){
        List<CurrencyDetails> currencyDetails = connection.getAllCurrencies();
        List<String> currencies = new ArrayList<>();
        for(CurrencyDetails currency : currencyDetails){
            currencies.add(currency.getCurrencyCode());
        }
        return currencies;
    }
}
