package com.back.office.ui;

import com.back.office.entity.ItemDetails;
import com.back.office.entity.Promotion;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromotionView extends CommonPageDetails {

    private final String DISCOUNT_NAME = "Promotion Name";
    private final String ITEM_COUNT = "Item Count";
    private final String DISCOUNT = "Discount $";
    private final String ITEMS = "Items";
    private final String ACTIVATE_DATE = "Activate Date";
    private final String END_DATE = "End Date";

    protected TextField discountNameFld;
    protected TextField itemCountFld;
    protected TextField discountFld;
    protected DateField activateDateFld;
    protected DateField endDateFld;
    VerticalLayout itemsLayout;

    FilterGrid<Promotion> promotionFilterGrid;
    List<Promotion> promotionList;
    Map<String,ItemDetails> itemDetailsList;

    public PromotionView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        setItemsMap();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.setSizeFull();
        secondRow.setMargin(Constants.noMargin);
        secondRow.setWidth(66.67f,Unit.PERCENTAGE);
        mainUserInputLayout.addComponent(secondRow);

        itemsLayout = new VerticalLayout();
        itemsLayout.setSizeFull();
        itemsLayout.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(itemsLayout);

        discountNameFld = new TextField(DISCOUNT_NAME);
        discountNameFld.setDescription(DISCOUNT_NAME);
        discountNameFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(discountNameFld);
        discountNameFld.addValueChangeListener(valueChangeEvent -> {isKeyFieldDirty = true;});

        itemCountFld = new TextField(ITEM_COUNT);
        itemCountFld.setDescription(ITEM_COUNT);
        itemCountFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(itemCountFld);
        itemCountFld.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                String val = valueChangeEvent.getValue();
                if(BackOfficeUtils.isInteger(val)){
                    addItemsDropdown(Integer.parseInt(val),"");
                }
                else if(val.isEmpty()){
                    itemsLayout.removeAllComponents();
                    itemsLayout.setCaption("");
                }
                else{

                    Notification.show("Please enter a number for item count", Notification.Type.WARNING_MESSAGE);
                }
        });

        discountFld = new TextField(DISCOUNT);
        discountFld.setDescription(DISCOUNT);
        discountFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(discountFld);

        activateDateFld = new DateField(ACTIVATE_DATE);
        activateDateFld.setDescription(ACTIVATE_DATE);
        activateDateFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(activateDateFld);

        endDateFld = new DateField(END_DATE);
        endDateFld.setDescription(END_DATE);
        endDateFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(endDateFld);

        promotionFilterGrid = new FilterGrid<>();
        promotionFilterGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        promotionFilterGrid.setSizeFull();
        tableLayout.addComponent(promotionFilterGrid);
        setDataInGrid();
        GridContextMenu<Promotion> gridMenu = new GridContextMenu<>(promotionFilterGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        userFormLayout.setWidth("60%");
        mainTableLayout.setWidth("75%");
        headerLayout.setWidth("60%");
    }

    private void setItemsMap(){
        List<ItemDetails> list = connection.getAllActiveItems();
        itemDetailsList = new HashMap<>();
        for(ItemDetails item : list){
            itemDetailsList.put(item.getItemName(),item);
        }
    }

    private void addItemsDropdown(int itemCount,String items){
        itemsLayout.removeAllComponents();
        itemsLayout.setCaption("Items");
        String[] itemsArr = items.split(",");
        for(int i = 0;i<itemCount;i++){
            ComboBox comboBox = new ComboBox();
            comboBox.setItems(itemDetailsList.values());
            if(!items.isEmpty()){
                comboBox.setValue(itemDetailsList.get(itemsArr[i]));
            }
            itemsLayout.addComponent(comboBox);
        }
    }

    private String getItemsList(boolean isItemNames){
        String items = "";
        for(int i = 0;i<itemsLayout.getComponentCount();i++){
            ItemDetails val =(ItemDetails) (((ComboBox)itemsLayout.getComponent(i)).getValue());
            if(val != null){
                if(isItemNames)
                items += val.getItemName() +",";
                else
                    items += val.getItemCode() + " and ";
            }
            else{
                Notification.show("One or more items does not enter.", Notification.Type.WARNING_MESSAGE);
                return null;
            }
        }
        if(isItemNames)
        return items.substring(0,items.length()-1);
        else
            return items.substring(0,items.length()-5);
    }

    private void setDataInGrid(){
        promotionList = ((List<Promotion>) connection.getAllValues(className));
        promotionFilterGrid.setItems(promotionList);
        promotionFilterGrid.addColumn(Promotion::getPromoName).setCaption(DISCOUNT_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        promotionFilterGrid.addColumn(Promotion::getItemCount).setCaption(ITEM_COUNT).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        promotionFilterGrid.addColumn(Promotion::getDiscount).setCaption(DISCOUNT).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        promotionFilterGrid.addColumn(Promotion::getItems).setCaption(ITEMS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        promotionFilterGrid.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getActivateDate())).setCaption(ACTIVATE_DATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        promotionFilterGrid.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getEndDate())).setCaption(END_DATE).
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
            Promotion promotion = new Promotion();
            promotion.setPromoId(itemIdVal);
            promotion.setPromoName(discountNameFld.getValue());
            promotion.setItemCount(Integer.parseInt(itemCountFld.getValue()));
            promotion.setDiscount(Float.parseFloat(discountFld.getValue()));
            Date activateDate = Date.from(activateDateFld.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            promotion.setActivateDate(activateDate);
            Date endDate = Date.from(endDateFld.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            promotion.setEndDate(endDate);
            String items = getItemsList(true);
            String itemCodes = getItemsList(false);
            if(items != null){
                promotion.setItems(items);
                promotion.setItemCodes(itemCodes);
            }
            else {
                return;
            }
            addOrUpdateDetails(promotion);
        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {
            Promotion promotion = (Promotion) target;
            idField.setValue(String.valueOf(promotion.getPromoId()));
            discountNameFld.setValue(promotion.getPromoName());
            itemCountFld.setValue(String.valueOf(promotion.getItemCount()));
            discountFld.setValue(String.valueOf(promotion.getDiscount()));
            LocalDate activateDate = promotion.getActivateDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            activateDateFld.setValue(activateDate);
            LocalDate endDate = promotion.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            endDateFld.setValue(endDate);
            addItemsDropdown(promotion.getItemCount(),promotion.getItems());
            addButton.setCaption("Save");
            editObj = promotion;
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = DISCOUNT_NAME;
        this.pageHeader = "Promotions";
        this.className = "com.back.office.entity.Promotion";
        this.keyFieldDBName = "promoName";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        Promotion blackListCC = (Promotion) details;
        if(isEdit){
            int index = promotionList.indexOf(editObj);
            promotionList.remove(editObj);
            promotionList.add(index,blackListCC);
        }
        else{
            promotionList.add(blackListCC);
        }
        promotionFilterGrid.setItems(promotionList);
    }

    @Override
    protected TextField getKeyField() {
        return discountNameFld;
    }

    @Override
    protected void deleteItem(Object target) {
        if (target != null) {
            Promotion blackListCC = (Promotion) target;
            boolean success = connection.deleteObjectHBM(blackListCC.getPromoId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "Promotion delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                promotionList.remove(target);
                promotionFilterGrid.setItems(promotionList);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }
        }
    }
}
