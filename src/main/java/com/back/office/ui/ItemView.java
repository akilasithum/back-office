package com.back.office.ui;

import com.back.office.entity.CurrencyDetails;
import com.back.office.entity.ItemDetails;
import com.back.office.framework.MyImageUpload;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.easyuploads.ImagePreviewField;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.NClob;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ItemView extends CommonPageDetails {

    private final String ITEM_NAME = "Item Description";
    private final String SERVICE_TYPE = "Service Type";
    private final String CATEGORY = "Category";
    private final String CATELOGUE = "Catalogue No";
    private final String WEIGHT = "Weight (Grams)";
    private final String COST_CURRENCY = "Cost Currency";
    private final String COST_PRICE = "Cost Price";
    private final String BASE_CURRENCY = "Base Currency";
    private final String BASE_PRICE = "Base Price";
    private final String ACTIVATE_DATE = "Activate Date";
    private final String ITEM_CODE = "Item No";
    private final String SECOND_CURRENCY = "Second Currency";
    private final String SECOND_PRICE = "Second Price";
    private final String DE_LISTED = "De listed";
    private final String NFC_ID = "RFID";
    private final String BARCODE = "Barcode";

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
    protected TextField itemCode;
    protected ComboBox secondCurrencyFld;
    protected TextField secondPriceFld;
    protected ComboBox deListed;
    protected MyImageUpload previewField;
    protected TextField NFCIdField;
    protected TextField barCodeField;
    HorizontalLayout imageLayout;

    FilterGrid<ItemDetails> itemDetailsFilterGrid;
    List<ItemDetails> itemDetails;

    public ItemView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        keyFieldValues = (List<String>)connection.getItemCodesList();
        imageLayout = new HorizontalLayout();
        imageLayout.setSizeFull();
        imageLayout.setMargin(Constants.noMargin);
        outerLayout.addComponent(imageLayout);

        userFormLayout.setSizeFull();
        outerLayout.setComponentAlignment(imageLayout,Alignment.BOTTOM_LEFT);
        outerLayout.setExpandRatio(userFormLayout,0.8f);
        outerLayout.setExpandRatio(imageLayout,0.2f);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        serviceTypeFld = new ComboBox(SERVICE_TYPE);
        serviceTypeFld.setItems("BOB","DTF","DTP","VRT");
        serviceTypeFld.setEmptySelectionAllowed(false);
        serviceTypeFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(serviceTypeFld);

        categoryFld = new ComboBox(CATEGORY);
        categoryFld.setEmptySelectionAllowed(false);
        categoryFld.setRequiredIndicatorVisible(true);
        serviceTypeFld.addValueChangeListener( valueChangeEvent -> {
            if(serviceTypeFld.getValue() != null && !serviceTypeFld.getValue().toString().isEmpty()){
                categoryFld.setItems(BackOfficeUtils.getCategoryFromServiceType(serviceTypeFld.getValue().toString()));
            }
        });
        firstRow.addComponent(categoryFld);

        itemCode = new TextField(ITEM_CODE);
        itemCode.setDescription(ITEM_CODE);
        itemCode.setRequiredIndicatorVisible(true);
        firstRow.addComponent(itemCode);
        itemCode.addValueChangeListener(valueChangeEvent -> {
            isKeyFieldDirty = true;
        });

        itemNameFld = new TextField(ITEM_NAME);
        itemNameFld.setDescription(ITEM_NAME);
        itemNameFld.setRequiredIndicatorVisible(true);
        firstRow.addComponent(itemNameFld);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        secondRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(secondRow);

        catelogFld = new TextField(CATELOGUE);
        catelogFld.setDescription(CATELOGUE);
        secondRow.addComponent(catelogFld);

        weightFld = new TextField(WEIGHT);
        weightFld.setDescription(WEIGHT);
        secondRow.addComponent(weightFld);

        activateDateFld = new DateField(ACTIVATE_DATE);
        activateDateFld.setRequiredIndicatorVisible(true);
        secondRow.addComponent(activateDateFld);

        deListed = new ComboBox(DE_LISTED);
        deListed.setItems("Yes","No");
        deListed.setSelectedItem("No");
        deListed.setEmptySelectionAllowed(false);
        deListed.setRequiredIndicatorVisible(true);
        secondRow.addComponent(deListed);

        HorizontalLayout thirdRow = new HorizontalLayout();
        thirdRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        thirdRow.setSpacing(true);
        thirdRow.setSizeFull();
        thirdRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(thirdRow);

        baseCurrencyFld = new ComboBox(BASE_CURRENCY);
        baseCurrencyFld.setItems(getCurrencyDropDownValues());
        baseCurrencyFld.setEmptySelectionAllowed(false);
        baseCurrencyFld.setRequiredIndicatorVisible(true);
        thirdRow.addComponent(baseCurrencyFld);

        basePriceFld = new TextField(BASE_PRICE);
        basePriceFld.setDescription(COST_PRICE);
        basePriceFld.setRequiredIndicatorVisible(true);
        thirdRow.addComponent(basePriceFld);

        secondCurrencyFld = new ComboBox(SECOND_CURRENCY);
        secondCurrencyFld.setItems(getCurrencyDropDownValues());
        secondCurrencyFld.setEmptySelectionAllowed(false);
        thirdRow.addComponent(secondCurrencyFld);

        secondPriceFld = new TextField(SECOND_PRICE);
        secondPriceFld.setDescription(SECOND_PRICE);
        thirdRow.addComponent(secondPriceFld);

        HorizontalLayout forthRow = new HorizontalLayout();
        forthRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        forthRow.setSpacing(true);
        forthRow.setSizeFull();
        mainUserInputLayout.addComponent(forthRow);

        costCurrencyFld = new ComboBox(COST_CURRENCY);
        costCurrencyFld.setItems(getCurrencyDropDownValues());
        costCurrencyFld.setEmptySelectionAllowed(false);
        forthRow.addComponent(costCurrencyFld);

        costPriceFld = new TextField(COST_PRICE);
        costPriceFld.setDescription(COST_PRICE);
        forthRow.addComponent(costPriceFld);

        NFCIdField = new TextField(NFC_ID);
        NFCIdField.setDescription(NFC_ID);
        forthRow.addComponent(NFCIdField);

        barCodeField = new TextField(BARCODE);
        barCodeField.setDescription(BARCODE);
        forthRow.addComponent(barCodeField);

        previewField = new MyImageUpload();
        previewField.setAcceptFilter("image/*");
        imageLayout.addComponent(previewField);

        itemDetailsFilterGrid = new FilterGrid<>();
        itemDetailsFilterGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        itemDetailsFilterGrid.setSizeFull();
        tableLayout.addComponent(itemDetailsFilterGrid);
        itemDetailsFilterGrid.addItemClickListener(new ItemClickListener<ItemDetails>() {
            @Override
            public void itemClick(Grid.ItemClick<ItemDetails> itemClick) {
                previewField.setValue(itemClick.getItem().getImage());
            }
        });
        setDataInGrid();
        GridContextMenu<ItemDetails> gridMenu = new GridContextMenu<>(itemDetailsFilterGrid);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);

        outerLayout.setWidth("90%");
        mainTableLayout.setWidth("90%");
    }

    protected void deleteItem(Object target) {
        if (target != null) {
            ItemDetails item = (ItemDetails) target;
            boolean success = connection.deleteObjectHBM(item.getItemId(), className);
            if (success) {
                BackOfficeUtils.showNotification("Success", "Item delete successfully", VaadinIcons.CHECK_CIRCLE_O);
                itemDetails.remove(item);
                itemDetailsFilterGrid.setItems(itemDetails);
            } else {
                BackOfficeUtils.showNotification("Error", "Something wrong, please try again", VaadinIcons.CLOSE);
            }

        }
    }

    private void setDataInGrid(){
        itemDetails = connection.getAllItems();
        itemDetailsFilterGrid.setItems(itemDetails);
        itemDetailsFilterGrid.addColumn(ItemDetails::getServiceType).setCaption(SERVICE_TYPE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getCategory).setCaption(CATEGORY).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getItemCode).setCaption(ITEM_CODE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getItemName).setCaption(ITEM_NAME).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getCatalogue).setCaption(CATELOGUE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getWeight).setCaption(WEIGHT).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getCostCurrency).setCaption(COST_CURRENCY).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getCostPrice).setCaption(COST_PRICE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getBaseCurrency).setCaption(BASE_CURRENCY).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getBasePrice).setCaption(BASE_PRICE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getSecondCurrency).setCaption(SECOND_CURRENCY).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(bean->(String.valueOf(bean.getSecondPrice())).equals("0.0") ? "" : bean.getSecondPrice()).setCaption(SECOND_PRICE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getDeListed).setCaption(DE_LISTED).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getActivateDate).setCaption(ACTIVATE_DATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getNfcId).setCaption(NFC_ID).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        itemDetailsFilterGrid.addColumn(ItemDetails::getBarcode).setCaption(BARCODE).
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
            ItemDetails itemDetails = new ItemDetails();
            itemDetails.setItemId(itemIdVal);
            itemDetails.setItemCode(itemCode.getValue());
            itemDetails.setItemName(itemNameFld.getValue());
            itemDetails.setServiceType(serviceTypeFld.getValue().toString());
            itemDetails.setCategory(categoryFld.getValue().toString());
            itemDetails.setCatalogue(catelogFld.getValue());
            String weight = weightFld.getValue();
            itemDetails.setWeight(weight != null && !weight.isEmpty() ? Float.parseFloat(weightFld.getValue()) : 0);
            Date date = Date.from(activateDateFld.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            String effectiveDateStr = BackOfficeUtils.getDateStringFromDate(date);
            itemDetails.setActivateDate(effectiveDateStr);
            String costCurrency = costCurrencyFld.getValue() != null ? costCurrencyFld.getValue().toString() : "";
            itemDetails.setCostCurrency(costCurrency);
            itemDetails.setCostPrice(costPriceFld.getValue() != null && !costPriceFld.getValue().isEmpty() ? Float.parseFloat(costPriceFld.getValue()) : 0);
            String baseCurrency = baseCurrencyFld.getValue() != null ? baseCurrencyFld.getValue().toString() : "";
            itemDetails.setBaseCurrency(baseCurrency);
            itemDetails.setBasePrice(Float.parseFloat(basePriceFld.getValue()));
            itemDetails.setSecondCurrency(secondCurrencyFld.getValue() != null ? secondCurrencyFld.getValue().toString() : "");
            itemDetails.setSecondPrice(secondPriceFld.getValue() != null && !secondPriceFld.getValue().isEmpty() ? Float.parseFloat(secondPriceFld.getValue()) : 0);
            itemDetails.setDeListed(String.valueOf(deListed.getValue()));
            itemDetails.setImage(previewField.getValue());
            itemDetails.setNfcId(NFCIdField.getValue());
            itemDetails.setBarcode(barCodeField.getValue());
            addOrUpdateDetails(itemDetails,itemIdVal);
            previewField.setValue(null);

        }
    }

    @Override
    protected void fillEditDetails(Object target) {
        if(target != null) {

            ItemDetails itemDetails = (ItemDetails) target;
            idField.setValue(String.valueOf(itemDetails.getItemId()));
            itemCode.setValue(itemDetails.getItemCode());
            itemNameFld.setValue(itemDetails.getItemName());
            serviceTypeFld.setValue(itemDetails.getServiceType());
            categoryFld.setValue(itemDetails.getCategory());
            catelogFld.setValue(itemDetails.getCategory());
            Date input = BackOfficeUtils.convertStringToDate(itemDetails.getActivateDate());
            LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            activateDateFld.setValue(date);
            weightFld.setValue(String.valueOf(itemDetails.getWeight()));
            costCurrencyFld.setValue(itemDetails.getCostCurrency());
            costPriceFld.setValue((String.valueOf(itemDetails.getCostPrice())).equals("0.0") ? "" : String.valueOf(itemDetails.getCostPrice()));
            baseCurrencyFld.setValue(itemDetails.getBaseCurrency());
            basePriceFld.setValue(String.valueOf(itemDetails.getBasePrice()));
            secondCurrencyFld.setValue(itemDetails.getSecondCurrency());
            secondPriceFld.setValue((String.valueOf(itemDetails.getSecondPrice())).equals("0.0") ? "" : String.valueOf(itemDetails.getSecondPrice()));
            deListed.setValue(itemDetails.getDeListed());
            previewField.setValue(itemDetails.getImage());
            NFCIdField.setValue(String.valueOf(itemDetails.getNfcId()));
            barCodeField.setValue(String.valueOf(itemDetails.getBarcode()));
            addButton.setCaption("Save");
            editObj = itemDetails;
            isKeyFieldDirty = false;
        }
    }

    @Override
    protected void defineStringFields() {
        this.filterFieldStr = ITEM_NAME;
        this.pageHeader = "Item Details";
        this.className = "com.back.office.entity.ItemDetails";
        this.keyFieldDBName = "itemCode";
    }

    @Override
    protected void updateTable(boolean isEdit, Object details, int newId) {
        ItemDetails item = (ItemDetails) details;
        if(isEdit){
            int index = itemDetails.indexOf(editObj);
            itemDetails.remove(editObj);
            itemDetails.add(index,item);
        }
        else{
            itemDetails.add(item);
        }
        itemDetailsFilterGrid.setItems(itemDetails);
    }
    @Override
    protected TextField getKeyField() {
        return itemCode;
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
