package com.back.office.ui;

import com.back.office.entity.CurrencyDetails;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CurerncyDetailsView extends CommonPageDetails {

    private final String CURRENCY_CODE = "Currency Code";
    private final String CURRENCY_DESC = "Currency Description";
    private final String CURRENCY_RATE = "Currency Rate";
    private final String CURRENCY_TYPE = "Currency Type";
    private final String PRIORITY_ORDER = "Priority Order";
    private final String EFFECTIVE_DATE = "Effective Date";

    protected TextField currencyCodeFld;
    TextField currencyDescFld;
    TextField currencyRateFld;
    ComboBox currencyTypeFld;
    ComboBox priorityOrderFld;
    DateField effectiveDateFld;

    FilterGrid<CurrencyDetails> currencyGrid;
    List<CurrencyDetails> currencyDetails;

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
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        currencyCodeFld = new TextField(CURRENCY_CODE);
        currencyCodeFld.setDescription(CURRENCY_CODE);
        currencyCodeFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(currencyCodeFld);
        currencyCodeFld.addValueChangeListener(valueChangeEvent -> {
            isKeyFieldDirty = true;
        });

        currencyDescFld = new TextField(CURRENCY_DESC);
        currencyDescFld.setDescription(CURRENCY_DESC);
        firstRow.addComponent(currencyDescFld);

        currencyRateFld = new TextField(CURRENCY_RATE);
        currencyRateFld.setDescription(CURRENCY_RATE);
        currencyRateFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(currencyRateFld);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(secondRow);

        currencyTypeFld = new ComboBox(CURRENCY_TYPE);
        currencyTypeFld.setItems("Cash","Voucher");
        currencyTypeFld.setRequiredIndicatorVisible(true);
        currencyTypeFld.setEmptySelectionAllowed(false);
        currencyTypeFld.setSelectedItem("Cash");
        secondRow.addComponent(currencyTypeFld);

        priorityOrderFld = new ComboBox(PRIORITY_ORDER);
        priorityOrderFld.setItems("Base","Standard");
        priorityOrderFld.setRequiredIndicatorVisible(true);
        priorityOrderFld.setEmptySelectionAllowed(false);
        priorityOrderFld.setSelectedItem("Base");
        currencyTypeFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(priorityOrderFld);

        effectiveDateFld = new DateField(EFFECTIVE_DATE);
        effectiveDateFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(effectiveDateFld);

        currencyGrid = new FilterGrid<>();
        currencyGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        currencyGrid.setSizeFull();
        tableLayout.addComponent(currencyGrid);
        setDataInGrid();
        GridContextMenu<CurrencyDetails> gridMenu = new GridContextMenu<>(currencyGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        userFormLayout.setWidth("60%");
        mainTableLayout.setWidth("85%");
        headerLayout.setWidth("70%");
    }

    protected void deleteItem(Object target) {
        if (target != null) {
            CurrencyDetails aircraftDetail = (CurrencyDetails) target;
            boolean success = connection.deleteObjectHBM(aircraftDetail.getCurrencyCodeId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "Currency delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                currencyDetails.remove(target);
                currencyGrid.setItems(currencyDetails);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }

    private void setDataInGrid(){
        currencyDetails = (List<CurrencyDetails>)connection.getAllValues(className);
        currencyGrid.setItems(currencyDetails);
        currencyGrid.addColumn(CurrencyDetails::getCurrencyCode).setCaption(CURRENCY_CODE).
        setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        currencyGrid.addColumn(CurrencyDetails::getCurrencyDesc).setCaption(CURRENCY_DESC).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        currencyGrid.addColumn(CurrencyDetails::getCurrencyRate).setCaption(CURRENCY_RATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        currencyGrid.addColumn(CurrencyDetails::getCurrencyType).setCaption(CURRENCY_TYPE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        currencyGrid.addColumn(CurrencyDetails::getPriorityOrder).setCaption(PRIORITY_ORDER).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        currencyGrid.addColumn(CurrencyDetails::getEffectiveDate).setCaption(EFFECTIVE_DATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
    }


    @Override
    protected void insertDetails() {
        String isValidated = validateFields();
        if(isValidated != null){
            Notification.show(isValidated, Notification.Type.WARNING_MESSAGE);
        }
        else{
            Date date = Date.from(effectiveDateFld.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            String effectiveDateStr = BackOfficeUtils.getDateStringFromDate(date);
            String val = idField.getValue();
            int currencyCodeIdVal = (val != null && !val.isEmpty())? Integer.parseInt(val) : 0;
            CurrencyDetails details = new CurrencyDetails();
            //details.setCurrencyCodeId(currencyCodeIdVal);
            details.setCurrencyCode(currencyCodeFld.getValue());
            details.setCurrencyDesc(currencyDescFld.getValue());
            details.setCurrencyRate(Float.parseFloat(currencyRateFld.getValue()));
            details.setCurrencyType(currencyTypeFld.getValue().toString());
            details.setPriorityOrder(priorityOrderFld.getValue().toString());
            details.setEffectiveDate(effectiveDateStr);
            addOrUpdateDetails(details,currencyCodeIdVal);
        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            CurrencyDetails currencyDetails = (CurrencyDetails) target;
            currencyCodeFld.setValue(currencyDetails.getCurrencyCode());
            currencyDescFld.setValue(currencyDetails.getCurrencyDesc());
            currencyRateFld.setValue(String.valueOf(currencyDetails.getCurrencyRate()));
            currencyTypeFld.setValue(currencyDetails.getCurrencyType());
            priorityOrderFld.setValue(currencyDetails.getPriorityOrder());
            Date input = BackOfficeUtils.convertStringToDate(currencyDetails.getEffectiveDate());
            LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            effectiveDateFld.setValue(date);
            idField.setValue(String.valueOf(currencyDetails.getCurrencyCodeId()));
            addButton.setCaption("Save");
            editObj = currencyDetails;
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = CURRENCY_CODE;
        this.pageHeader = "Currency Details";
        this.className = "com.back.office.entity.CurrencyDetails";
        this.keyFieldDBName = "currencyCode";
    }

    @Override
    protected void updateTable(boolean isEdit, Object detail, int newId) {
        CurrencyDetails details = (CurrencyDetails) detail;
        if(isEdit){
            int index = currencyDetails.indexOf(editObj);
            currencyDetails.remove(editObj);
            currencyDetails.add(index,details);
        }
        else{
            currencyDetails.add(details);
        }
        currencyGrid.setItems(currencyDetails);
    }

    @Override
    protected TextField getKeyField() {
        return currencyCodeFld;
    }
}
