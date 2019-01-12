package com.back.office.ui.salesReports;

import com.back.office.entity.FlightPaymentDetails;
import com.back.office.entity.Flights;
import com.back.office.entity.Sector;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

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
        MarginInfo marginInfo = new MarginInfo(false,false,true,false);
        firstRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(firstRow);

        flightDateFromDateField = new DateField(FLIGHT_DATE_FROM);
        flightDateFromDateField.setValue(new Date());
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(new Date());
        firstRow.addComponent(flightDateToDateField);

        flightNoComboBox = new ComboBox(FLIGHT_NO);
        flightNoComboBox.setInputPrompt(FLIGHT_NO);
        flightNoComboBox.addItems(getFlightNos());
        firstRow.addComponent(flightNoComboBox);

        serviceTypeComboBox = new ComboBox(SERVICE_TYPE);
        serviceTypeComboBox.setInputPrompt(SERVICE_TYPE);
        serviceTypeComboBox.addItem("All");
        serviceTypeComboBox.addItem("Duty Free");
        serviceTypeComboBox.addItem("Duty Paid");
        serviceTypeComboBox.addItem("Buy on Board");
        serviceTypeComboBox.select("All");
        serviceTypeComboBox.setNullSelectionAllowed(false);
        firstRow.addComponent(serviceTypeComboBox);

        userFormLayout.setWidth("80%");
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
    protected IndexedContainer generateContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(FLIGHT_DATE, String.class, null);
        container.addContainerProperty(FLIGHT_NO, String.class, null);
        container.addContainerProperty(CASH, Float.class, null);
        container.addContainerProperty(CREDIT_CARD, Float.class, null);
        container.addContainerProperty(VOUCHER, Float.class, null);
        container.addContainerProperty(GROSS_SALE, Float.class, null);
        container.addContainerProperty(DISCOUNT, Float.class, null);
        container.addContainerProperty(NET_SALE, Float.class, null);
        return container;
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "Sales Summary by Flight";
        this.reportExcelHeader = "Sales Summary by Flight";
    }

    @Override
    protected void showFilterData() {
        mainTableLayout.setVisible(true);
        Container container = detailsTable.getContainerDataSource();
        container.removeAllItems();
        String flightNo = flightNoComboBox.getValue() != null ? flightNoComboBox.getValue().toString() : null;
        String serviceType = BackOfficeUtils.getServiceTypeFromServiceType( serviceTypeComboBox.getValue().toString());
        List<FlightPaymentDetails> list = connection.getFlightPaymentDetails(flightDateFromDateField.getValue(),flightDateToDateField.getValue(),
                serviceType,flightNo);
        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(flightDateFromDateField.getValue()) +
                " , To " + BackOfficeUtils.getDateFromDateTime(flightDateToDateField.getValue()) + " , " +
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
                    showDataInTheTable(container,details.getId(),flightDate,flightNoStr,cashAmount,creditCardAmount,voucherAmount,total,0,total);
                }

                flightNoStr = details.getFlightNo();
                total = details.getAmount();
                flightDate = details.getFlightDate();
            }
        }
        if(list != null && !list.isEmpty())
        showDataInTheTable(container,"1",flightDate,flightNoStr,cashAmount,creditCardAmount,voucherAmount,total,0,total);
    }

    private void showDataInTheTable(Container container,String id,Date flightDate,String flightNoStr,float cashAmount,
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
    }
}