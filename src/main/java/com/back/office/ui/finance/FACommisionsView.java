package com.back.office.ui.finance;

import com.back.office.entity.FACommission;
import com.back.office.entity.FlightAmountSummary;
import com.back.office.entity.TenderSummaryObj;
import com.back.office.ui.salesReports.ReportCommonView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FACommisionsView extends ReportCommonView {

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected ComboBox flightNoComboBox;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String FLIGHT_NO = "Flight No";
    float faCommissionPercentage;
    protected Grid<FACommission> detailsTable;
    NumberFormat formatter = new DecimalFormat("#0.00");

    public FACommisionsView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        faCommissionPercentage = connection.getFACommissionPercentage();
        if(faCommissionPercentage == 0){
            faCommissionPercentage = 7;
            errorLayout.setSizeFull();
            errorLayout.setWidth("500px");
            errorLayout.setStyleName("warning");
            Label specialCareLabel = new Label("FA commission ratio not specified. Considered default ratio as 7%.");
            errorLayout.addComponent(specialCareLabel);
            errorLayout.setVisible(true);
            setComponentAlignment(errorLayout,Alignment.MIDDLE_CENTER);
        }
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
        flightDateFromDateField.setStyleName("datePickerStyle");
        flightDateFromDateField.setSizeFull();
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(today);
        flightDateToDateField.setSizeFull();
        flightDateToDateField.setStyleName("datePickerStyle");
        firstRow.addComponent(flightDateToDateField);

        flightNoComboBox = new ComboBox(FLIGHT_NO);
        flightNoComboBox.setDescription(FLIGHT_NO);
        flightNoComboBox.setItems(connection.getFlightsNoList());
        flightNoComboBox.setSizeFull();
        firstRow.addComponents(flightNoComboBox,buttonRow);

        detailsTable = new Grid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        firstRow.setWidth("60%");
        createShowTableHeader();
    }

    private void createShowTableHeader(){
        detailsTable.addColumn(FACommission::getFlightNo).setCaption(FLIGHT_NO);
        detailsTable.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption("Flight Date");
        detailsTable.addColumn(FACommission::getSector).setCaption("Sector");
        detailsTable.addColumn(FACommission::getSifNo).setCaption("SIF Numbers");
        detailsTable.addColumn(bean-> formatter.format(bean.getTotalSale())).setCaption("Total Sales");
        detailsTable.addColumn( FACommission::getFaCount).setCaption("FA's");
        detailsTable.addColumn(bean -> getCommissionPerAttendant(bean)).setCaption("Comm per FA");
        detailsTable.addColumn(bean -> getTotalCommission(bean)).setCaption("Total Comm");
    }

    private String getCommissionPerAttendant(FACommission commission){
        float commissionFloat = (commission.getTotalSale() * (faCommissionPercentage/100))/commission.getFaCount();
        return formatter.format(commissionFloat);
    }

    private String getTotalCommission(FACommission commission){
        float commissionFloat = (commission.getTotalSale() * (faCommissionPercentage/100));
        return formatter.format(commissionFloat);
    }

    @Override
    protected Sheet getWorkbook(Sheet sheet) {
        return null;
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "FA Commissions";
        this.reportExcelHeader = "FA Commissions";
    }

    private List<FACommission> getList(){
        List<FACommission> commissionList = new ArrayList<>();
        List<String> flights = Arrays.asList("WS 1210","WS 1216","WS 3517","WS 1680","WS 1400","WS 670","WS 676");
        List<Integer> dates = Arrays.asList(1,1,2,2,3,3,3);
        List<String> sales =  Arrays.asList("1340.5","1560.5","1340.6","1100","1202","1236.6","1450");
        List<String> sectors = Arrays.asList("YYZ to LGA","YYZ to LGA","YYZ to YXU","YYC to JFK","YYC to LAS","YYC to YYZ","YYC to YYZ ");
        for(int i = 0 ; i < 7 ;i++){
            FACommission commission = new FACommission();
            commission.setFlightNo(flights.get(i));
            long DAY_IN_MS = 1000 * 60 * 60 * 24;
            commission.setFlightDate(new Date(System.currentTimeMillis() - (dates.get(i) * DAY_IN_MS)));
            commission.setSifNo(123 + i);
            commission.setFaCount(2);
            commission.setTotalSale(Float.parseFloat(sales.get(i)));
            commission.setSector(sectors.get(i));
            commissionList.add(commission);
        }

        return commissionList;
    }

    @Override
    protected void showFilterData() {
        String flightNo =  String.valueOf(flightNoComboBox.getValue());
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo) + " , " +
                (flightNo != null && !flightNo.equalsIgnoreCase("null") ? "Flight No = " +  flightNo : "");
        filterCriteriaText.setValue(outputStr);

        List<FACommission> list = connection.getFACommission(dateFrom,dateTo,
                flightNo);

        detailsTable.setItems(list);
    }

    @Override
    protected PdfPTable getPdfTable(PdfPTable sheet, Font redFont) {
        return null;
    }
}
