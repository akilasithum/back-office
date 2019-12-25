package com.back.office.ui.salesReports;

import com.back.office.entity.TenderSummaryDisplayObj;
import com.back.office.entity.TenderSummaryObj;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.back.office.utils.BackOfficeUtils.showWithDollarSign;

public class TenderSummaryView extends ReportCommonView {

    DateField flightDateFromDateField;
    DateField flightDateToDateField;
    ComboBox serviceTypeComboBox;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String SERVICE_TYPE = "Service Type";
    private final String SIF_NO = "SIF No";
    private final String FLIGHT_DATE = "Dep Date";
    private final String FLIGHT_FROM = "From";
    private final String FLIGHT_NO = "Flight No";
    private final String FLIGHT_TO = "To";
    private final String GROSS_SALE = "Gross Sales";
    private final String CASH_SALE = "Cash";
    private final String VOUCHER_SALE = "Voucher";
    private final String CC_SALE = "Credit Card";

    protected Grid<TenderSummaryDisplayObj> detailsTable;

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
        flightDateToDateField.setStyleName("datePickerStyle");
        flightDateToDateField.setSizeFull();
        firstRow.addComponent(flightDateToDateField);

        serviceTypeComboBox = new ComboBox(SERVICE_TYPE);
        serviceTypeComboBox.setDescription(SERVICE_TYPE);
        serviceTypeComboBox.setItems("All","Duty Free","Duty Paid","Buy on Board");
        serviceTypeComboBox.setSelectedItem("All");
        serviceTypeComboBox.setEmptySelectionAllowed(false);
        serviceTypeComboBox.setSizeFull();
        firstRow.addComponents(serviceTypeComboBox,buttonRow);

        detailsTable = new Grid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        createShowTableHeader();
    }

    private void createShowTableHeader(){
        detailsTable.addColumn(TenderSummaryDisplayObj::getSifNo).setCaption(SIF_NO);
        detailsTable.addColumn(TenderSummaryDisplayObj::getFlightNo).setCaption(FLIGHT_NO);
        detailsTable.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption(FLIGHT_DATE);
        detailsTable.addColumn(TenderSummaryDisplayObj::getFlightFrom).setCaption(FLIGHT_FROM);
        detailsTable.addColumn(TenderSummaryDisplayObj::getFlightTo).setCaption(FLIGHT_TO);
        detailsTable.addColumn(bean-> showWithDollarSign(bean.getGrossSale())).setCaption(GROSS_SALE);
        detailsTable.addColumn(bean-> showWithDollarSign(bean.getCashSale())).setCaption(CASH_SALE);
        detailsTable.addColumn(bean-> showWithDollarSign(bean.getCreditCardSale())).setCaption(CC_SALE);
        detailsTable.addColumn(bean-> showWithDollarSign(bean.getVoucherSale())).setCaption(VOUCHER_SALE);
        detailsTable.addColumn(TenderSummaryDisplayObj::getServiceType).setCaption(SERVICE_TYPE);
    }

    @Override
    protected Sheet getWorkbook(Sheet sheet) {
        return null;
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "Tender Summary";
        this.reportExcelHeader = "Tender Summary";
    }

    @Override
    protected void showFilterData() {
        mainTableLayout.setVisible(true);
        String serviceType = BackOfficeUtils.getServiceTypeFromServiceType( serviceTypeComboBox.getValue().toString());
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo) + " , " +
                "Service Type = " + serviceType == null ? "All" : serviceType;
        filterCriteriaText.setValue(outputStr);

        List<TenderSummaryObj> list = connection.getTenderSummary(dateFrom,dateTo,
                serviceType);
        Map<String,TenderSummaryDisplayObj> displayObjMap = new HashMap<>();
        float grossAmount = 0;
        for(TenderSummaryObj obj : list){
            if(displayObjMap.containsKey(obj.getServiceType()+obj.getFlightId())){
                TenderSummaryDisplayObj displayObj = displayObjMap.get(obj.getServiceType()+obj.getFlightId());
                grossAmount += obj.getAmount();
                displayObj.setGrossSale(grossAmount);
                if(obj.getPaymentType().equals("Cash CAD")) displayObj.setCashSale(obj.getAmount());
                else if(obj.getPaymentType().equals("Credit Card CAD")) displayObj.setCreditCardSale(obj.getAmount());
                else displayObj.setVoucherSale(obj.getAmount());
                displayObjMap.put(obj.getServiceType()+obj.getFlightId(),displayObj);
            }
            else{
                TenderSummaryDisplayObj displayObj = new TenderSummaryDisplayObj();
                displayObj.setSifNo(obj.getSifNo());
                displayObj.setFlightDate(obj.getFlightDate());
                displayObj.setFlightNo(obj.getFlightNo());
                displayObj.setFlightFrom(obj.getFlightFrom());
                displayObj.setFlightTo(obj.getFlightTo());
                displayObj.setServiceType(obj.getServiceType());
                grossAmount = obj.getAmount();
                displayObj.setGrossSale(grossAmount);
                if(obj.getPaymentType().equals("Cash CAD")) displayObj.setCashSale(obj.getAmount());
                else if(obj.getPaymentType().equals("Credit Card CAD")) displayObj.setCreditCardSale(obj.getAmount());
                else displayObj.setVoucherSale(obj.getAmount());
                displayObjMap.put(obj.getServiceType()+obj.getFlightId(),displayObj);
            }
        }
        detailsTable.setItems(displayObjMap.values());
    }

    @Override
    protected PdfPTable getPdfTable(PdfPTable sheet, Font redFont) {
        return null;
    }
}
