package com.back.office.ui.bondReports;

import com.back.office.entity.SIFDetails;
import com.back.office.ui.salesReports.ReportCommonView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.ui.*;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FlightBondActivityReportView extends ReportCommonView {

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected Grid<SIFDetails> detailsTable;
    List<SIFDetails> list;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String SIF_NO = "SIF No";
    private final String DEVICE_ID = "Device";
    private final String DOWNLOADED = "Downloaded";
    private final String PACKED_FOR =  "Packed For";
    private final String PACKED_TIME = "Packed";
    private final String CREW_OPEN_TIME = "Crew Opened";
    private final String CREW_CLOSE_TIME = "Crew Closed";

    public FlightBondActivityReportView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();
        firstRow.setMargin(Constants.noMargin);
        mainUserInputLayout.addComponent(firstRow);

        Date date = new Date();
        LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        flightDateFromDateField = new DateField(FLIGHT_DATE_FROM);
        flightDateFromDateField.setValue(today);
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(today);
        firstRow.addComponent(flightDateToDateField);

        detailsTable = new Grid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        userFormLayout.setWidth("40%");
        createShowTableHeader();
    }

    private void exportToExcel(){
        String[] arr = {SIF_NO,PACKED_FOR,DEVICE_ID,PACKED_TIME,CREW_OPEN_TIME,CREW_CLOSE_TIME,DOWNLOADED};
        exportToExcel("SIF Details",arr);
    }
    @Override
    protected void defineStringFields() {
        this.pageHeader = "Flight/Bond Activity";
        this.reportExcelHeader = "Flight/Bond Activity";
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
        String[] arr = {SIF_NO,PACKED_FOR,DEVICE_ID,PACKED_TIME,CREW_OPEN_TIME,CREW_CLOSE_TIME,DOWNLOADED};
        optionButtonRow.removeComponent(optionButtonRow.getComponent(1));
        File file = exportToExcel("SIF Details",arr);
        downloadExcelBtn = getDownloadExcelBtn("SIF Details",file);
        optionButtonRow.addComponent(downloadExcelBtn);

    }

    private void createShowTableHeader(){
        detailsTable.addColumn(SIFDetails::getSIFNo).setCaption(SIF_NO);
        detailsTable.addColumn(SIFDetails::getPackedFor).setCaption(PACKED_FOR);
        detailsTable.addColumn(SIFDetails::getDeviceId).setCaption(DEVICE_ID);
        detailsTable.addColumn(SIFDetails::getPackedTime).setCaption(PACKED_TIME);
        detailsTable.addColumn(SIFDetails::getCrewOpenedTime).setCaption(CREW_OPEN_TIME);
        detailsTable.addColumn(SIFDetails::getCrewClosedTime).setCaption(CREW_CLOSE_TIME);
        detailsTable.addColumn(SIFDetails::getDownloaded).setCaption(DOWNLOADED);
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
