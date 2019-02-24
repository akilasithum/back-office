package com.back.office.ui;

import com.back.office.entity.BlackListCC;
import com.back.office.entity.Voucher;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

    FilterGrid<Voucher> voucherGrid;
    List<Voucher> voucherList;

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
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        secondRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(secondRow);

        voucherNameFld = new TextField(VOUCHER_NAME);
        voucherNameFld.setDescription(VOUCHER_NAME);
        voucherNameFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(voucherNameFld);
        voucherNameFld.addValueChangeListener(valueChangeEvent -> {isKeyFieldDirty = true;});

        voucherTypeComboBox = new ComboBox(VOUCHER_TYPE);
        voucherTypeComboBox.setDescription(VOUCHER_TYPE);
        voucherTypeComboBox.setItems("Percentage","Ratio");
        voucherTypeComboBox.setEmptySelectionAllowed(false);
        voucherTypeComboBox.setRequiredIndicatorVisible(true);
        firstRow.addComponent(voucherTypeComboBox);
        voucherTypeComboBox.addValueChangeListener(valueChangeEvent -> {
            secondRow.removeAllComponents();
            if(voucherTypeComboBox.getValue() != null && voucherTypeComboBox.getValue().equals("Percentage")){
                addPercentageConditonLayout();
            }
            else{
                addRatioConditionLayout();
            }
        });

        activateDateFld = new DateField(ACTIVATE_DATE);
        activateDateFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(activateDateFld);

        endDateFld = new DateField(END_DATE);
        endDateFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(endDateFld);

        voucherGrid = new FilterGrid<>();
        voucherGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        voucherGrid.setSizeFull();
        tableLayout.addComponent(voucherGrid);
        setDataInGrid();
        GridContextMenu<Voucher> gridMenu = new GridContextMenu<>(voucherGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        userFormLayout.setWidth("70%");
        mainTableLayout.setWidth("70%");
        headerLayout.setWidth("70%");
    }

    protected void deleteItem(Object target) {
        if (target != null) {
            Voucher voucher = (Voucher) target;
            boolean success = connection.deleteObjectHBM(voucher.getVoucherId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "Voucher delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                voucherList.remove(target);
                voucherGrid.setItems(voucherList);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }

    private void setDataInGrid(){
        voucherList = (List<Voucher>)connection.getAllValues(className);
        voucherGrid.setItems(voucherList);
        voucherGrid.addColumn(Voucher::getVoucherName).setCaption(VOUCHER_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        voucherGrid.addColumn(Voucher::getVoucherType).setCaption(VOUCHER_TYPE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        voucherGrid.addColumn(Voucher::getAmount).setCaption(AMOUNT).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        voucherGrid.addColumn(Voucher::getDiscount).setCaption(DISCOUNT).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        voucherGrid.addColumn(Voucher::getActivateDate).setCaption(ACTIVATE_DATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        voucherGrid.addColumn(Voucher::getEndDate).setCaption(END_DATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
    }

    private void addPercentageConditonLayout(){
        discountFld = new TextField(DISCOUNT_PERCENTAGE);
        discountFld.setDescription(DISCOUNT_PERCENTAGE);
        discountFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(discountFld);
    }

    private void addRatioConditionLayout(){
        secondRow.setWidth("50%");
        MarginInfo marginInfo = new MarginInfo(false,false,true,false);
        secondRow.setMargin(marginInfo);

        amountFld = new TextField(AMOUNT);
        amountFld.setDescription(AMOUNT);
        amountFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(amountFld);

        rationDiscountFld = new TextField("Discount");
        rationDiscountFld.setDescription("Discount");
        rationDiscountFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(rationDiscountFld);
    }



    @Override
    protected void insertDetails() {
        String isValidated = validateFields();
        if(isValidated != null){
            Notification.show(isValidated, Notification.Type.WARNING_MESSAGE);
        }
        else{
            int itemIdVal = (idField.getValue() == null || idField.getValue().isEmpty()) ? 0 : Integer.parseInt(idField.getValue());
            Voucher voucher = new Voucher();
            voucher.setVoucherId(itemIdVal);
            voucher.setVoucherName(voucherNameFld.getValue());
            String voucherType = voucherTypeComboBox.getValue().toString();
            voucher.setVoucherType(voucherType);

            Date date = Date.from(activateDateFld.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            String effectiveDateStr = BackOfficeUtils.getDateStringFromDate(date);
            Date endDate = Date.from(endDateFld.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            String endDateStr = BackOfficeUtils.getDateStringFromDate(endDate);

            voucher.setActivateDate(effectiveDateStr);
            voucher.setEndDate(endDateStr);
            if(voucherType.equals("Percentage")){
                voucher.setDiscount(discountFld.getValue());
            }
            else{
                voucher.setDiscount(rationDiscountFld.getValue());
                voucher.setAmount(Float.parseFloat(amountFld.getValue()));
            }
            addOrUpdateDetails(voucher,itemIdVal);
            secondRow.removeAllComponents();
        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            Voucher voucher = (Voucher) target;
            idField.setValue(String.valueOf(voucher.getVoucherId()));
            voucherNameFld.setValue(voucher.getVoucherName());
            String voucherType = voucher.getVoucherType();
            voucherTypeComboBox.setValue(voucherType);
            Date input = BackOfficeUtils.convertStringToDate(voucher.getActivateDate());
            Date endDate = BackOfficeUtils.convertStringToDate(voucher.getEndDate());
            LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDateVal = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            activateDateFld.setValue(date);
            endDateFld.setValue(endDateVal);
            secondRow.removeAllComponents();
            if(voucherTypeComboBox.getValue() != null && voucherTypeComboBox.getValue().equals("Percentage")){
                addPercentageConditonLayout();
                discountFld.setValue(String.valueOf(voucher.getDiscount()));
            }
            else{
                addRatioConditionLayout();
                rationDiscountFld.setValue(voucher.getDiscount());
                amountFld.setValue(String.valueOf(voucher.getAmount()));
            }
            addButton.setCaption("Save");
            editObj = voucher;
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = VOUCHER_NAME;
        this.pageHeader = "Voucher Details";
        this.className = "com.back.office.entity.Voucher";
        this.keyFieldDBName = "voucherName";
    }

    @Override
    protected TextField getKeyField() {
        return voucherNameFld;
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        Voucher voucher = (Voucher) details;
        if(isEdit){
            int index = voucherList.indexOf(editObj);
            voucherList.remove(editObj);
            voucherList.add(index,voucher);
        }
        else{
            voucherList.add(voucher);
        }
        voucherGrid.setItems(voucherList);
    }
}
