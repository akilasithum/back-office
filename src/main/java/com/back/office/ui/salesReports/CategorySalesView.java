package com.back.office.ui.salesReports;

import com.back.office.entity.CategorySalesDetails;
import com.back.office.entity.SalesByCategoryObj;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.Sheet;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class CategorySalesView extends ReportCommonView {

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected ComboBox serviceTypeComboBox;
    protected Grid<SalesByCategoryObj> detailsTable;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String SERVICE_TYPE = "Service Type";

    private final String CATEGORY = "Category";
    private final String QUANTITY = "Quantity";
    private final String QTY_PERCENTAGE = "QTY %";
    private final String RETAIL_AMOUNT = "Amount (Retail)";
    private final String RETAIL_AMOUNT_PERCENTAGE = "Retail Amount %";
    private final String DISCOUNT = "Discount";
    private final String NET_AMOUNT = "Net Amount";
    private final String NET_AMOUNT_PERCENTAGE = "Net Amount %";

    public CategorySalesView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("80%");
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

        serviceTypeComboBox = new ComboBox(SERVICE_TYPE);
        serviceTypeComboBox.setDescription(SERVICE_TYPE);
        serviceTypeComboBox.setItems("Duty Free","Duty Paid","Buy on Board");
        serviceTypeComboBox.setSelectedItem("Duty Free");
        serviceTypeComboBox.setEmptySelectionAllowed(false);
        serviceTypeComboBox.setSizeFull();
        firstRow.addComponents(serviceTypeComboBox,buttonRow);
        detailsTable = new Grid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        userFormLayout.setWidth("70%");
        createShowTableHeader();
    }

    @Override
    protected Sheet getWorkbook(Sheet sheet) {
        return null;
    }

    private void createShowTableHeader(){
        detailsTable.addColumn(SalesByCategoryObj::getCategory).setCaption(CATEGORY);
        detailsTable.addColumn(SalesByCategoryObj::getQuantity).setCaption(QUANTITY);
        detailsTable.addColumn(SalesByCategoryObj::getQtyPercentage).setCaption(QTY_PERCENTAGE);
        //detailsTable.addColumn(SalesByCategoryObj::getRetailAmount).setCaption(RETAIL_AMOUNT);
        //detailsTable.addColumn(SalesByCategoryObj::getRetailAmountPercentage).setCaption(RETAIL_AMOUNT_PERCENTAGE);
       // detailsTable.addColumn(SalesByCategoryObj::getDiscount).setCaption(DISCOUNT);
        detailsTable.addColumn(SalesByCategoryObj::getNetAmount).setCaption(NET_AMOUNT);
        detailsTable.addColumn(SalesByCategoryObj::getNetAmountPercentage).setCaption(NET_AMOUNT_PERCENTAGE);
    }
    @Override
    protected void defineStringFields() {
        this.pageHeader = "Sales by Category";
        this.reportExcelHeader = "Sales by Category";
    }

    @Override
    protected void showFilterData() {
        mainTableLayout.setVisible(true);

        String serviceType = BackOfficeUtils.getServiceTypeFromServiceType( serviceTypeComboBox.getValue().toString());
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<Object[]> payments = connection.getCategorySalesDetails(dateFrom,dateTo,
                serviceType);
        List<SalesByCategoryObj> salesByCategoryObjList = new ArrayList<>();

        int qtyTotal = 0;
        float amountTotal = 0;
        for (Object[] payment : payments) {
            qtyTotal += (Integer) payment[0];
            amountTotal += (Float) payment[1];
        }
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        for (Object[] payment : payments) {
            SalesByCategoryObj obj = new SalesByCategoryObj();
            int qty = (Integer) payment[0];
            obj.setQuantity(qty);
            float amount = (Float) payment[1];
            obj.setNetAmount(amount);
            String category = (String)payment[2];
            obj.setCategory(category);
            salesByCategoryObjList.add(obj);
            float qtyPercentage = (Float.valueOf(String.valueOf(qty))/qtyTotal)*100;
            obj.setQtyPercentage(df.format(qtyPercentage));
            float totalPercentage = (amount/amountTotal)*100;
            obj.setNetAmountPercentage(df.format(totalPercentage));
        }
        if(salesByCategoryObjList.size()>0) {
            SalesByCategoryObj totalObj = new SalesByCategoryObj();
            totalObj.setCategory("Total");
            totalObj.setNetAmount(amountTotal);
            totalObj.setNetAmountPercentage("100");
            totalObj.setQtyPercentage("100");
            totalObj.setQuantity(qtyTotal);
            salesByCategoryObjList.add(totalObj);
        }

        detailsTable.setItems(salesByCategoryObjList);
        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo) + " , " +
                "Service Type = " + serviceTypeComboBox.getValue().toString();
        filterCriteriaText.setValue(outputStr);
        /*float totalQty = 0;
        int totalPrice = 0;
        Map<String,Float> catQuantityMap = new HashMap<>();
        Map<String,Float> catPriceMap = new HashMap<>();
        for(CategorySalesDetails details : list){
            if(catQuantityMap.containsKey(details.getCategory())){
                catQuantityMap.put(details.getCategory(),catQuantityMap.get(details.getCategory()) + details.getQuantity());
                catPriceMap.put(details.getCategory(),catPriceMap.get(details.getCategory()) + details.getPrice());

            }
            else{
                catQuantityMap.put(details.getCategory(),Float.valueOf(String.valueOf(details.getQuantity())));
                catPriceMap.put(details.getCategory(),details.getPrice());
            }
            totalQty += details.getQuantity();
            totalPrice += details.getPrice();
        }
        int i = 1;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);*/
        /*for(Map.Entry<String, Float> map : catQuantityMap.entrySet()){

            float qtyPercentage = (map.getValue()/totalQty)*100;
            float totalPercentage = (catPriceMap.get(map.getKey())/totalPrice)*100;

            Item item = container.addItem(i);
            item.getItemProperty(CATEGORY).setValue(map.getKey());
            item.getItemProperty(QUANTITY).setValue(df.format(map.getValue()));
            item.getItemProperty(QTY_PERCENTAGE).setValue(df.format(qtyPercentage));
            item.getItemProperty(RETAIL_AMOUNT).setValue(df.format(catPriceMap.get(map.getKey())));
            item.getItemProperty(RETAIL_AMOUNT_PERCENTAGE).setValue(df.format(totalPercentage));
            item.getItemProperty(DISCOUNT).setValue(0.0f);
            item.getItemProperty(NET_AMOUNT).setValue(df.format(catPriceMap.get(map.getKey())));
            item.getItemProperty(NET_AMOUNT_PERCENTAGE).setValue(df.format(totalPercentage));
            i++;
        }
        Item item = container.addItem(i);
        item.getItemProperty(CATEGORY).setValue("Total");
        item.getItemProperty(QUANTITY).setValue(df.format(totalQty));
        item.getItemProperty(QTY_PERCENTAGE).setValue("100");
        item.getItemProperty(RETAIL_AMOUNT).setValue(df.format(totalPrice));
        item.getItemProperty(RETAIL_AMOUNT_PERCENTAGE).setValue("100");
        item.getItemProperty(DISCOUNT).setValue(0.0f);
        item.getItemProperty(NET_AMOUNT).setValue(df.format(totalPrice));
        item.getItemProperty(NET_AMOUNT_PERCENTAGE).setValue("100");*/


    }

    @Override
    protected PdfPTable getPdfTable(PdfPTable sheet, Font redFont) {
        return null;
    }
}
