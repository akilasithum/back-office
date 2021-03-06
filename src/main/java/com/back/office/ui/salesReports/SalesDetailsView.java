package com.back.office.ui.salesReports;

import com.back.office.db.DBConnection;
import com.back.office.entity.SalesDetails;
import com.back.office.entity.Sector;
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

import static com.back.office.utils.BackOfficeUtils.showWithDollarSign;

public class SalesDetailsView extends UserEntryView implements View {

    DBConnection connection;
    protected String pageHeader = "Item Sales Summary";
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
    protected ComboBox flightNoComboBox;
    protected Grid<SalesDetails> detailsTable;
    protected Label filterCriteriaText;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String FLIGHT_NO = "Flight No";
    private static final String QUANTITY = "Quantity";

    public SalesDetailsView(){
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
        setMargin(false);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        headerLayout.setMargin(Constants.noMargin);
        addComponent(headerLayout);
        Label h1 = new Label(pageHeader);
        h1.addStyleName("headerText");
        headerLayout.addComponent(h1);

        userFormLayout = new VerticalLayout();
        userFormLayout.setMargin(Constants.noMargin);
        addComponent(userFormLayout);
        mainTableLayout = new VerticalLayout();
        mainTableLayout.setMargin(Constants.noMargin);
        addComponent(mainTableLayout);
        //mainTableLayout.setVisible(false);
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
        firstRow.setWidth("60%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");
        mainUserInputLayout.addComponent(firstRow);

        Date date = new Date();
        LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        flightDateFromDateField = new DateField(FLIGHT_DATE_FROM);
        flightDateFromDateField.setValue(today);
        flightDateFromDateField.setSizeFull();
        flightDateFromDateField.setStyleName("datePickerStyle");
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(today);
        flightDateToDateField.setSizeFull();
        flightDateToDateField.setStyleName("datePickerStyle");
        firstRow.addComponent(flightDateToDateField);


        flightNoComboBox = new ComboBox(FLIGHT_NO);
        flightNoComboBox.setDescription(FLIGHT_NO);
        flightNoComboBox.setSizeFull();
        List<String> catList = connection.getFlightsNoList();
        flightNoComboBox.setItems(catList);
        firstRow.addComponent(flightNoComboBox);
        //firstRow.addComponent(searchButton);

        buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
       buttonRow.setStyleName("searchButton");
       userFormLayout.addComponent(buttonRow);

        searchButton = new Button("Search");
        searchButton.addClickListener((Button.ClickListener) clickEvent -> showFilterData());
        buttonRow.addComponent(searchButton);
        firstRow.addComponent(buttonRow);

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
        mainTableLayout.addComponent(tableLayout);

        mainTableLayout.setComponentAlignment(optionButtonRow, Alignment.MIDDLE_RIGHT);

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
        detailsTable.addColumn(SalesDetails::getItemId).setCaption("Item No").setId("Item No");
        detailsTable.addColumn(SalesDetails::getItemName).setCaption("Description").setId("Description");
        detailsTable.addColumn(bean-> showWithDollarSign(bean.getPrice()/bean.getQuantity())).setCaption("Price").setId("Price");
        detailsTable.addColumn(bean-> showWithDollarSign(bean.getCostPrice()/bean.getQuantity())).setCaption("Cost").setId("Cost");
        detailsTable.addColumn(SalesDetails::getQuantity).setCaption(QUANTITY).setId("quantity");
        detailsTable.addColumn(bean-> showWithDollarSign(bean.getPrice())).setCaption("Gross Sale").setId("Gross Sale");
        detailsTable.addColumn(bean-> showWithDollarSign(bean.getPrice())).setCaption("Net Sale").setId("Net Sale");
        detailsTable.addColumn(bean-> showWithDollarSign(bean.getCostPrice())).setCaption("Net Cost").setId("Net Cost");

    }

    protected void showFilterData(){
        mainTableLayout.setVisible(true);
        String flightNo = flightNoComboBox.getValue() != null ? flightNoComboBox.getValue().toString() : null;

        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<SalesDetails> list = connection.getMonthlySales(dateFrom,dateTo,flightNo);

        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo) +
                 ((flightNo == null || flightNo.isEmpty()) ? "" : " Flight Number = " + flightNo);
        filterCriteriaText.setValue(outputStr);
        detailsTable.setItems(list);
    }
}
