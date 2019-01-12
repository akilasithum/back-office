package com.back.office.ui;

import com.back.office.entity.BlackListCC;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class BlackListCCView extends CommonPageDetails {

    private final String CREDIT_CARD_NUMBER = "Card Number";
    private final String STATUS = "Status";

    protected TextField cardNumberFld;
    protected ComboBox statusComboBox;

    public BlackListCCView(){
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

        cardNumberFld = new TextField(CREDIT_CARD_NUMBER);
        cardNumberFld.setInputPrompt(CREDIT_CARD_NUMBER);
        cardNumberFld.setRequired(true);
        firstRow.addComponent(cardNumberFld);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        MarginInfo marginInfo = new MarginInfo(true,false,true,false);
        secondRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(secondRow);

        statusComboBox = new ComboBox(STATUS);
        statusComboBox.setInputPrompt(STATUS);
        statusComboBox.addItem("Bank Black List");
        statusComboBox.addItem("System Labeled Black List");
        statusComboBox.setNullSelectionAllowed(false);
        statusComboBox.setRequired(true);
        secondRow.addComponent(statusComboBox);
        userFormLayout.setWidth("60%");
        mainTableLayout.setWidth("60%");
        headerLayout.setWidth("60%");
    }

    @Override
    protected IndexedContainer generateContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(CREDIT_CARD_NUMBER, String.class, null);
        container.addContainerProperty(STATUS, String.class, null);

        List<BlackListCC> blackListCCS = (List<BlackListCC>)connection.getAllValues(className);
        for(BlackListCC details : blackListCCS){
            Item item = container.addItem(details.getCreditCardId());
            item.getItemProperty(CREDIT_CARD_NUMBER).setValue(details.getCreditCardNumber());
            item.getItemProperty(STATUS).setValue(details.getStatus());
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
            BlackListCC blackListCC = new BlackListCC();
            blackListCC.setCreditCardId(itemIdVal);
            blackListCC.setCreditCardNumber(cardNumberFld.getValue());
            blackListCC.setStatus(statusComboBox.getValue().toString());
            addOrUpdateDetails(blackListCC);

        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
            Item item = container.getItem(target);
            idField.setValue(target.toString());
            cardNumberFld.setValue(item.getItemProperty(CREDIT_CARD_NUMBER).getValue().toString());
            statusComboBox.setValue(item.getItemProperty(STATUS).getValue());
            addButton.setCaption("Edit");
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = CREDIT_CARD_NUMBER;
        this.pageHeader = "Credit Card Black List Details";
        this.className = "com.back.office.entity.BlackListCC";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        BlackListCC blackListCC = (BlackListCC) details;
        IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
        Item item;
        if(isEdit){
            item  = container.getItem(blackListCC.getCreditCardId());
        }
        else{
            item  = container.addItem(newId);
        }
        item.getItemProperty(CREDIT_CARD_NUMBER).setValue(blackListCC.getCreditCardNumber());
        item.getItemProperty(STATUS).setValue(blackListCC.getStatus());
    }
}
