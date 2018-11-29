package com.back.office.ui;

import com.back.office.db.DBConnection;
import com.back.office.entity.CurrencyDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;

import java.util.Date;
import java.util.List;

public class CurrencyView extends VerticalLayout implements View {

    DBConnection connection;

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
    TextField currencyCodeId;
    Button addButton;
    Button resetButton;
    Table currencyDetailsTable;


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    public CurrencyView(){
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
    }

    private void createMainLayout() {

        setSpacing(true);
        Label h1 = new Label("Currency Details");
        h1.addStyleName(ValoTheme.LABEL_H1);
        addComponent(h1);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        addComponent(firstRow);

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
        addComponent(secondRow);

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

        HorizontalLayout buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setSpacing(true);
        addComponent(buttonRow);

        addButton = new Button("Add");
        addButton.addClickListener((Button.ClickListener) clickEvent -> insertCurrencyDetails());
        buttonRow.addComponent(addButton);
        resetButton = new Button("Reset");
        resetButton.addClickListener((Button.ClickListener) clickEvent -> resetFields());
        buttonRow.addComponent(resetButton);


        currencyCodeId = new TextField("Currency Id");
        currencyCodeId.setVisible(false);
        buttonRow.addComponent(currencyCodeId);

        HorizontalLayout rowFilter = new HorizontalLayout();
        rowFilter.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        MarginInfo marginInfo = new MarginInfo(true,false,false,false);
        rowFilter.setMargin(marginInfo);
        rowFilter.setSpacing(true);

        TextField filterFiled = new TextField();
        filterFiled.setInputPrompt("Filter by Currency Code");
        rowFilter.addComponent(filterFiled);

        Button filterBtn = new Button("Filter");
        filterBtn.addClickListener((Button.ClickListener) clickEvent -> {
            IndexedContainer container = (IndexedContainer)currencyDetailsTable.getContainerDataSource();
            if(filterFiled.getValue() == null || filterFiled.getValue().isEmpty()) {
                container.removeContainerFilters(CURRENCY_CODE);
            }
            else{
                container.addContainerFilter(CURRENCY_CODE, filterFiled.getValue(), true, false);
            }
        });
        rowFilter.addComponent(filterBtn);
        addComponent(rowFilter);

        currencyDetailsTable = new Table();
        currencyDetailsTable.setSelectable(true);
        currencyDetailsTable.setMultiSelect(false);
        currencyDetailsTable.setSortEnabled(true);
        currencyDetailsTable.setColumnCollapsingAllowed(true);
        currencyDetailsTable.setColumnReorderingAllowed(true);
        currencyDetailsTable.setPageLength(10);

        IndexedContainer normalContainer = generateContainer();
        currencyDetailsTable.setContainerDataSource(normalContainer);
        currencyDetailsTable.setSizeFull();
        currencyDetailsTable.addActionHandler(actionHandler);

        addComponent(currencyDetailsTable);
    }

