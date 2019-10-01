package com.back.office.ui.salesReports;

import com.back.office.entity.FlightAmountSummary;
import com.back.office.entity.FlightPaymentDetails;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class FlightPaymentDetailsView extends ReportCommonView {

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected ComboBox flightNoComboBox;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String FLIGHT_NO = "Flight No";

    private final String NO_OF_FLIGHTS = "No of Flights";
    private final String CASH = "Cash";
    private final String CREDIT_CARD = "Credit Cards";
    private final String VOUCHER = "Vouchers";
    private final String GROSS_SALE = "Gross Sales";
    private final String DISCOUNT = "Discounts";
    private final String NET_SALE = "Net Sales";
    private final String SALES_PER_FLIGHT = "Sales per Flight";

    protected Grid<FlightAmountSummary> detailsTable;

    public FlightPaymentDetailsView(){
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

    @Override
    protected Sheet getWorkbook(Sheet sheet) {
        return null;
    }

    private void createShowTableHeader(){
        NumberFormat formatter = new DecimalFormat("#0.00");
        detailsTable.addColumn(FlightAmountSummary::getFlightNo).setCaption(FLIGHT_NO);
        detailsTable.addColumn(FlightAmountSummary::getNoOfFlights).setCaption(NO_OF_FLIGHTS);
        detailsTable.addColumn(bean-> formatter.format(bean.getCashAmount())).setCaption(CASH);
        detailsTable.addColumn(bean-> formatter.format(bean.getCreditCardAmount())).setCaption(CREDIT_CARD);
        detailsTable.addColumn(bean-> formatter.format(bean.getVoucherAmount())).setCaption(VOUCHER);
        detailsTable.addColumn( bean-> formatter.format(bean.getGrossSale())).setCaption(GROSS_SALE);
        detailsTable.addColumn(bean-> formatter.format(bean.getDiscount())).setCaption(DISCOUNT);
        detailsTable.addColumn(bean-> formatter.format(bean.getNetSale())).setCaption(NET_SALE);
        detailsTable.addColumn(bean -> formatter.format(bean.getNetSale()/bean.getNoOfFlights())).setCaption(SALES_PER_FLIGHT);
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "Flight Sales";
        this.reportExcelHeader = "Flight Sales";
    }

    @Override
    protected void showFilterData() {
        mainTableLayout.setVisible(true);
        String flightNo = flightNoComboBox.getValue() != null ? flightNoComboBox.getValue().toString() : null;
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<FlightPaymentDetails> list = connection.getFlightPaymentDetails(dateFrom,dateTo,
                flightNo);
        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo) + " , " +
                ((flightNo == null || flightNo.isEmpty()) ? "" : " Flight No = " + flightNo);
        filterCriteriaText.setValue(outputStr);
        String flightNoStr = "";
        float creditCardAmount  = 0;
        float cashAmount = 0;
        float voucherAmount = 0;
        float total = 0;
        Date flightDate = null;
        Map<String,FlightAmountSummary> summaries = new HashMap<>();
        for(FlightPaymentDetails details : list){
            String key = details.getFlightNo();
            String paymentMethod = details.getPaymentType();
            if(summaries.containsKey(key)){
                FlightAmountSummary summary = summaries.get(key);
                /*if(paymentMethod.equals("Cash USD")){
                    summary.setCashAmount(details.getAmount());
                }
                else if(paymentMethod.equals("Credit Card USD")){
                    summary.setCreditCardAmount(details.getAmount());
                }
                else{
                    summary.setVoucherAmount(details.getAmount());
                }*/
                Float discount = summary.getDiscount() + details.getDiscount();
                Float netAmount = summary.getNetSale() + details.getDiscount() + details.getAmount();
                Float grossAmount = summary.getGrossSale() + details.getAmount();
                if(paymentMethod.equals("Cash USD")){
                    summary.setCashAmount(summary.getCashAmount() + details.getAmount());
                }
                else if(paymentMethod.equals("Credit Card USD")){
                    summary.setCreditCardAmount(summary.getCreditCardAmount() + details.getAmount());
                }
                else{
                    summary.setVoucherAmount(summary.getVoucherAmount() + details.getAmount());
                }
                summary.setDiscount(discount);
                summary.setNetSale(netAmount);
                summary.setGrossSale(grossAmount);
                summary.setNoOfFlights(summary.getNoOfFlights() + 1);
                summaries.put(key,summary);

            }
            else{
                FlightAmountSummary summary = new FlightAmountSummary();
                summary.setFlightNo(details.getFlightNo());
                if(paymentMethod.equals("Cash USD")){
                    summary.setCashAmount(details.getAmount());
                    summary.setCreditCardAmount(0F);
                    summary.setVoucherAmount(0F);
                }
                else if(paymentMethod.equals("Credit Card USD")){
                    summary.setCreditCardAmount(details.getAmount());
                    summary.setCashAmount(0F);
                    summary.setVoucherAmount(0F);
                }
                else{
                    summary.setVoucherAmount(details.getAmount());
                    summary.setCashAmount(0F);
                    summary.setCreditCardAmount(0F);
                }
                summary.setDiscount(details.getDiscount());
                summary.setGrossSale(details.getAmount());
                summary.setNetSale(details.getAmount() + details.getDiscount());
                summary.setNoOfFlights(1);
                summaries.put(key,summary);
            }


        }
        detailsTable.setItems(summaries.values());
        // if(list != null && !list.isEmpty())
        //showDataInTheTable("1",flightDate,flightNoStr,cashAmount,creditCardAmount,voucherAmount,total,0,total);
    }

    /*private void showDataInTheTable(String id,Date flightDate,String flightNoStr,float cashAmount,
                                    float creditCardAmount,float voucherAmount,float grossSale,float discount,float netSale){
        Item item = container.addItem(id);
        item.getItemProperty(NO_OF_FLIGHTS).setValue(BackOfficeUtils.getDateFromDateTime(flightDate));
        item.getItemProperty(FLIGHT_NO).setValue(flightNoStr);
        item.getItemProperty(CASH).setValue(cashAmount);
        item.getItemProperty(CREDIT_CARD).setValue(creditCardAmount);
        item.getItemProperty(VOUCHER).setValue(voucherAmount);
        item.getItemProperty(GROSS_SALE).setValue(grossSale);
        item.getItemProperty(DISCOUNT).setValue(discount);
        item.getItemProperty(NET_SALE).setValue(netSale);
    }*/
}