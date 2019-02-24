package com.back.office.ui;

import com.back.office.entity.BlackListCC;
import com.back.office.entity.CCNumericRange;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CCNumberRangeView extends CommonPageDetails {

    private final String NUMERIC_RANGE = "Numeric Range";
    private final String CARD_TYPE = "Card Type";
    private final String CARD_DESC = "Card Description";
    private final String START_RANGE = "Start Range";
    private final String END_RANGE = "End Range";
    private final String GENERAL_CC_LIMIT = "General CC Limit(USD)";
    private final String AUTHORIZED_CC_LIMIT = "Authorized Limit(USD):";

    protected TextField numericRangeFld;
    protected ComboBox cardTypeComboBox;
    protected TextField cardDescFld;
    protected TextField startRangeFld;
    protected TextField endRangeFld;
    protected TextField generalCCLimitFld;
    protected TextField authorizedCCLimitFld;

    FilterGrid<CCNumericRange> ccNumericRangeFilterGrid;
    List<CCNumericRange> ccNumericRangeList;
    Map<String,String> cardTypeDescMap;

    public CCNumberRangeView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        cardTypeDescMap = getCardTypeDescMap();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.setSizeFull();
        secondRow.setMargin(Constants.noMargin);
        secondRow.setWidth(75,Unit.PERCENTAGE);
        mainUserInputLayout.addComponent(secondRow);

        numericRangeFld = new TextField(NUMERIC_RANGE);
        numericRangeFld.setDescription(NUMERIC_RANGE);
        numericRangeFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(numericRangeFld);
        numericRangeFld.addValueChangeListener(valueChangeEvent -> {
            isKeyFieldDirty = true;
        });

        cardTypeComboBox = new ComboBox(CARD_TYPE);
        cardTypeComboBox.setDescription(CARD_TYPE);
        cardTypeComboBox.setItems("VI","AX","JC","MC");
        cardTypeComboBox.setEmptySelectionAllowed(false);
        cardTypeComboBox.setRequiredIndicatorVisible(true);
        firstRow.addComponent(cardTypeComboBox);
        cardTypeComboBox.addValueChangeListener((HasValue.ValueChangeListener) valueChangeEvent -> {
            cardDescFld.setEnabled(true);
            String val = cardTypeDescMap.get(valueChangeEvent.getValue());
            if(val == null) val = "";
            cardDescFld.setValue(val);
            cardDescFld.setEnabled(false);
        });

        cardDescFld = new TextField(CARD_DESC);
        cardDescFld.setDescription(CARD_DESC);
        cardDescFld.setEnabled(false);
        firstRow.addComponent(cardDescFld);

        startRangeFld = new TextField(START_RANGE);
        startRangeFld.setDescription(START_RANGE);
        startRangeFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(startRangeFld);

        endRangeFld = new TextField(END_RANGE);
        endRangeFld.setDescription(END_RANGE);
        endRangeFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(endRangeFld);

        generalCCLimitFld = new TextField(GENERAL_CC_LIMIT);
        generalCCLimitFld.setDescription(GENERAL_CC_LIMIT);
        generalCCLimitFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(generalCCLimitFld);

        authorizedCCLimitFld = new TextField(AUTHORIZED_CC_LIMIT);
        authorizedCCLimitFld.setDescription(AUTHORIZED_CC_LIMIT);
        authorizedCCLimitFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(authorizedCCLimitFld);

        ccNumericRangeFilterGrid = new FilterGrid<>();
        ccNumericRangeFilterGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        ccNumericRangeFilterGrid.setSizeFull();
        tableLayout.addComponent(ccNumericRangeFilterGrid);
        setDataInGrid();
        GridContextMenu<CCNumericRange> gridMenu = new GridContextMenu<>(ccNumericRangeFilterGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        userFormLayout.setWidth("70%");
        mainTableLayout.setWidth("80%");
        headerLayout.setWidth("60%");
    }

    private Map<String,String> getCardTypeDescMap(){
        Map<String,String> map = new HashMap<>();
        map.put("VI","Visa");
        map.put("AX","American Express");
        map.put("JC","JCB");
        map.put("MC","MasterCard");
        return map;
    }

    private void setDataInGrid(){
        ccNumericRangeList = ((List<CCNumericRange>) connection.getAllValues(className));
        ccNumericRangeFilterGrid.setItems(ccNumericRangeList);
        ccNumericRangeFilterGrid.addColumn(CCNumericRange::getNumaricRange).setCaption(NUMERIC_RANGE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        ccNumericRangeFilterGrid.addColumn(CCNumericRange::getCardType).setCaption(CARD_TYPE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        ccNumericRangeFilterGrid.addColumn(CCNumericRange::getCardDesc).setCaption(CARD_DESC).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        ccNumericRangeFilterGrid.addColumn(CCNumericRange::getStartRange).setCaption(START_RANGE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        ccNumericRangeFilterGrid.addColumn(CCNumericRange::getEndRange).setCaption(END_RANGE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        ccNumericRangeFilterGrid.addColumn(CCNumericRange::getGenralLimit).setCaption(GENERAL_CC_LIMIT).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        ccNumericRangeFilterGrid.addColumn(CCNumericRange::getAuthorizedLimit).setCaption(AUTHORIZED_CC_LIMIT).
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
            CCNumericRange ccNumericRange = new CCNumericRange();
            ccNumericRange.setRangeId(itemIdVal);
            ccNumericRange.setNumaricRange(numericRangeFld.getValue());
            ccNumericRange.setCardType(cardTypeComboBox.getValue().toString());
            ccNumericRange.setCardDesc(cardDescFld.getValue());
            ccNumericRange.setStartRange(startRangeFld.getValue());
            ccNumericRange.setEndRange(endRangeFld.getValue());
            ccNumericRange.setGenralLimit(Float.valueOf(generalCCLimitFld.getValue()));
            ccNumericRange.setAuthorizedLimit(Float.parseFloat(authorizedCCLimitFld.getValue()));
            addOrUpdateDetails(ccNumericRange,itemIdVal);
        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            CCNumericRange ccNumericRange = (CCNumericRange) target;
            idField.setValue(String.valueOf(ccNumericRange.getRangeId()));
            numericRangeFld.setValue(ccNumericRange.getNumaricRange());
            cardTypeComboBox.setValue(ccNumericRange.getCardType());
            startRangeFld.setValue(ccNumericRange.getStartRange());
            endRangeFld.setValue(ccNumericRange.getEndRange());
            generalCCLimitFld.setValue(String.valueOf(ccNumericRange.getGenralLimit()));
            authorizedCCLimitFld.setValue(String.valueOf(ccNumericRange.getAuthorizedLimit()));
            addButton.setCaption("Save");
            editObj = ccNumericRange;
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = NUMERIC_RANGE;
        this.pageHeader = "CC Number Range";
        this.className = "com.back.office.entity.CCNumericRange";
        this.keyFieldDBName = "numaricRange";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        CCNumericRange ccNumericRange = (CCNumericRange) details;
        if(isEdit){
            int index = ccNumericRangeList.indexOf(editObj);
            ccNumericRangeList.remove(editObj);
            ccNumericRangeList.add(index,ccNumericRange);
        }
        else{
            ccNumericRangeList.add(ccNumericRange);
        }
        ccNumericRangeFilterGrid.setItems(ccNumericRangeList);
    }

    @Override
    protected TextField getKeyField() {
        return numericRangeFld;
    }

    @Override
    protected void deleteItem(Object target) {
        if (target != null) {
            CCNumericRange blackListCC = (CCNumericRange) target;
            boolean success = connection.deleteObjectHBM(blackListCC.getRangeId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "CC number range delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                ccNumericRangeList.remove(target);
                ccNumericRangeFilterGrid.setItems(ccNumericRangeList);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }
}
