package com.back.office.ui.salesReports;

import com.back.office.entity.FlightPaymentDetails;
import com.back.office.entity.Flights;
import com.back.office.entity.Sector;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.Sheet;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightPaymentDetailsView extends ReportCommonView {

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected ComboBox flightNoComboBox;
    protected ComboBox serviceTypeComboBox;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String FLIGHT_NO = "Flight No";
    private final String SERVICE_TYPE = "Service Type";

    private final String FLIGHT_DATE = "Flight Date";
    private final String CASH = "Cash";
    private final String CREDIT_CARD = "Credit Card";
    private final String VOUCHER = "Voucher";
    private final String GROSS_SALE = "Gross Sale";
    private final String DISCOUNT = "Discount";
    private final String NET_SALE = "Net Sale";

    protected Grid<FlightPaymentDetails> detailsTable;

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

        flightNoComboBox = new ComboBox(FLIGHT_NO);
        flightNoComboBox.setDescription(FLIGHT_NO);
        flightNoComboBox.setItems(getFlightNos());
        firstRow.addComponent(flightNoComboBox);

        serviceTypeComboBox = new ComboBox(SERVICE_TYPE);
        serviceTypeComboBox.setDescription(SERVICE_TYPE);
        serviceTypeComboBox.setItems("All","Duty Free","Duty Paid","Buy on Board");
        serviceTypeComboBox.setSelectedItem("All");
        serviceTypeComboBox.setEmptySelectionAllowed(false);
        firstRow.addComponent(serviceTypeComboBox);
        detailsTable = new Grid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        userFormLayout.setWidth("80%");
    }

    @Override
    protected Sheet getWorkbook(Sheet sheet) {
        return null;
    }

    private void createShowTableHeader(){
        /*detailsTable.addColumn(FlightPaymentDetails::getFlightDate).setCaption(FLIGHT_DATE);
        detailsTable.addColumn(FlightPaymentDetails::getAmount).setCaption(CASH);
        detailsTable.addColumn(FlightPaymentDetails::g).setCaption(CREDIT_CARD);
        detailsTable.addColumn(FlightPaymentDetails::getItemId).setCaption(VOUCHER);
        detailsTable.addColumn(FlightPaymentDetails::getQuantity).setCaption(GROSS_SALE);
        detailsTable.addColumn(FlightPaymentDetails::getPrice).setCaption(DISCOUNT);
        detailsTable.addColumn(FlightPaymentDetails::getPrice).setCaption(NET_SALE);*/

    }

    private List<String> getFlightNos(){
        List<Flights> flights = (List<Flights>)connection.getAllValues("com.back.office.entity.Flights");
        List<String> flightNoList  = new ArrayList<>();
        for(Flights flight : flights){
            flightNoList.add(flight.getFlightName());
        }
        return flightNoList;
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "Sales Summary by Flight";
        this.reportExcelHeader = "Sales Summary by Flight";
    }

    @Override
    protected void showFilterData() {
        mainTableLayout.setVisible(true);
        String flightNo = flightNoComboBox.getValue() != null ? flightNoComboBox.getValue().toString() : null;
        String serviceType = BackOfficeUtils.getServiceTypeFromServiceType( serviceTypeComboBox.getValue().toString());
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<FlightPaymentDetails> list = connection.getFlightPaymentDetails(dateFrom,dateTo,
                serviceType,flightNo);
        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo) + " , " +
                "Service Type = " + serviceTypeComboBox.getValue().toString() + ((flightNo == null || flightNo.isEmpty()) ? "" : " Flight No = " + flightNo);
        filterCriteriaText.setValue(outputStr);
        String flightNoStr = "";
        float creditCardAmount  = 0;
        float cashAmount = 0;
        float voucherAmount = 0;
        float total = 0;
        Date flightDate = null;
        for(FlightPaymentDetails details : list){
            if(details.getPaymentType().equalsIgnoreCase("Credit card"))creditCardAmount = details.getAmount();
            else if(details.getPaymentType().equalsIgnoreCase("Cash"))cashAmount = details.getAmount();
            else if(details.getPaymentType().equalsIgnoreCase("Voucher"))voucherAmount = details.getAmount();
            if(details.getFlightNo().equals(flightNoStr)){
                total += details.getAmount();
            }
            else{
                if(flightNoStr != ""){
                    //showDataInTheTable(details.getId(),flightDate,flightNoStr,cashAmount,creditCardAmount,voucherAmount,total,0,total);
                }

                flightNoStr = details.getFlightNo();
                total = details.getAmount();
                flightDate = details.getFlightDate();
            }
        }
       // if(list != null && !list.isEmpty())
        //showDataInTheTable("1",flightDate,flightNoStr,cashAmount,creditCardAmount,voucherAmount,total,0,total);
    }

    /*private void showDataInTheTable(String id,Date flightDate,String flightNoStr,float cashAmount,
                                    float creditCardAmount,float voucherAmount,float grossSale,float discount,float netSale){
        Item item = container.addItem(id);
        item.getItemProperty(FLIGHT_DATE).setValue(BackOfficeUtils.getDateFromDateTime(flightDate));
        item.getItemProperty(FLIGHT_NO).setValue(flightNoStr);
        item.getItemProperty(CASH).setValue(cashAmount);
        item.getItemProperty(CREDIT_CARD).setValue(creditCardAmount);
        item.getItemProperty(VOUCHER).setValue(voucherAmount);
        item.getItemProperty(GROSS_SALE).setValue(grossSale);
        item.getItemProperty(DISCOUNT).setValue(discount);
        item.getItemProperty(NET_SALE).setValue(netSale);
    }*/
}