package com.back.office.ui.finance;

import com.back.office.entity.FACommission;
import com.back.office.entity.FlightAmountSummary;
import com.back.office.entity.TenderSummaryObj;
import com.back.office.ui.salesReports.ReportCommonView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
        detailsTable.addColumn(FACommission::getSector).setCaption("Sector");
        detailsTable.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption("Flight Date");
        detailsTable.addColumn(bean-> formatter.format(bean.getTotalSale())).setCaption("Total Sales");
        detailsTable.addColumn(FACommission::getSifNo).setCaption("SIF Numbers");
        detailsTable.addColumn( FACommission::getFaCount).setCaption("No of Flight Attendants");
        detailsTable.addColumn(bean -> getCommissionPerAttendant(bean)).setCaption("Commission per Attendants");
    }

    private String getCommissionPerAttendant(FACommission commission){
        float commissionFloat = (commission.getTotalSale() * (faCommissionPercentage/100))/commission.getFaCount();
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
}