    private IndexedContainer generateContainer(){
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

    Action.Handler actionHandler = new Action.Handler() {
        private final Action editItem = new Action("Edit Currency" , FontAwesome.EDIT);
        private final Action deleteItem = new Action("Delete Currency" , FontAwesome.REMOVE);
        private final Action[] ACTIONS = new Action[] {editItem, deleteItem};

        @Override
        public void handleAction(Action action, Object sender, Object target) {
            if(action.getCaption().equals("Edit Currency")){
                fillEditDetails(target);
            }
            else if(action.getCaption().equals("Delete Currency")){
                ConfirmDialog.show(getUI(), "Delete Currency", "Are you sure you want to delete this Currency?",
                        "Yes", "No", new ConfirmDialog.Listener() {

                            public void onClose(ConfirmDialog dialog) {
                                if(dialog.isConfirmed()){
                                    deleteItem(target);
                                }
                            }
                        });
            }
        }

        @Override
        public Action[] getActions(Object target, Object sender) {
            return ACTIONS;
        }
    };

    private void fillEditDetails(Object target){
        if(target != null) {
            IndexedContainer container = (IndexedContainer) currencyDetailsTable.getContainerDataSource();
            Item item = container.getItem(target);
            currencyCodeFld.setValue(item.getItemProperty(CURRENCY_CODE).getValue().toString());
            currencyDescFld.setValue(item.getItemProperty(CURRENCY_DESC).getValue() != null ?
                    item.getItemProperty(CURRENCY_DESC).getValue().toString() : "");
            currencyRateFld.setValue(item.getItemProperty(CURRENCY_RATE).getValue().toString());
            currencyTypeFld.setValue(item.getItemProperty(CURRENCY_TYPE).getValue());
            priorityOrderFld.setValue(item.getItemProperty(PRIORITY_ORDER).getValue());
            effectiveDateFld.setValue(BackOfficeUtils.convertStringToDate(item.getItemProperty(EFFECTIVE_DATE).getValue().toString()));
            currencyCodeId.setValue(target.toString());
            addButton.setCaption("Edit");
        }
    }

    private void deleteItem(Object target){
        if(target != null){
            boolean success = connection.deleteCurrencyDetails(Integer.parseInt(target.toString()));
            if(success){
                Notification.show("Currency delete successfully");
                IndexedContainer container = (IndexedContainer) currencyDetailsTable.getContainerDataSource();
                container.removeItem(target);
            }
            else {
                Notification.show("Something wrong, please try again");
            }
        }
    }

    private void insertCurrencyDetails(){
        String currencyCodeFldValue = currencyCodeFld.getValue();
        if(currencyCodeFldValue == null || currencyCodeFldValue.isEmpty()){
            Notification.show("Enter Currency Code");
            currencyCodeFld.focus();
            return;
        }
        String currencyDescFldValue = currencyDescFld.getValue();
        String currencyTypeFieldVal = currencyTypeFld.getValue().toString();
        String currencyRateFldValue = currencyRateFld.getValue();
        float currencyRate = 0;
        if(currencyRateFldValue == null || currencyRateFldValue.isEmpty() || !BackOfficeUtils.isFloat(currencyRateFldValue)){
            Notification.show("Currency Rate value is incorrect");
            currencyRateFld.focus();
            return;
        }
        else{
            currencyRate = Float.parseFloat(currencyRateFldValue);
        }
        String priorityOrderValue = priorityOrderFld.getValue().toString();
        Date effectiveDate = effectiveDateFld.getValue();
        if(effectiveDate == null){
            Notification.show("Enter Effective Date");
            effectiveDateFld.focus();
            return;
        }
        String effectiveDateStr = BackOfficeUtils.getDateStringFromDate(effectiveDate);
        String val = currencyCodeId.getValue();
        int currencyCodeIdVal = (val != null && !val.isEmpty())? Integer.parseInt(val) : 0;
        CurrencyDetails details = new CurrencyDetails();
        details.setCurrencyCodeId(currencyCodeIdVal);
        details.setCurrencyCode(currencyCodeFldValue);
        details.setCurrencyDesc(currencyDescFldValue);
        details.setCurrencyRate(currencyRate);
        details.setCurrencyType(currencyTypeFieldVal);
        details.setPriorityOrder(priorityOrderValue);
        details.setEffectiveDate(effectiveDateStr);
        if(addButton.getCaption().equals("Add")) {
            int newId = connection.insertObjectHBM(details);
            if (newId != 0) {
                Notification.show("Currency added successfully");
                updateCurrencyTable(false,details,newId);
                resetFields();
            } else {
                Notification.show("Something wrong, please try again");
            }
        }
        else{
            connection.updateObjectHBM(details);
            Notification.show("Currency updated successfully");
            updateCurrencyTable(true,details,0);
            addButton.setCaption("Add");
            resetFields();
        }
    }

    private void updateCurrencyTable(boolean isEdit , CurrencyDetails details, int newId){
        IndexedContainer container = (IndexedContainer) currencyDetailsTable.getContainerDataSource();
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

    private void resetFields(){
        currencyCodeFld.clear();
        currencyDescFld.clear();
        currencyRateFld.clear();
        effectiveDateFld.clear();
    }
}
