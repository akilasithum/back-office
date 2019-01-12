package com.back.office.ui.salesReports;

import com.back.office.db.DBConnection;
import com.back.office.db.SQLConnection;
import com.back.office.entity.SalesDetails;
import com.back.office.entity.Sector;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.util.*;

public class SalesDetailsView extends VerticalLayout implements View {

    DBConnection connection;
    protected String pageHeader = "Sales Details";
    protected VerticalLayout headerLayout;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout mainTableLayout;
    protected HorizontalLayout tableLayout;
    protected VerticalLayout mainUserInputLayout;
    protected Button searchButton;
    protected HorizontalLayout buttonRow;

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected TextField sifNoField;
    protected ComboBox categoryComboBox;
    protected ComboBox sectorComboBox;
    protected ComboBox serviceTypeComboBox;
    protected Table detailsTable;
    protected Label filterCriteriaText;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String SIF_NO = "SIF no";
    private final String CATEGORY = "Category";
    private final String SECTOR = "Sector";
    private final String SERVICE_TYPE = "Service Type";
    private static final String ITEM_NAME = "Item Name";
    private static final String ITEM_ID = "Item Id";
    private static final String QUANTITY = "Quantity";
    private static final String GROSS_AMOUNT = "Gross Amount";
    private static final String TOTAL = "Total";
    private static final String FLIGHT_DATE = "Flight Date";
    private static final String FLIGHT_NAME = "Flight";
    private static final String FLIGHT_FROM = "From";
    private static final String FLIGHT_TO = "To";

