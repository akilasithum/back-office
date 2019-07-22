package com.back.office.ui;

import com.back.office.entity.BlackListCC;
import com.back.office.entity.User;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import java.util.List;

public class BlackListCCView extends CommonPageDetails {

    private final String CREDIT_CARD_NUMBER = "Card Number";
    private final String STATUS = "Status";

    protected TextField cardNumberFld;
    protected ComboBox statusComboBox;

    FilterGrid<BlackListCC> blackListCCGrid;
    List<BlackListCC> blackListCCList;

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
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        cardNumberFld = new TextField(CREDIT_CARD_NUMBER);
        cardNumberFld.setDescription(CREDIT_CARD_NUMBER);
        cardNumberFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(cardNumberFld);
        cardNumberFld.addValueChangeListener(valueChangeEvent -> {
            isKeyFieldDirty = true;
        });

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        secondRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(secondRow);

        statusComboBox = new ComboBox(STATUS);
        statusComboBox.setDescription(STATUS);
        statusComboBox.setItems("Bank Black List","System Labeled Black List");
        statusComboBox.setEmptySelectionAllowed(false);
        statusComboBox.setRequiredIndicatorVisible(true);
        secondRow.addComponent(statusComboBox);

        blackListCCGrid = new FilterGrid<>();
        blackListCCGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        blackListCCGrid.setSizeFull();
        tableLayout.addComponent(blackListCCGrid);
        setDataInGrid();
        GridContextMenu<BlackListCC> gridMenu = new GridContextMenu<>(blackListCCGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        userFormLayout.setWidth("60%");
        mainTableLayout.setWidth("60%");
        headerLayout.setWidth("60%");
    }

    protected void deleteItem(Object target) {
        if (target != null) {
            BlackListCC blackListCC = (BlackListCC) target;
            boolean success = connection.deleteObjectHBM(blackListCC.getCreditCardId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "Credit card Entry delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                blackListCCList.remove(target);
                blackListCCGrid.setItems(blackListCCList);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }

    private void setDataInGrid(){
        blackListCCList = ((List<BlackListCC>) connection.getAllValues(className));
        blackListCCGrid.setItems(blackListCCList);
        blackListCCGrid.addColumn(BlackListCC::getCreditCardNumber).setCaption(CREDIT_CARD_NUMBER).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        blackListCCGrid.addColumn(BlackListCC::getStatus).setCaption(STATUS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
    }

    @Override
    protected void insertDetails() {
        String isValidated = validateFields();
        if(isValidated != null){
            Notification.show(isValidated, Notification.Type.WARNING_MESSAGE);
        }
        else{
            int itemIdVal = (idField.getValue() == null || idField.getValue().isEmpty()) ? 0 : Integer.parseInt(idField.getValue());
            BlackListCC blackListCC = new BlackListCC();
            blackListCC.setCreditCardId(itemIdVal);
            blackListCC.setCreditCardNumber(cardNumberFld.getValue());
            blackListCC.setStatus(statusComboBox.getValue().toString());
            addOrUpdateDetails(blackListCC,itemIdVal);

        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            BlackListCC blackListCC = (BlackListCC) target;
            idField.setValue(String.valueOf(blackListCC.getCreditCardId()));
            cardNumberFld.setValue(blackListCC.getCreditCardNumber());
            statusComboBox.setValue(blackListCC.getStatus());
            addButton.setCaption("Save");
            editObj = blackListCC;
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = CREDIT_CARD_NUMBER;
        this.pageHeader = "Credit Card Black List Details";
        this.className = "com.back.office.entity.BlackListCC";
        this.keyFieldDBName = "creditCardNumber";
    }

    @Override
    protected TextField getKeyField() {
        return cardNumberFld;
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        BlackListCC blackListCC = (BlackListCC) details;
        if(isEdit){
            int index = blackListCCList.indexOf(editObj);
            blackListCCList.remove(editObj);
            blackListCCList.add(index,blackListCC);
        }
        else{
            blackListCCList.add(blackListCC);
        }
        blackListCCGrid.setItems(blackListCCList);
    }
}
