package com.back.office.ui.salesReports;

import com.back.office.entity.CCSummaryObj;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class CreditCardSummaryView extends ReportCommonView{

    DateField flightDateFromDateField;
    DateField flightDateToDateField;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String CREDIT_CARD_NO = "Card No";
    private final String AMOUNT = "Amount";
    private final String APPROVED = "Approved";
    private final String DECLINED = "Declined";
    private final String FLIGHT_DATE = "Flight Date";
    private final String FLIGHT_FROM = "Flight From";
    private final String FLIGHT_TO = "Flight To";

    protected Grid<CCSummaryObj> detailsTable;

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
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
        userFormLayout.setWidth("60%");
        createShowTableHeader();
    }

    private void createShowTableHeader(){
        detailsTable.addColumn(CCSummaryObj::getCreditCardNumber).setCaption(CREDIT_CARD_NO);
        detailsTable.addColumn(CCSummaryObj::getAmount).setCaption(AMOUNT);
        detailsTable.addColumn(CCSummaryObj::isApproved).setCaption(APPROVED);
        detailsTable.addColumn(CCSummaryObj::isDeclined).setCaption(DECLINED);
        detailsTable.addColumn(CCSummaryObj::getFlightFrom).setCaption(FLIGHT_FROM);
        detailsTable.addColumn(CCSummaryObj::getFlightTo).setCaption(FLIGHT_TO);
        detailsTable.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption(FLIGHT_DATE);
    }
    
    @Override
    protected Sheet getWorkbook(Sheet sheet) {
        return null;
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "Credit Card Summary";
        this.reportExcelHeader = "Credit Card Summary";
    }

    @Override
    protected void showFilterData() {
        mainTableLayout.setVisible(true);
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<CCSummaryObj> list = connection.getCreditCardSummary(dateFrom,dateTo);

        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo);
        filterCriteriaText.setValue(outputStr);
        detailsTable.setItems(list);
    }
}
