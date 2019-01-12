package com.back.office.ui.salesReports;

import com.back.office.entity.CategorySalesDetails;
import com.back.office.utils.BackOfficeUtils;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategorySalesView extends ReportCommonView {

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected ComboBox serviceTypeComboBox;

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
        MarginInfo marginInfo = new MarginInfo(false,false,true,false);
        firstRow.setMargin(marginInfo);
        mainUserInputLayout.addComponent(firstRow);

        flightDateFromDateField = new DateField(FLIGHT_DATE_FROM);
        flightDateFromDateField.setValue(new Date());
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(new Date());
        firstRow.addComponent(flightDateToDateField);

        serviceTypeComboBox = new ComboBox(SERVICE_TYPE);
        serviceTypeComboBox.setInputPrompt(SERVICE_TYPE);
        serviceTypeComboBox.addItem("Duty Free");
        serviceTypeComboBox.addItem("Duty Paid");
        serviceTypeComboBox.addItem("Buy on Board");
        serviceTypeComboBox.select("Duty Free");
        serviceTypeComboBox.setNullSelectionAllowed(false);
        firstRow.addComponent(serviceTypeComboBox);

        userFormLayout.setWidth("70%");
    }

    @Override
    protected IndexedContainer generateContainer() {
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty(CATEGORY, String.class, null);
        container.addContainerProperty(QUANTITY, String.class, null);
        container.addContainerProperty(QTY_PERCENTAGE, String.class, null);
        container.addContainerProperty(RETAIL_AMOUNT, String.class, null);
        container.addContainerProperty(RETAIL_AMOUNT_PERCENTAGE, String.class, null);
        container.addContainerProperty(DISCOUNT, Float.class, null);
        container.addContainerProperty(NET_AMOUNT, String.class, null);
        container.addContainerProperty(NET_AMOUNT_PERCENTAGE, String.class, null);
        return container;
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "Sales by Category";
        this.reportExcelHeader = "Sales by Category";
    }

    @Override
    protected void showFilterData() {
        mainTableLayout.setVisible(true);
        Container container = detailsTable.getContainerDataSource();
        container.removeAllItems();
        String serviceType = BackOfficeUtils.getServiceTypeFromServiceType( serviceTypeComboBox.getValue().toString());
        List<CategorySalesDetails> list = connection.getCategorySalesDetails(flightDateFromDateField.getValue(),flightDateToDateField.getValue(),
                serviceType);
        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(flightDateFromDateField.getValue()) +
                " , To " + BackOfficeUtils.getDateFromDateTime(flightDateToDateField.getValue()) + " , " +
                "Service Type = " + serviceTypeComboBox.getValue().toString();
        filterCriteriaText.setValue(outputStr);
        float totalQty = 0;
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
        df.setMaximumFractionDigits(2);
        for(Map.Entry<String, Float> map : catQuantityMap.entrySet()){

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
        item.getItemProperty(NET_AMOUNT_PERCENTAGE).setValue("100");

        /*for(CategorySalesDetails details : list){
            float qty = Float.parseFloat(String.valueOf(details.getQuantity()));
            float qtyPercentage = (qty/totalQty)*100;
            float totalPercentage = (details.getPrice()/totalPrice)*100;
            Item item = container.addItem(details.getId());
            item.getItemProperty(CATEGORY).setValue(details.getCategory());
            item.getItemProperty(QUANTITY).setValue(details.getQuantity());
            item.getItemProperty(QTY_PERCENTAGE).setValue(qtyPercentage);
            item.getItemProperty(RETAIL_AMOUNT).setValue(details.getPrice());
            item.getItemProperty(RETAIL_AMOUNT_PERCENTAGE).setValue(totalPercentage);
            item.getItemProperty(DISCOUNT).setValue(0.0f);
            item.getItemProperty(NET_AMOUNT).setValue(details.getPrice());
            item.getItemProperty(NET_AMOUNT_PERCENTAGE).setValue(totalPercentage);
        }*/
    }
}
