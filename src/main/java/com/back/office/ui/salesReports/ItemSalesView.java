package com.back.office.ui.salesReports;

import com.back.office.db.DBConnection;
import com.back.office.entity.SalesDetails;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.haijian.Exporter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ItemSalesView extends UserEntryView implements View {

    DBConnection connection;
    protected String pageHeader = "Item Sales by Flight";
    protected VerticalLayout headerLayout;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout mainTableLayout;
    protected HorizontalLayout tableLayout;
    protected VerticalLayout mainUserInputLayout;
    protected Button searchButton;
    protected Button printBtn;
    protected HorizontalLayout buttonRow;

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected TextField sifNoField;
    protected ComboBox categoryComboBox;
    protected ComboBox itemNoCombo;
    protected ComboBox serviceTypeComboBox;
    protected Grid<SalesDetails> detailsTable;
    protected Label filterCriteriaText;
    private Map<Integer,String> itemIdItemCodeMap;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String SIF_NO = "SIF no";
    private final String CATEGORY = "Category";
    private final String ITEM_NO = "Item No";
    private final String SERVICE_TYPE = "Service Type";
    private static final String ITEM_NAME = "Description";
    private static final String ITEM_ID = "Item No";
    private static final String QUANTITY = "Quantity";
    private static final String GROSS_AMOUNT = "Gross Sale";
    private static final String TOTAL = "Total";
    private static final String FLIGHT_DATE = "Flight Date";
    private static final String FLIGHT_NAME = "Flight";
    private static final String FLIGHT_FROM = "From";
    private static final String FLIGHT_TO = "To";

    public ItemSalesView(){
        super();
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    private void createMainLayout() {
        //setSpacing(true);
        setMargin(Constants.noTopMargin);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        headerLayout.setMargin(Constants.noMargin);
        addComponent(headerLayout);
        Label h1 = new Label(pageHeader);
        h1.addStyleName("headerText");
        headerLayout.addComponent(h1);
        itemIdItemCodeMap = connection.getItemIdCodeMap();

        userFormLayout = new VerticalLayout();
        userFormLayout.setMargin(false);
        addComponent(userFormLayout);
        mainTableLayout = new VerticalLayout();
        mainTableLayout.setMargin(Constants.noMargin);
        addComponent(mainTableLayout);
        tableLayout = new HorizontalLayout();
        tableLayout.setMargin(Constants.noMargin);
        tableLayout.setSizeFull();

        mainUserInputLayout = new VerticalLayout();
        mainUserInputLayout.setMargin(Constants.noMargin);
        userFormLayout.addComponent(mainUserInputLayout);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("80%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");
        mainUserInputLayout.addComponent(firstRow);

        Date date = new Date();
        LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        flightDateFromDateField = new DateField(FLIGHT_DATE_FROM);
        flightDateFromDateField.setValue(today);
        flightDateFromDateField.setStyleName("datePickerStyle");
        flightDateFromDateField.setSizeFull();
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(today);
        flightDateToDateField.setStyleName("datePickerStyle");
        flightDateToDateField.setSizeFull();
        firstRow.addComponent(flightDateToDateField);

        sifNoField = new TextField(SIF_NO);
        sifNoField.setDescription(SIF_NO);
        sifNoField.setSizeFull();
        //firstRow.addComponent(sifNoField);

        categoryComboBox = new ComboBox(CATEGORY);
        categoryComboBox.setDescription(CATEGORY);
        List<String> catList = (List<String>) connection.getCategories();
        categoryComboBox.setItems(catList);
        categoryComboBox.setSizeFull();
        //firstRow.addComponent(categoryComboBox);

        itemNoCombo = new ComboBox(ITEM_NO);
        itemNoCombo.setDescription(ITEM_NO);
        itemNoCombo.setItems((List<String>)connection.getItemCodesList());
        itemNoCombo.setSizeFull();
        firstRow.addComponent(itemNoCombo);

        serviceTypeComboBox = new ComboBox(SERVICE_TYPE);
        serviceTypeComboBox.setDescription(SERVICE_TYPE);
        serviceTypeComboBox.setItems("All","Duty Free","Duty Paid","Buy on Board");
        serviceTypeComboBox.setSelectedItem("All");
        serviceTypeComboBox.setEmptySelectionAllowed(false);
        serviceTypeComboBox.setSizeFull();
        firstRow.addComponent(serviceTypeComboBox);

        buttonRow=new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setStyleName("searchButton");
        firstRow.addComponent(buttonRow);

        searchButton = new Button("Search");
        searchButton.addClickListener((Button.ClickListener) clickEvent -> showFilterData());
        buttonRow.addComponent(searchButton);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);
        optionButtonRow.setMargin(Constants.noMargin);


        printBtn = new Button();
        printBtn.setIcon(VaadinIcons.PRINT);
        Button downloadExcelBtn = new Button();
        downloadExcelBtn.setIcon(FontAwesome.FILE_EXCEL_O);
        /*downloadExcelBtn.addClickListener((Button.ClickListener) clickEvent -> {
            ExcelExport excelExport = new ExcelExport(detailsTable);
            excelExport.excludeCollapsedColumns();
            excelExport.setReportTitle("Sales Details");
            excelExport.export();
        });*/
        optionButtonRow.addComponents(printBtn,downloadExcelBtn);
        filterCriteriaText = new Label("");
        filterCriteriaText.addStyleName(ValoTheme.LABEL_H3);
        mainTableLayout.addComponent(filterCriteriaText);
        mainTableLayout.addComponent(optionButtonRow);
        mainTableLayout.setComponentAlignment(optionButtonRow, Alignment.MIDDLE_RIGHT);
        mainTableLayout.addComponent(tableLayout);

        detailsTable = new Grid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        setComponentAlignment(mainTableLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(userFormLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_LEFT);
        createShowTableHeader();

        StreamResource excelStreamResource = new StreamResource((StreamResource.StreamSource)
                () -> Exporter.exportAsCSV(detailsTable), "my-excel.csv");
        FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
        excelFileDownloader.extend(printBtn);
    }

    private void createShowTableHeader(){
        detailsTable.addColumn(bean -> itemIdItemCodeMap.get(bean.getItemId())).setCaption(ITEM_ID).setId("itemId");
        detailsTable.addColumn(SalesDetails::getItemName).setCaption(ITEM_NAME).setId("itemName");
        detailsTable.addColumn(SalesDetails::getCategory).setCaption(CATEGORY).setId("category");
        detailsTable.addColumn(SalesDetails::getQuantity).setCaption(QUANTITY).setId("Qty");
        detailsTable.addColumn(SalesDetails::getPrice).setCaption(GROSS_AMOUNT).setId("price");
        //detailsTable.addColumn(SalesDetails::getPrice).setCaption(TOTAL).setId("price");
        detailsTable.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption(FLIGHT_DATE).setId("flightDate");
        detailsTable.addColumn(SalesDetails::getFlightNo).setCaption(FLIGHT_NAME).setId("flightNo");
        detailsTable.addColumn(SalesDetails::getFlightFrom).setCaption(FLIGHT_FROM).setId("flightFrom");
        detailsTable.addColumn(SalesDetails::getFlightTo).setCaption(FLIGHT_TO).setId("flightTo");
        detailsTable.addColumn(SalesDetails::getSifNo).setCaption(SIF_NO).setId("sifNo");
    }

    protected void showFilterData(){
        mainTableLayout.setVisible(true);
        String itemDesc = itemNoCombo.getValue() != null ? itemNoCombo.getValue().toString() : null;
        String serviceType = BackOfficeUtils.getServiceTypeFromServiceType( serviceTypeComboBox.getValue().toString());

        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<SalesDetails> list = connection.getSalesDetails(dateFrom,dateTo,
                itemDesc,serviceType);

        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo) + " , " +
                "Service Type = " + serviceTypeComboBox.getValue().toString() + ((itemDesc == null || itemDesc.isEmpty()) ? "" : " Item No = " + itemDesc);
        filterCriteriaText.setValue(outputStr);
        detailsTable.setItems(list);
    }
}

