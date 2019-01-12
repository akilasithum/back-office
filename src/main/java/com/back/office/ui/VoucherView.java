package com.back.office.ui;

import com.back.office.entity.BlackListCC;
import com.back.office.entity.EquipmentDetails;
import com.back.office.entity.Voucher;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class VoucherView extends CommonPageDetails {


    private final String VOUCHER_NAME = "Voucher Name";
    private final String VOUCHER_TYPE = "Voucher Type";
    private final String DISCOUNT = "Discount";
    private final String DISCOUNT_PERCENTAGE = "Discount Percentage";
    private final String ACTIVATE_DATE = "Activate Date";
    private final String END_DATE = "End Date";
    private final String AMOUNT = "Amount";

    protected TextField voucherNameFld;
    protected ComboBox voucherTypeComboBox;
    protected DateField activateDateFld;
    protected DateField endDateFld;
    protected TextField discountFld;
    protected TextField amountFld;
    protected TextField rationDiscountFld;
    protected HorizontalLayout secondRow;

    public VoucherView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        MarginInfo marginInfo = new MarginInfo(false,false,true,false);
        firstRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(firstRow);

        secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        secondRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(secondRow);

        voucherNameFld = new TextField(VOUCHER_NAME);
        voucherNameFld.setInputPrompt(VOUCHER_NAME);
        voucherNameFld.setRequired(true);
        firstRow.addComponent(voucherNameFld);

        voucherTypeComboBox = new ComboBox(VOUCHER_TYPE);
        voucherTypeComboBox.setInputPrompt(VOUCHER_TYPE);
        voucherTypeComboBox.addItem("Percentage");
        voucherTypeComboBox.addItem("Ratio");
        voucherTypeComboBox.setNullSelectionAllowed(false);
        voucherTypeComboBox.setRequired(true);
        firstRow.addComponent(voucherTypeComboBox);
        voucherTypeComboBox.addValueChangeListener((Property.ValueChangeListener) valueChangeEvent -> {
            secondRow.removeAllComponents();
            if(voucherTypeComboBox.getValue() != null && voucherTypeComboBox.getValue().equals("Percentage")){
                addPercentageConditonLayout();
            }
            else{
                addRatioConditionLayout();
            }
        });

        activateDateFld = new DateField(ACTIVATE_DATE);
        activateDateFld.setRequired(true);
        firstRow.addComponent(activateDateFld);

        endDateFld = new DateField(END_DATE);
        endDateFld.setRequired(true);
        firstRow.addComponent(endDateFld);

        userFormLayout.setWidth("70%");
        mainTableLayout.setWidth("70%");
        headerLayout.setWidth("70%");
    }

    private void addPercentageConditonLayout(){
        discountFld = new TextField(DISCOUNT_PERCENTAGE);
        discountFld.setInputPrompt(DISCOUNT_PERCENTAGE);
        discountFld.setRequired(true);
        secondRow.addComponent(discountFld);
    }

    private void addRatioConditionLayout(){
        secondRow.setWidth("50%");
        MarginInfo marginInfo = new MarginInfo(false,false,true,false);
        secondRow.setMargin(marginInfo);

        amountFld = new TextField(AMOUNT);
        amountFld.setInputPrompt(AMOUNT);
        amountFld.setRequired(true);
        secondRow.addComponent(amountFld);

        rationDiscountFld = new TextField("Discount");
        rationDiscountFld.setInputPrompt("Discount");
        rationDiscountFld.setRequired(true);
        secondRow.addComponent(rationDiscountFld);
    }

    @Override
    protected IndexedContainer generateContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(VOUCHER_NAME, String.class, null);
        container.addContainerProperty(VOUCHER_TYPE, String.class, null);
        container.addContainerProperty(AMOUNT, Float.class, null);
        container.addContainerProperty(DISCOUNT, String.class, null);
        container.addContainerProperty(ACTIVATE_DATE, String.class, null);
        container.addContainerProperty(END_DATE, String.class, null);

        List<Voucher> voucherList = (List<Voucher>)connection.getAllValues(className);
        for(Voucher voucher : voucherList){
            Item item = container.addItem(voucher.getVoucherId());
            item.getItemProperty(VOUCHER_NAME).setValue(voucher.getVoucherName());
            item.getItemProperty(VOUCHER_TYPE).setValue(voucher.getVoucherType());
            item.getItemProperty(AMOUNT).setValue(voucher.getAmount());
            item.getItemProperty(DISCOUNT).setValue(voucher.getDiscount());
            item.getItemProperty(ACTIVATE_DATE).setValue(voucher.getActivateDate());
            item.getItemProperty(END_DATE).setValue(voucher.getEndDate());
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
            Voucher voucher = new Voucher();
            voucher.setVoucherId(itemIdVal);
            voucher.setVoucherName(voucherNameFld.getValue());
            String voucherType = voucherTypeComboBox.getValue().toString();
            voucher.setVoucherType(voucherType);
            voucher.setActivateDate(BackOfficeUtils.getDateStringFromDate(activateDateFld.getValue()));
            voucher.setEndDate(BackOfficeUtils.getDateStringFromDate(endDateFld.getValue()));
            if(voucherType.equals("Percentage")){
                voucher.setDiscount(discountFld.getValue());
            }
            else{
                voucher.setDiscount(rationDiscountFld.getValue());
                voucher.setAmount(Float.parseFloat(amountFld.getValue()));
            }
            addOrUpdateDetails(voucher);
            secondRow.removeAllComponents();
        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
            Item item = container.getItem(target);
            idField.setValue(target.toString());
            voucherNameFld.setValue(item.getItemProperty(VOUCHER_NAME).getValue().toString());
            String voucherType = item.getItemProperty(VOUCHER_TYPE).getValue().toString();
            voucherTypeComboBox.setValue(voucherType);
            activateDateFld.setValue(BackOfficeUtils.convertStringToDate(item.getItemProperty(ACTIVATE_DATE).getValue().toString()));
            endDateFld.setValue(BackOfficeUtils.convertStringToDate(item.getItemProperty(END_DATE).getValue().toString()));
            secondRow.removeAllComponents();
            if(voucherTypeComboBox.getValue() != null && voucherTypeComboBox.getValue().equals("Percentage")){
                addPercentageConditonLayout();
                discountFld.setValue(item.getItemProperty(DISCOUNT).getValue().toString());
            }
            else{
                addRatioConditionLayout();
                rationDiscountFld.setValue(item.getItemProperty(DISCOUNT).getValue().toString());
                amountFld.setValue(item.getItemProperty(AMOUNT).getValue().toString());
            }
            addButton.setCaption("Edit");
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = VOUCHER_NAME;
        this.pageHeader = "Voucher Details";
        this.className = "com.back.office.entity.Voucher";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        Voucher voucher = (Voucher) details;
        IndexedContainer container = (IndexedContainer) detailsTable.getContainerDataSource();
        Item item;
        if(isEdit){
            item  = container.getItem(voucher.getVoucherId());
        }
        else{
            item  = container.addItem(newId);
        }
        item.getItemProperty(VOUCHER_NAME).setValue(voucher.getVoucherName());
        item.getItemProperty(VOUCHER_TYPE).setValue(voucher.getVoucherType());
        item.getItemProperty(DISCOUNT).setValue(voucher.getDiscount());
        item.getItemProperty(AMOUNT).setValue(voucher.getAmount());
        item.getItemProperty(ACTIVATE_DATE).setValue(voucher.getActivateDate());
        item.getItemProperty(END_DATE).setValue(voucher.getEndDate());
    }
}
