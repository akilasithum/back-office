package com.back.office.ui.wizard;

import com.back.office.db.DBConnection;
import com.back.office.entity.ItemDetails;
import com.back.office.ui.wizard.steps.itemSteps.*;
import com.vaadin.navigator.View;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.teemu.wizards.Wizard;

import java.util.List;

public class CreateItemView extends VerticalLayout implements View {

    VerticalLayout headerLayout;
    HorizontalLayout buttonLayout;
    HorizontalLayout tableLayout;
    Button addNewButton;
    Button editButton;
    Button deleteButton;
    Button viewButton;
    FilterGrid<ItemDetails> itemDetailsFilterGrid;
    List<ItemDetails> itemDetails;
    DBConnection connection;

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
        connection = DBConnection.getInstance();
        createLayout();
    }

    private void createLayout(){
        MarginInfo marginInfo = new MarginInfo(false,false,false,false);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        Label h1 = new Label("Create Items");
        h1.addStyleName(ValoTheme.LABEL_H1);
        headerLayout.addComponent(h1);
        headerLayout.setMargin(marginInfo);

        buttonLayout = new HorizontalLayout();
        buttonLayout.setMargin(marginInfo);
        buttonLayout.setSizeFull();
        addComponent(buttonLayout);

        addNewButton = new Button("Add");
        addNewButton.setSizeFull();
        addNewButton.addClickListener((Button.ClickListener) clickEvent -> createItemWizard());
        editButton = new Button("Edit");
        editButton.setSizeFull();
        deleteButton = new Button("Delete");
        deleteButton.setSizeFull();
        viewButton = new Button("View");
        viewButton.setSizeFull();
        buttonLayout.addComponents(viewButton,addNewButton,editButton,deleteButton);

        tableLayout = new HorizontalLayout();
        tableLayout.setSizeFull();
        tableLayout.setMargin(marginInfo);
        tableLayout.setHeightUndefined();
        addComponent(tableLayout);

        itemDetailsFilterGrid = new FilterGrid<>();
        itemDetailsFilterGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        itemDetailsFilterGrid.setSizeFull();
        tableLayout.addComponent(itemDetailsFilterGrid);
        setDataInGrid();
        buttonLayout.setWidth("50%");
        setComponentAlignment(tableLayout, Alignment.MIDDLE_LEFT);
        setComponentAlignment(buttonLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_LEFT);
    }

    private void createItemWizard(){
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

    protected TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        return filter;
    }
}
