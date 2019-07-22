package com.back.office.ui.preOrder;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vaadin.addons.filteringgrid.FilterGrid;
import com.back.office.entity.PreOrderDetails;
import com.back.office.entity.PreOrderItem;
import com.back.office.ui.salesReports.ReportCommonView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.server.FileDownloader;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class PreOrders extends ReportCommonView {

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected ComboBox service;
    FilterGrid<PreOrderDetails> detailsTable;
    List<PreOrderDetails> list;
    protected Grid<PreOrderItem> listdata;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String ORDER_TYPE = "Source";

    private final String preOrderId = "Pre Order No";
    private final String PNR = "PNR";
    private final String customerName = "Customer Name";
    private final String serviceType = "Service Type";
    private final String flightNumber = "Flight Number";
    private final String FlightDate = "Flight Date";
    private final String preOrderStatus = "Pre Order Status";
    private final String typeOfOrder = "Source";


    @Override
    protected void defineStringFields() {
        this.pageHeader = "Pre Order";
        this.reportExcelHeader = "Pre Order";

    }

    @Override
    public Sheet getWorkbook(Sheet sheet){

        return null;
    }

    public void fileexcelh(Date fromdateh, Date todateh,String serviceh) {
        List<PreOrderDetails> lista = connection.getPreOrderDetails(fromdateh, todateh, serviceh);

        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            File file = new File(serviceh+" pre Order"+".xlsx");
            FileOutputStream out = new FileOutputStream(file);

            XSSFSheet Spreadsheet = workbook.createSheet("PreOrderDetail");
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.BLUE.getIndex());
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setWrapText(true);
            headerCellStyle.setShrinkToFit(true);

            String[] array1 = { "Pre Order Id","PNR","Customer Name","Service Type","Flight Number","Flight Date","Pre Order Status",
                    "Total Amount","Source" };
            Row r1 = Spreadsheet.createRow(0);

            for (int k = 0; k < array1.length; k++) {

                Cell c = r1.createCell(k);
                c.setCellValue(array1[k].toString());
                c.setCellStyle(headerCellStyle);

            }

            for (int i = 0; i < lista.size(); i++) {
                Row r = Spreadsheet.createRow(i + 1);

                int s1 = lista.get(i).getPreOrderId();
                String s2 = lista.get(i).getPNR();
                String s3 = lista.get(i).getCustomerName();
                String s4 = lista.get(i).getServiceType();
                String s5 = lista.get(i).getFlightNumber();
                Date s6 = lista.get(i).getFlightDate();
                String s7 = lista.get(i).getPreOrderStatus();
                Float s8 = lista.get(i).getTotalAmount();
                String s9 = lista.get(i).getTypeOfOrder();
                Cell c = r.createCell(0);
                c.setCellValue(s1);
                Cell c1 = r.createCell(1);
                c1.setCellValue(s2);
                Cell c2 = r.createCell(2);
                c2.setCellValue(s3);
                Cell c3 = r.createCell(3);
                c3.setCellValue(s4);
                Cell c4 = r.createCell(4);
                c4.setCellValue(s5);
                Cell c5 = r.createCell(5);
                c5.setCellValue(s6);
                Cell c6 = r.createCell(6);
                c6.setCellValue(s7);
                Cell c7 = r.createCell(7);
                c7.setCellValue(s8);
                Cell c8 = r.createCell(8);
                c8.setCellValue(s9);
                Cell c9 = r.createCell(9);


            }

            workbook.write(out);
            out.close();

            com.vaadin.server.FileResource fir = new com.vaadin.server.FileResource(file);
            FileDownloader fid = new FileDownloader(fir);
            fid.extend(downloadExcelBtn);


        } catch (Exception e) {
            Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);


        }
    }

    @Override
    protected void showFilterData() {
        mainTableLayout.setVisible(true);

        String serviceh = BackOfficeUtils.getServicehFromServiceh( service.getValue().toString());
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<PreOrderDetails> list = connection.getPreOrderDetails(dateFrom, dateTo, serviceh);
        String outputStr = "Flight Date From " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To " + BackOfficeUtils.getDateFromDateTime(dateTo) + " , " +
                "Order Type = " + service.getValue().toString();
        filterCriteriaText.setValue(outputStr);
        detailsTable.setItems(list);
        optionButtonRow.removeComponent(optionButtonRow.getComponent(1));
        fileexcelh(dateFrom, dateTo, serviceh);
        optionButtonRow.addComponent(downloadExcelBtn);

    }

    private void selectDetailsh(int datalistid) {

        int idofdata=datalistid;
        List<PreOrderItem> listdataorder = connection.getPreOrderItemDetails(idofdata);

        listdata=new Grid<>();
        listdata.setColumnReorderingAllowed(true);
        listdata.setSizeFull();

        listdata.addColumn(PreOrderItem::getPreOrderItemId).setCaption("Pre Order Item No");
        listdata.addColumn(PreOrderItem::getPreOrderId).setCaption("Ppre Order No");
        listdata.addColumn(PreOrderItem::getCategory).setCaption("Category");
        listdata.addColumn(PreOrderItem::getItemNo).setCaption("Item No");
        listdata.addColumn(PreOrderItem::getQuantity).setCaption("Quantity");
        Window windowdatatable=new Window();
        VerticalLayout windowContent = new VerticalLayout();
        windowContent.setMargin(true);
        windowdatatable.setContent(windowContent);
        Button buttonclose=new Button("Ok");
        buttonclose.addClickListener((Button.ClickListener) clickEvent->closedatawindowh(windowdatatable));

        windowdatatable.setWidth("50%");
        windowContent.addComponent(new Label("Per Order Item"));
        windowdatatable.center();
        windowContent.addComponent(listdata);
        windowContent.addComponent(buttonclose);
        windowContent.setComponentAlignment(buttonclose,Alignment.BOTTOM_CENTER);

        listdata.setItems(listdataorder);

        windowdatatable.addStyleName("mywindowstyle");
        getUI().addWindow(windowdatatable);
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

        service = new ComboBox(ORDER_TYPE);
        service.setDescription(ORDER_TYPE);
        service.setItems("HHC Order","Call Center Order","Web Order","All");
        service.setSelectedItem("All");
        service.setEmptySelectionAllowed(false);
        firstRow.addComponent(service);
        detailsTable = new FilterGrid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        userFormLayout.setWidth("70%");

        detailsTable.addItemClickListener((ItemClickListener<PreOrderDetails>) itemClick -> {
            itemClick.getRowIndex();
            int selectdata=itemClick.getItem().getPreOrderId();
            selectDetailsh(selectdata);

        });
        createShowTableHeader();
    }

    private void closedatawindowh(Window windowdata) {
        windowdata.close();
    }
    private void createShowTableHeader(){
        detailsTable.addColumn(PreOrderDetails::getServiceType).setCaption(serviceType);
        detailsTable.addColumn(PreOrderDetails::getTypeOfOrder).setCaption(typeOfOrder);
        detailsTable.addColumn(PreOrderDetails::getPreOrderId).setCaption(preOrderId);
        detailsTable.addColumn(PreOrderDetails::getPNR).setCaption(PNR);
        detailsTable.addColumn(PreOrderDetails::getCustomerName).setCaption(customerName);
        detailsTable.addColumn(PreOrderDetails::getFlightNumber).setCaption(flightNumber);
        detailsTable.addColumn(PreOrderDetails::getFlightDate).setCaption(FlightDate);
        detailsTable.addColumn(PreOrderDetails::getPreOrderStatus).setCaption(preOrderStatus);

    }

}
