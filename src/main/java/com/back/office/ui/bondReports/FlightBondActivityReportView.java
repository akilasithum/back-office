package com.back.office.ui.bondReports;

import com.back.office.entity.*;
import com.back.office.ui.salesReports.ReportCommonView;
import com.back.office.utils.*;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;
import org.vaadin.dialogs.ConfirmDialog;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class FlightBondActivityReportView extends ReportCommonView {

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected FilterGrid<SIFDetails> detailsTable;
    List<SIFDetails> list;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String SIF_NO = "SIF No";
    private final String DEVICE_ID = "HHC No";
    private final String DOWNLOADED = "Downloaded";
    private final String PACKED_FOR =  "Packed For";
    private final String PACKED_TIME = "Packed";
    private final String CREW_OPEN_TIME = "Crew Opened";
    private final String CREW_CLOSE_TIME = "Crew Closed";
    private final String DEPARTURE_DATE = "Departure Date";
    private final String STATUS = "Status";
    private final String PROGRAM = "Service";
    Button downloadPdfBtn;

    public FlightBondActivityReportView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("60%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");

        mainUserInputLayout.addComponent(firstRow);
        downloadPdfBtn = new Button();
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
        firstRow.addComponents(flightDateToDateField,buttonRow);

        detailsTable = new FilterGrid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        //userFormLayout.setWidth("40%");
        createShowTableHeader();
        //GridContextMenu<SIFDetails> gridMenu = new GridContextMenu<>(detailsTable);
        //gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);
    }

    protected void updateGridBodyMenu(SIFDetails sifDetails,String serviceType) {
            if(sifDetails.getPrograms() != null && !sifDetails.getPrograms().isEmpty()) {
                if (serviceType.equalsIgnoreCase("BOB")) {
                    sifDetails.setDownloadType("Buy on board");
                    downloadFile(sifDetails);
                }
                if (serviceType.equalsIgnoreCase("DTF")) {
                     sifDetails.setDownloadType("Duty Free");
                     downloadFile(sifDetails);
                }
                if (serviceType.equalsIgnoreCase("DTP")) {
                    sifDetails.setDownloadType("Duty Paid");
                    downloadFile(sifDetails);
                }
                if (serviceType.equalsIgnoreCase("VRT")) {
                    sifDetails.setDownloadType("Virtual Inventory");
                    downloadFile(sifDetails);
                }
            }
    }

    private void downloadFile(SIFDetails sifDetails){
        DownloadHelper helper = new DownloadHelperImpl();
        helper.createFile(new DownloadHelper.DownloadServiceListener() {
            @Override
            public void onComplete(String path) {
                if (path != null && !path.equals("")) {
                    try {
                        File file = new File(path);
                        StreamResource resource = getExistingFile(sifDetails.getDownloadType().
                                replace(" ","_")+".pdf", path);
                        getUI().getPage().open(resource, "_blank", false);
                        file.deleteOnExit();
                    } catch (Exception e) {
                        Notification.show("Something went worng", Notification.Type.WARNING_MESSAGE);
                    }
                }
            }
            @Override
            public void onFail() {

            }
        },sifDetails);
    }

    /*private void exportToExcel(){
        String[] arr = {SIF_NO,PACKED_FOR,DEVICE_ID,PACKED_TIME,CREW_OPEN_TIME,CREW_CLOSE_TIME,DOWNLOADED};
        exportToExcel("SIF Details",arr);
    }*/
    @Override
    protected void defineStringFields() {
        this.pageHeader = "SIF Details";
        this.reportExcelHeader = "SIF Details";
        String[] arr = {SIF_NO,PACKED_FOR,DEVICE_ID,PACKED_TIME,CREW_OPEN_TIME,CREW_CLOSE_TIME,DOWNLOADED};
        this.excelColumnArr = arr;
    }

    @Override
    protected void showFilterData() {

        mainTableLayout.setVisible(true);
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        list = connection.getSifDetails(dateFrom,dateTo);
        String outputStr = "Flight Date From: " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To: " + BackOfficeUtils.getDateFromDateTime(dateTo);
        filterCriteriaText.setValue(outputStr);
        detailsTable.setItems(list);
        ;
        //optionButtonRow.removeComponent(optionButtonRow.getComponent(1));
        //File file = exportToExcel("SIF Details",arr);
        //downloadExcelBtn = getDownloadExcelBtn("SIF Details",file);
        //optionButtonRow.addComponent(downloadExcelBtn);

    }

    @Override
    protected PdfPTable getPdfTable(PdfPTable sheet, Font redFont) {
        return null;
    }

    private void createShowTableHeader(){
        detailsTable.addColumn(SIFDetails::getSIFNo).setCaption(SIF_NO).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(SIFDetails::getDeviceId).setCaption(DEVICE_ID).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(bean->BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption(DEPARTURE_DATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(SIFDetails::getStatus).setCaption(STATUS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(SIFDetails::getPackedFor).setCaption("Flight No").
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(SIFDetails::getPrograms).setCaption(PROGRAM).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(
                person -> showDownloadBtn(person),
                new ComponentRenderer()
        ).setWidth(80);
    }

    private Button showDownloadBtn(SIFDetails sifDetails){
        Button btn = new Button();
        btn.setSizeFull();
        btn.setStyleName("downloadBtn");
        btn.setIcon(VaadinIcons.DOWNLOAD);
        btn.addClickListener(event -> {showSelectServiceTypeMenu(sifDetails);});
        return btn;
    }

    private void showSelectServiceTypeMenu(SIFDetails sifDetails){
        if(sifDetails.getPrograms() != null && !sifDetails.getPrograms().isEmpty()){
            Window serviceTypeSelectionWindow = new Window();
            serviceTypeSelectionWindow.setCaption("Select Service Type");
            serviceTypeSelectionWindow.setWidth(330,Unit.PIXELS);
            serviceTypeSelectionWindow.setHeight(260,Unit.PIXELS);
            serviceTypeSelectionWindow.center();
            serviceTypeSelectionWindow.setModal(true);
            VerticalLayout windowContent = new VerticalLayout();
            windowContent.setMargin(true);
            ComboBox serviceTypeCombo = new ComboBox("Service Type");
            serviceTypeCombo.setWidth(250,Unit.PIXELS);
            List<String> serviceTypes = Arrays.asList(sifDetails.getPrograms().split(","));
            serviceTypeCombo.setItems(serviceTypes);
            serviceTypeSelectionWindow.setContent(windowContent);
            Button okBtn = new Button("Download SIF Form");
            HorizontalLayout errorLayout = new HorizontalLayout();
            errorLayout.setSizeFull();
            errorLayout.setWidth("250px");
            errorLayout.setStyleName("warning");
            Label specialCareLabel = new Label("Please Select a service type.");
            errorLayout.addComponent(specialCareLabel);
            errorLayout.setVisible(false);
            windowContent.addComponents(serviceTypeCombo,okBtn,errorLayout);
            okBtn.addClickListener((Button.ClickListener) clickEvent-> {
                Object serviceType = serviceTypeCombo.getValue();
                if(serviceType != null && !serviceType.toString().isEmpty()){
                    updateGridBodyMenu(sifDetails,serviceType.toString());
                    serviceTypeSelectionWindow.close();
                }
                else {
                    errorLayout.setVisible(true);
                }
            });
            getUI().getUI().addWindow(serviceTypeSelectionWindow);
        }
        else {
            UserNotification.show("Warning","Service Types are not available.","warning",UI.getCurrent());
        }
    }

    @Override
    public Sheet getWorkbook(Sheet sheet){

        int rowNum = 1;
        for(SIFDetails sifDetails: list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0)
                    .setCellValue(String.valueOf(sifDetails.getSIFNo()));
            row.createCell(1)
                    .setCellValue(sifDetails.getPackedFor());
            row.createCell(2)
                    .setCellValue(sifDetails.getDeviceId());

            Cell packedTime = row.createCell(3);
            Date packTime = sifDetails.getPackedTime();
            if(packTime != null) {
                packedTime.setCellValue(sifDetails.getPackedTime());
                packedTime.setCellStyle(dateCellStyle);
            }
            else packedTime.setCellValue("");

            Cell crewOpenCell = row.createCell(4);
            Date crewOpen = sifDetails.getCrewOpenedTime();
            if(crewOpen != null) {
                crewOpenCell.setCellValue(sifDetails.getCrewOpenedTime());
                crewOpenCell.setCellStyle(dateCellStyle);
            }
            else crewOpenCell.setCellValue("");

            Cell crewClosedCell = row.createCell(5);
            Date crewClose = sifDetails.getCrewClosedTime();
            if(crewClose != null){
                crewClosedCell.setCellValue(sifDetails.getCrewClosedTime());
                crewClosedCell.setCellStyle(dateCellStyle);
            }
            else crewClosedCell.setCellValue("");


            Cell downloaded = row.createCell(6);
            downloaded.setCellValue(sifDetails.getDownloaded());
            downloaded.setCellStyle(dateCellStyle);
        }
        return sheet;
    }
}
