package com.back.office.ui.salesReports;

import com.back.office.db.DBConnection;
import com.back.office.entity.SalesDetails;
import com.back.office.entity.Sector;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.haijian.Exporter;

import java.time.LocalDate;
import java.time.ZoneId;
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
    protected Button printBtn;
    protected HorizontalLayout buttonRow;

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected TextField sifNoField;
    protected ComboBox categoryComboBox;
    protected ComboBox sectorComboBox;
    protected ComboBox serviceTypeComboBox;
    protected Grid<SalesDetails> detailsTable;
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
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    private void createMainLayout() {
        setSpacing(true);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        headerLayout.setMargin(Constants.noMargin);
        addComponent(headerLayout);
        Label h1 = new Label(pageHeader);
        h1.addStyleName(ValoTheme.LABEL_H1);
        headerLayout.addComponent(h1);

        userFormLayout = new VerticalLayout();
        userFormLayout.setMargin(Constants.noMargin);
        addComponent(userFormLayout);
        mainTableLayout = new VerticalLayout();
        mainTableLayout.setMargin(Constants.noMargin);
        addComponent(mainTableLayout);
        mainTableLayout.setVisible(false);
        tableLayout = new HorizontalLayout();
        tableLayout.setMargin(Constants.noMargin);
        tableLayout.setSizeFull();

        mainUserInputLayout = new VerticalLayout();
        mainUserInputLayout.setMargin(Constants.noMargin);
        userFormLayout.addComponent(mainUserInputLayout);
        userFormLayout.setWidth("70%");

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        HorizontalLayout secondRow = new HorizontalLayout();
        secondRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        secondRow.setSpacing(true);
        secondRow.setSizeFull();
        secondRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(secondRow);

        Date date = new Date();
        LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        flightDateFromDateField = new DateField(FLIGHT_DATE_FROM);
        flightDateFromDateField.setValue(today);
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(today);
        firstRow.addComponent(flightDateToDateField);

        sifNoField = new TextField(SIF_NO);
        sifNoField.setDescription(SIF_NO);
        firstRow.addComponent(sifNoField);

        categoryComboBox = new ComboBox(CATEGORY);
        categoryComboBox.setDescription(CATEGORY);
        List<String> catList = (List<String>) connection.getCategories();
        categoryComboBox.setItems(catList);
        secondRow.addComponent(categoryComboBox);

        sectorComboBox = new ComboBox(SECTOR);
        sectorComboBox.setDescription(SECTOR);
        sectorComboBox.setItems(getSectors());
        secondRow.addComponent(sectorComboBox);

        serviceTypeComboBox = new ComboBox(SERVICE_TYPE);
        serviceTypeComboBox.setDescription(SERVICE_TYPE);
        serviceTypeComboBox.setItems("All","Duty Free","Duty Paid","Buy on Board");
        serviceTypeComboBox.setSelectedItem("All");
        serviceTypeComboBox.setEmptySelectionAllowed(false);
        secondRow.addComponent(serviceTypeComboBox);

        buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setSpacing(true);
        buttonRow.setMargin(Constants.noMargin);
        userFormLayout.addComponent(buttonRow);

        searchButton = new Button("Search");
        searchButton.addClickListener((Button.ClickListener) clickEvent -> showFilterData());
        buttonRow.addComponent(searchButton);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);
        optionButtonRow.setMargin(Constants.noMargin);

        printBtn = new Button("Print");
        Button downloadExcelBtn = new Button("Download as Excel");
        /*downloadExcelBtn.addClickListener((Button.ClickListener) clickEvent -> {
            ExcelExport excelExport = new ExcelExport(detailsTable);
            excelExport.excludeCollapsedColumns();
            excelExport.setReportTitle("Sales Details");
            excelExport.export();
        });*/
        optionButtonRow.addComponents(printBtn,downloadExcelBtn);
        filterCriteriaText = new Label("");
        filterCriteriaText.addStyleName(ValoTheme.LABEL_H3);
        mainTableLayout.addComponent(optionButtonRow);
        mainTableLayout.addComponent(filterCriteriaText);
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
        detailsTable.addColumn(SalesDetails::getItemName).setCaption(ITEM_NAME).setId("itemName");
        detailsTable.addColumn(SalesDetails::getCategory).setCaption(CATEGORY).setId("category");
        detailsTable.addColumn(SalesDetails::getItemId).setCaption(ITEM_ID).setId("itemId");
        detailsTable.addColumn(SalesDetails::getQuantity).setCaption(QUANTITY).setId("quantity");
        detailsTable.addColumn(SalesDetails::getPrice).setCaption(GROSS_AMOUNT).setId("price");
        //detailsTable.addColumn(SalesDetails::getPrice).setCaption(TOTAL).setId("price");
        detailsTable.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption(FLIGHT_DATE).setId("flightDate");
        detailsTable.addColumn(SalesDetails::getFlightNo).setCaption(FLIGHT_NAME).setId("flightNo");
        detailsTable.addColumn(SalesDetails::getFlightFrom).setCaption(FLIGHT_FROM).setId("flightFrom");
        detailsTable.addColumn(SalesDetails::getFlightTo).setCaption(FLIGHT_TO).setId("flightTo");
        detailsTable.addColumn(SalesDetails::getSifNo).setCaption(SIF_NO).setId("sifNo");
    }

    private List<String> getSectors(){

        List<Sector> sectors = (List<Sector>)connection.getSectors("com.back.office.entity.Sector");
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
        String category = categoryComboBox.getValue() != null ? categoryComboBox.getValue().toString() : null;
        String serviceType = BackOfficeUtils.getServiceTypeFromServiceType( serviceTypeComboBox.getValue().toString());
        String sifNo = sifNoField.getValue() != null ? sifNoField.getValue().toString() : null;
        String flightFrom = null;
        String flightTo = null;
        if(sectorComboBox.getValue() != null && !sectorComboBox.getValue().toString().isEmpty()){
            flightFrom = sectorComboBox.getValue().toString().split("-")[0];
            flightTo = sectorComboBox.getValue().toString().split("-")[1];
        }
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<SalesDetails> list = connection.getSalesDetails(dateFrom,dateTo,
                category,serviceType,flightFrom,flightTo,sifNo);

        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo) + " , " +
                "Service Type = " + serviceTypeComboBox.getValue().toString() + ((category == null || category.isEmpty()) ? "" : " category = " + category)
                + (sifNo == null || sifNo.isEmpty() ? "" :" SIF No" + sifNo);
        filterCriteriaText.setValue(outputStr);
        detailsTable.setItems(list);
    }
}
