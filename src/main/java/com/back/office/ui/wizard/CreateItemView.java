package com.back.office.ui.wizard;

import com.back.office.entity.ItemDetails;
import com.back.office.framework.*;
import com.back.office.ui.wizard.steps.itemSteps.*;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.ui.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.Wizard;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CreateItemView extends WizardCommonView {

    FilterGrid<ItemDetails> itemDetailsFilterGrid;
    List<ItemDetails> itemDetails;

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

    public CreateItemView(){
        super();
    }

    @Override
    protected void createMainLayout(){
        super.createMainLayout();
        itemDetailsFilterGrid = new FilterGrid<>();
        itemDetailsFilterGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        itemDetailsFilterGrid.setSizeFull();
        tableLayout.addComponent(itemDetailsFilterGrid);
        setDataInGrid();
        itemDetailsFilterGrid.addItemClickListener(itemClick -> {
            openViewEditWindow(false,itemClick.getItem());
        });
    }

    protected void createItemWizard(){
        final Window window = new Window("Create Item");
        window.setWidth("50%");
        window.setHeight(500,Unit.PIXELS);
        final FormLayout content = new FormLayout();
        content.setMargin(true);
        window.center();
        Wizard components = new Wizard();
        components.setSizeFull();
        components.addStep(new CreateItemsFirstStep());
        components.addStep(new CreateItemsSecondStep());
        components.addStep(new CreateItemsThirdStep());
        components.addStep(new CreateItemsFourthStep());
        components.addStep(new CreateItemsFifthStep());
        window.setContent(components);
        components.getCancelButton().addClickListener((Button.ClickListener) clickEvent ->
        {
            ConfirmDialog.show(getUI(), "Add item", "Are you sure you want to cancel the wizard?",
                    "Yes", "No", new ConfirmDialog.Listener() {
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                UI.getCurrent().getSession().setAttribute("item",null);
                                window.close();
                            }
                        }
                    });
        });
        components.getFinishButton().addClickListener((Button.ClickListener) clickEvent -> {
            ConfirmDialog.show(getUI(), "Add item", "Are you sure you want to add this item?",
                    "Yes", "No", new ConfirmDialog.Listener() {
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                Object obj = UI.getCurrent().getSession().getAttribute("item");
                                ItemDetails item;
                                if(obj != null && obj instanceof ItemDetails) {
                                    item = (ItemDetails) obj;
                                    connection.insertObjectHBM(item);
                                    updateTable(false,item);
                                }
                                UI.getCurrent().getSession().setAttribute("item",null);
                                window.close();
                            }
                        }
                    });
        });
        getUI().getUI().addWindow(window);
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
    protected void defineStringFields() {
        headerName = "Item Details";
    }

    private void openViewEditWindow(boolean isEdit,ItemDetails item){
        Window detailsWindow = new Window((isEdit ? "Edit " : "View") + " Item");
        FormLayout formLayout = new FormLayout();
        editObj = item;
        ComboBox serviceTypeFld = new ComboBox(SERVICE_TYPE);
        serviceTypeFld.setItems("BOB","DTF","DTP","VRT");
        serviceTypeFld.setEmptySelectionAllowed(false);
        serviceTypeFld.setValue(item.getServiceType());
        serviceTypeFld.setEnabled(isEdit);
        formLayout.addComponent(serviceTypeFld);

        ComboBox categoryFld = new ComboBox(CATEGORY);
        categoryFld.setEmptySelectionAllowed(false);
        if(isEdit) {
            serviceTypeFld.addValueChangeListener(valueChangeEvent -> {
                if (serviceTypeFld.getValue() != null && !serviceTypeFld.getValue().toString().isEmpty()) {
                    categoryFld.setItems(BackOfficeUtils.getCategoryFromServiceType(serviceTypeFld.getValue().toString()));
                }
            });
        }
        else{
            categoryFld.setEnabled(false);
        }
        categoryFld.setValue(item.getCategory());
        formLayout.addComponent(categoryFld);

        TextField idFld = new TextField();
        idFld.setValue(String.valueOf(item.getItemId()));
        formLayout.addComponents(idFld);
        idFld.setVisible(false);

        TextField itemCode = new TextField(ITEM_CODE);
        itemCode.setValue(item.getItemCode());
        itemCode.setEnabled(isEdit);
        formLayout.addComponents(itemCode);

        TextField itemNameFld = new TextField(ITEM_NAME);
        itemNameFld.setValue(item.getItemName());
        itemNameFld.setEnabled(isEdit);
        formLayout.addComponent(itemNameFld);

        TextField catelogFld = new TextField(CATELOGUE);
        catelogFld.setValue(item.getCatalogue());
        catelogFld.setEnabled(isEdit);
        formLayout.addComponent(catelogFld);

        TextField weightFld = new TextField(WEIGHT);
        weightFld.setValue(String.valueOf(item.getWeight()));
        weightFld.setEnabled(isEdit);
        formLayout.addComponent(weightFld);

        DateField activateDateFld = new DateField(ACTIVATE_DATE);
        Date input = BackOfficeUtils.convertStringToDate(item.getActivateDate());
        LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        activateDateFld.setValue(date);
        activateDateFld.setEnabled(isEdit);
        formLayout.addComponent(activateDateFld);

        ComboBox deListed = new ComboBox(DE_LISTED);
        deListed.setItems("Yes","No");
        deListed.setSelectedItem(item.getDeListed());
        deListed.setEnabled(isEdit);
        formLayout.addComponent(deListed);

        List<String> currencyValues = BackOfficeUtils.getCurrencyDropDownValues(true);

        ComboBox baseCurrencyFld = new ComboBox(BASE_CURRENCY);
        baseCurrencyFld.setItems(currencyValues);
        baseCurrencyFld.setEnabled(isEdit);
        baseCurrencyFld.setValue(item.getBaseCurrency());
        formLayout.addComponent(baseCurrencyFld);

        TextField basePriceFld = new TextField(BASE_PRICE);
        basePriceFld.setEnabled(isEdit);
        basePriceFld.setValue(String.valueOf(item.getBasePrice()));
        formLayout.addComponent(basePriceFld);

        ComboBox secondCurrencyFld = new ComboBox(SECOND_CURRENCY);
        secondCurrencyFld.setItems(currencyValues);
        secondCurrencyFld.setValue(item.getSecondCurrency());
        secondCurrencyFld.setEnabled(isEdit);
        formLayout.addComponent(secondCurrencyFld);

        TextField secondPriceFld = new TextField(SECOND_PRICE);
        secondPriceFld.setEnabled(isEdit);
        secondPriceFld.setValue(String.valueOf(item.getSecondPrice()));
        formLayout.addComponent(secondPriceFld);

        ComboBox costCurrencyFld = new ComboBox(COST_CURRENCY);
        costCurrencyFld.setItems(currencyValues);
        costCurrencyFld.setValue(item.getCostCurrency());
        costCurrencyFld.setEnabled(isEdit);
        formLayout.addComponent(costCurrencyFld);

        TextField costPriceFld = new TextField(COST_PRICE);
        costPriceFld.setEnabled(isEnabled());
        secondPriceFld.setValue(String.valueOf(item.getCostPrice()));
        formLayout.addComponent(costPriceFld);

        TextField NFCIdField = new TextField(NFC_ID);
        NFCIdField.setValue(item.getNfcId());
        NFCIdField.setEnabled(isEdit);
        formLayout.addComponent(NFCIdField);

        TextField barCodeField = new TextField(BARCODE);
        barCodeField.setEnabled(isEdit);
        barCodeField.setValue(item.getBarcode());
        formLayout.addComponent(barCodeField);

        detailsWindow.setWidth("40%");
        detailsWindow.setHeight(500,Unit.PIXELS);
        detailsWindow.center();
        formLayout.setMargin(true);

        MyImageUpload previewField = new MyImageUpload();
        previewField.setAcceptFilter("image/*");
        previewField.setValue(item.getImage());
        previewField.setVisibleUploadBtn(false);
        formLayout.addComponent(previewField);

        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.addComponents(editButton,deleteButton);

        editButton.addClickListener(clickEvent -> {

            if(editButton.getCaption().equals("Edit")){
            itemCode.setEnabled(true);
            itemNameFld.setEnabled(true);
            serviceTypeFld.setEnabled(true);
            categoryFld.setEnabled(true);
            catelogFld.setEnabled(true);
            weightFld.setEnabled(true);
            activateDateFld.setEnabled(true);
            deListed.setEnabled(true);
            baseCurrencyFld.setEnabled(true);
            basePriceFld.setEnabled(true);
            secondCurrencyFld.setEnabled(true);
            secondPriceFld.setEnabled(true);
            costCurrencyFld.setEnabled(true);
            costPriceFld.setEnabled(true);
            barCodeField.setEnabled(true);
            NFCIdField.setEnabled(true);
            previewField.setVisibleUploadBtn(true);
            editButton.setCaption("Save");
            }
            else{

                connection.updateRecordStatus(Integer.parseInt(idFld.getValue()),
                        "com.back.office.entity.ItemDetails");
                ItemDetails editItem = new ItemDetails();
                editItem.setItemCode(itemCode.getValue());
                editItem.setItemName(itemNameFld.getValue());
                editItem.setServiceType(String.valueOf(serviceTypeFld.getValue()));
                editItem.setCategory(String.valueOf(categoryFld.getValue()));
                editItem.setCatalogue(catelogFld.getValue());
                editItem.setWeight(Float.parseFloat(weightFld.getValue()));
                Date dateVal = Date.from(activateDateFld.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                String effectiveDateStr = BackOfficeUtils.getDateStringFromDate(dateVal);
                editItem.setActivateDate(effectiveDateStr);
                editItem.setDeListed(String.valueOf(deListed.getValue()));
                editItem.setBaseCurrency(String.valueOf(baseCurrencyFld.getValue()));
                editItem.setBasePrice(Float.parseFloat(basePriceFld.getValue()));
                editItem.setCostCurrency(String.valueOf(costCurrencyFld.getValue()));
                editItem.setCostPrice(Float.parseFloat(costPriceFld.getValue()));
                editItem.setSecondPrice(Float.parseFloat(secondPriceFld.getValue()));
                editItem.setSecondCurrency(String.valueOf(secondCurrencyFld.getValue()));
                editItem.setImage(previewField.getValue());
                editItem.setNfcId(NFCIdField.getValue());
                editItem.setBarcode(barCodeField.getValue());
                connection.insertObjectHBM(editItem);
                updateTable(true,editItem);
                detailsWindow.close();

            }
        });
        deleteButton.addClickListener(clickEvent -> {
            ConfirmDialog.show(getUI(), "Delete", "Are you sure you want to delete this item?",
                    "Yes", "No", new ConfirmDialog.Listener() {
                        public void onClose(ConfirmDialog dialog) {
                            if (dialog.isConfirmed()) {
                                connection.deleteObjectHBM(item);
                                detailsWindow.close();
                            }
                        }
            });
        });

        formLayout.addComponents(btnLayout);
        detailsWindow.setContent(formLayout);
        getUI().addWindow(detailsWindow);
    }

    protected void updateTable(boolean isEdit, Object details) {
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
}