    public SalesDetailsView(){
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    private void createMainLayout() {
        setSpacing(true);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        addComponent(headerLayout);
        Label h1 = new Label(pageHeader);
        h1.addStyleName(ValoTheme.LABEL_H1);
        headerLayout.addComponent(h1);

        userFormLayout = new VerticalLayout();
        addComponent(userFormLayout);
        mainTableLayout = new VerticalLayout();
        addComponent(mainTableLayout);
        mainTableLayout.setVisible(false);
        tableLayout = new HorizontalLayout();
        tableLayout.setSizeFull();

        mainUserInputLayout = new VerticalLayout();
        userFormLayout.addComponent(mainUserInputLayout);
        userFormLayout.setWidth("70%");

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        MarginInfo marginInfo = new MarginInfo(false,false,true,false);
        firstRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(firstRow);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        secondRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(secondRow);

        flightDateFromDateField = new DateField(FLIGHT_DATE_FROM);
        flightDateFromDateField.setValue(new Date());
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(new Date());
        firstRow.addComponent(flightDateToDateField);

        sifNoField = new TextField(SIF_NO);
        sifNoField.setInputPrompt(SIF_NO);
        firstRow.addComponent(sifNoField);

        categoryComboBox = new ComboBox(CATEGORY);
        categoryComboBox.setInputPrompt(CATEGORY);
        List<String> catList = (List<String>) connection.getCategories();
        categoryComboBox.addItems(catList);
        secondRow.addComponent(categoryComboBox);

        sectorComboBox = new ComboBox(SECTOR);
        sectorComboBox.setInputPrompt(SECTOR);
        sectorComboBox.addItems(getSectors());
        secondRow.addComponent(sectorComboBox);

        serviceTypeComboBox = new ComboBox(SERVICE_TYPE);
        serviceTypeComboBox.setInputPrompt(SERVICE_TYPE);
        serviceTypeComboBox.addItem("All");
        serviceTypeComboBox.addItem("Duty Free");
        serviceTypeComboBox.addItem("Duty Paid");
        serviceTypeComboBox.addItem("Buy on Board");
        serviceTypeComboBox.select("All");
        serviceTypeComboBox.setNullSelectionAllowed(false);
        secondRow.addComponent(serviceTypeComboBox);

        buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setSpacing(true);
        userFormLayout.addComponent(buttonRow);

        searchButton = new Button("Search");
        searchButton.addClickListener((Button.ClickListener) clickEvent -> showFilterData());
        buttonRow.addComponent(searchButton);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);

        Button printBtn = new Button("Print");
        Button downloadExcelBtn = new Button("Download as Excel");
        downloadExcelBtn.addClickListener((Button.ClickListener) clickEvent -> {
            ExcelExport excelExport = new ExcelExport(detailsTable);
            excelExport.excludeCollapsedColumns();
            excelExport.setReportTitle("Sales Details");
            excelExport.export();
        });
        optionButtonRow.addComponents(printBtn,downloadExcelBtn);
        filterCriteriaText = new Label("");
        filterCriteriaText.addStyleName(ValoTheme.LABEL_H1);
        mainTableLayout.addComponent(optionButtonRow);
        mainTableLayout.addComponent(filterCriteriaText);
        mainTableLayout.addComponent(tableLayout);

        detailsTable = new Table();
        detailsTable.setSelectable(true);
        detailsTable.setMultiSelect(false);
        detailsTable.setSortEnabled(true);
        detailsTable.setColumnCollapsingAllowed(true);
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setPageLength(10);
        detailsTable.setSizeFull();
        detailsTable.setContainerDataSource(generateContainer());
        tableLayout.addComponent(detailsTable);
        setComponentAlignment(mainTableLayout,Alignment.MIDDLE_CENTER);
        setComponentAlignment(userFormLayout,Alignment.MIDDLE_CENTER);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_CENTER);

    }

    private List<String> getSectors(){

        List<Sector> sectors = (List<Sector>)connection.getAllValues("com.back.office.entity.Sector");
        List<String> sectorsStrList  = new ArrayList<>();
        for(Sector sector : sectors){
            String sectorStr = sector.getSectorFrom() + "-" + sector.getSectorTo();
            if(!sectorsStrList.contains(sectorStr)){
                sectorsStrList.add(sectorStr);
            }
        }
        return sectorsStrList;
    }

    protected void showFilterData(){
        mainTableLayout.setVisible(true);
        SQLConnection sqlConnection = new SQLConnection();
        sqlConnection.getSalesDetails();
        String category = categoryComboBox.getValue() != null ? categoryComboBox.getValue().toString() : null;
        String serviceType = BackOfficeUtils.getServiceTypeFromServiceType( serviceTypeComboBox.getValue().toString());
        String sifNo = sifNoField.getValue() != null ? sifNoField.getValue().toString() : null;
        String flightFrom = null;
        String flightTo = null;
        if(sectorComboBox.getValue() != null && !sectorComboBox.getValue().toString().isEmpty()){
            flightFrom = sectorComboBox.getValue().toString().split("-")[0];
            flightTo = sectorComboBox.getValue().toString().split("-")[1];
        }
        List<SalesDetails> list = connection.getSalesDetails(flightDateFromDateField.getValue(),flightDateToDateField.getValue(),
                category,serviceType,flightFrom,flightTo,sifNo);
        Container container = detailsTable.getContainerDataSource();
        container.removeAllItems();
        for(SalesDetails details : list){
            Item item = container.addItem(details.getId());
            item.getItemProperty(ITEM_NAME).setValue(details.getItemName());
            item.getItemProperty(CATEGORY).setValue(details.getCategory());
            item.getItemProperty(ITEM_ID).setValue(details.getItemId());
            item.getItemProperty(QUANTITY).setValue(details.getQuantity());
            item.getItemProperty(GROSS_AMOUNT).setValue(details.getCostPrice());
            item.getItemProperty(TOTAL).setValue(details.getPrice());
            item.getItemProperty(FLIGHT_DATE).setValue(BackOfficeUtils.getDateFromDateTime(details.getFlightDate()));
            item.getItemProperty(FLIGHT_NAME).setValue(details.getFlightNo());
            item.getItemProperty(FLIGHT_FROM).setValue(details.getFlightFrom());
            item.getItemProperty(FLIGHT_TO).setValue(details.getFlightTo());
            item.getItemProperty(SIF_NO).setValue(details.getSifNo());
        }
    }

    protected IndexedContainer generateContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(ITEM_NAME, String.class, null);
        container.addContainerProperty(CATEGORY, String.class, null);
        container.addContainerProperty(ITEM_ID, Integer.class, null);
        container.addContainerProperty(QUANTITY, Integer.class, null);
        container.addContainerProperty(GROSS_AMOUNT, Float.class, null);
        container.addContainerProperty(TOTAL, Float.class, null);
        container.addContainerProperty(FLIGHT_DATE, String.class, null);
        container.addContainerProperty(FLIGHT_NAME, String.class, null);
        container.addContainerProperty(FLIGHT_FROM, String.class, null);
        container.addContainerProperty(FLIGHT_TO, String.class, null);
        container.addContainerProperty(SIF_NO, Integer.class, null);
        return container;
    }
}
