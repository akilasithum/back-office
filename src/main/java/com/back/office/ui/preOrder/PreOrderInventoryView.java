package com.back.office.ui.preOrder;

import com.back.office.entity.ItemDetails;
import com.back.office.entity.PreOrderDetails;
import com.back.office.entity.PreOrderItem;
import com.back.office.ui.salesReports.ReportCommonView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.Sheet;
import org.vaadin.addons.filteringgrid.FilterGrid;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PreOrderInventoryView extends ReportCommonView {

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected ComboBox flightNoCombo;
    protected Grid<PreOrderItem> preOrderItemGrid;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String FLIGHT_NO = "Flight No";

    private final String ITEM_NO = "Item No";
    private final String ITEM_DESC = "Item Desc";
    private final String QTY = "Qty";
    private Map<String, ItemDetails> itemNoNameMap;

    @Override
    protected Sheet getWorkbook(Sheet sheet) {
        return null;
    }

    @Override
    protected void defineStringFields() {
        this.pageHeader = "Pre Order Inventory";
        this.reportExcelHeader = "Pre Order Inventory";
    }

    @Override
    protected void showFilterData() {
        mainTableLayout.setVisible(true);

        String flightNo =  String.valueOf(flightNoCombo.getValue());
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<PreOrderItem> list = connection.getPreOrderItemList(dateFrom, dateTo, flightNo);
        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo)  +
                (!flightNo.equals("null") ? ", Flight No = " + flightNoCombo.getValue().toString() : "");
        filterCriteriaText.setValue(outputStr);
        if(list != null){
            for(PreOrderItem item : list){
                item.setItemDesc(itemNoNameMap.get(item.getItemNo()).getItemName());
            }
            preOrderItemGrid.setItems(list);
        }
        else{
            list = new ArrayList<>();
            preOrderItemGrid.setItems(list);
        }


    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        itemNoNameMap = connection.getItemNoItemDetailsMap();
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
        flightDateFromDateField.setSizeFull();
        flightDateFromDateField.setStyleName("datePickerStyle");
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(today);
        flightDateToDateField.setSizeFull();
        flightDateToDateField.setStyleName("datePickerStyle");
        firstRow.addComponent(flightDateToDateField);

        flightNoCombo = new ComboBox(FLIGHT_NO);
        flightNoCombo.setDescription(FLIGHT_NO);
        flightNoCombo.setItems(connection.getFlightsNoList());
        flightNoCombo.setEmptySelectionAllowed(false);
        flightNoCombo.setSizeFull();
        firstRow.addComponents(flightNoCombo,buttonRow);

        preOrderItemGrid = new Grid<>();
        preOrderItemGrid.setColumnReorderingAllowed(true);
        preOrderItemGrid.setSizeFull();
        tableLayout.addComponent(preOrderItemGrid);
        createShowTableHeader();
    }

    private void createShowTableHeader(){
        preOrderItemGrid.addColumn(PreOrderItem::getItemNo).setCaption(ITEM_NO);
        preOrderItemGrid.addColumn(PreOrderItem::getItemDesc).setCaption(ITEM_DESC);
        preOrderItemGrid.addColumn(PreOrderItem::getQuantity).setCaption(QTY);


    }
}
