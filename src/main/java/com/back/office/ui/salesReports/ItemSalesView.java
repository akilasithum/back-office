package com.back.office.ui.salesReports;

import com.back.office.db.DBConnection;
import com.back.office.entity.ItemDetails;
import com.back.office.entity.SalesDetails;
import com.back.office.entity.Sector;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.haijian.Exporter;
import sun.font.FontAccess;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ItemSalesView extends UserEntryView implements View {

    DBConnection connection;
    protected String pageHeader = "Item Sales";
    protected VerticalLayout headerLayout;
    protected VerticalLayout mainTableLayout;
    protected HorizontalLayout tableLayout;
    protected Button searchButton;
    protected Button printBtn;
    protected HorizontalLayout buttonRow;

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected TextField sifNoField;
    protected ComboBox categoryComboBox;
    protected Grid<SalesDetails> detailsTable;
    protected Label filterCriteriaText;
    protected TextField itemNo;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String SIF_NO = "SIF no";
    private final String CATEGORY = "Category";
    private final String SECTOR = "Sector";
    private final String SERVICE_TYPE = "Service Type";
    private static final String ITEM_NAME = "Item Description";
    private static final String ITEM_ID = "Item No";
    private static final String QUANTITY = "Quantity";
    private static final String GROSS_AMOUNT = "Gross Sales";
    private static final String TOTAL = "Total";
    private static final String FLIGHT_DATE = "Flight Date";
    private static final String FLIGHT_NAME = "Flight #";
    private static final String FLIGHT_FROM = "From";
    private static final String FLIGHT_TO = "To";
    Map<String, ItemDetails> itemIdNameMap;

    public ItemSalesView(){
        super();
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
        itemIdNameMap = connection.getItemCodeDetailsMap();
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

        mainTableLayout = new VerticalLayout();
        mainTableLayout.setMargin(Constants.noMargin);

        tableLayout = new HorizontalLayout();
        tableLayout.setMargin(Constants.noMargin);
        tableLayout.setSizeFull();

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName("report-filter-panel");
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);

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
        //firstRow.addComponent(sifNoField);

        categoryComboBox = new ComboBox(CATEGORY);
        categoryComboBox.setDescription(CATEGORY);
        List<String> catList = (List<String>) connection.getCategories();
        categoryComboBox.setItems(catList);
        firstRow.addComponent(categoryComboBox);

        itemNo = new TextField(ITEM_ID);
        itemNo.setDescription(ITEM_ID);
        firstRow.addComponent(itemNo);
        firstRow.setSizeFull();
        firstRow.setWidth("70%");
        flightDateFromDateField.setSizeFull();
        flightDateFromDateField.setStyleName("datePickerStyle");
        flightDateToDateField.setSizeFull();
        flightDateToDateField.setStyleName("datePickerStyle");
        categoryComboBox.setSizeFull();
        itemNo.setSizeFull();


        searchButton = new Button("Search");
        searchButton.addClickListener((Button.ClickListener) clickEvent -> showFilterData());

        buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setStyleName("searchButton");
        buttonRow.addComponent(searchButton);
        firstRow.addComponent(buttonRow);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);
        optionButtonRow.setMargin(Constants.noMargin);

        printBtn = new Button();
        printBtn.setDescription("Print");
        printBtn.setIcon(FontAwesome.PRINT);
        Button downloadExcelBtn = new Button();
        downloadExcelBtn.setIcon(FontAwesome.FILE_EXCEL_O);
        downloadExcelBtn.setDescription("Download as excel");
        /*downloadExcelBtn.addClickListener((Button.ClickListener) clickEvent -> {
            ExcelExport excelExport = new ExcelExport(detailsTable);
            excelExport.excludeCollapsedColumns();
            excelExport.setReportTitle("Sales Details");
            excelExport.export();
        });*/
        optionButtonRow.addComponents(printBtn,downloadExcelBtn);
        filterCriteriaText = new Label("");
        filterCriteriaText.addStyleName(ValoTheme.LABEL_H3);

        HorizontalLayout buttonAndLabelLayout = new HorizontalLayout();
        buttonAndLabelLayout.setSizeFull();
        buttonAndLabelLayout.addComponents(filterCriteriaText,optionButtonRow);
        buttonAndLabelLayout.setComponentAlignment(optionButtonRow,Alignment.MIDDLE_RIGHT);

        mainTableLayout.addComponent(buttonAndLabelLayout);
        //mainTableLayout.addComponent(filterCriteriaText);
        mainTableLayout.addComponent(tableLayout);

        detailsTable = new Grid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        addComponent(firstRow);
        addComponent(mainTableLayout);
        setComponentAlignment(mainTableLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(firstRow,Alignment.MIDDLE_LEFT);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_LEFT);
        createShowTableHeader();

        StreamResource excelStreamResource = new StreamResource((StreamResource.StreamSource)
                () -> Exporter.exportAsCSV(detailsTable), "my-excel.csv");
        FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
        excelFileDownloader.extend(printBtn);
    }

    private void createShowTableHeader(){
        detailsTable.addColumn(SalesDetails::getItemId).setCaption(ITEM_ID).setId("itemId");
        detailsTable.addColumn(bean-> getItemNameFromId(bean.getItemId())).setCaption(ITEM_NAME).setId("itemName");
        detailsTable.addColumn(SalesDetails::getCategory).setCaption(CATEGORY).setId("category");
        detailsTable.addColumn(SalesDetails::getQuantity).setCaption(QUANTITY).setId("quantity");
        detailsTable.addColumn(SalesDetails::getPrice).setCaption(GROSS_AMOUNT).setId("price");
        //detailsTable.addColumn(SalesDetails::getPrice).setCaption(TOTAL).setId("price");
        detailsTable.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption(FLIGHT_DATE).setId("flightDate");
        detailsTable.addColumn(SalesDetails::getFlightNo).setCaption(FLIGHT_NAME).setId("flightNo");
        /*detailsTable.addColumn(SalesDetails::getFlightFrom).setCaption(FLIGHT_FROM).setId("flightFrom");
        detailsTable.addColumn(SalesDetails::getFlightTo).setCaption(FLIGHT_TO).setId("flightTo");*/
    }

    private String getItemNameFromId(String itemId){

        ItemDetails item = itemIdNameMap.get(itemId);
        if(item != null) return item.getItemName();
        else return "";
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
        String itemNoVal = itemNo.getValue() != null ? itemNo.getValue() : null;


        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<SalesDetails> list = connection.getSalesDetails(dateFrom,dateTo,
                category,itemNoVal);

        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo) + " , "
                + ((category == null || category.isEmpty()) ? "" : " category = " + category)
                + (itemNoVal == null || itemNoVal.isEmpty() ? "" :" Item No" + itemNoVal);
        filterCriteriaText.setValue(outputStr);
        detailsTable.setItems(list);
    }
}

