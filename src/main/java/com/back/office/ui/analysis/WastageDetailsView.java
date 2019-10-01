package com.back.office.ui.analysis;

import java.io.File;
import java.io.FileOutputStream;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.back.office.db.DBConnection;
import com.back.office.entity.WastageDetail;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.themes.ValoTheme;

public class WastageDetailsView extends UserEntryView implements View{

    protected Button submitList;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected Grid<WastageDetail> wastageDetailGrid;
    protected Button clearButton;
    protected Button exportToExcel;
    protected Button printDetail;
    protected DateField fromDateText;
    protected DateField toDateText;
    protected File file=new File("Wastage.xlsx");
    protected FileResource fir=new FileResource(file);
    protected FileDownloader fid=new FileDownloader(fir);
    protected TextField sifNumberFld;
    protected ComboBox flightText;

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }


    public WastageDetailsView() {
        super();
        connection=DBConnection.getInstance();
        createMainLayout();
        setMargin(Constants.noMargin);
    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        createLayout.setMargin(Constants.leftBottomtMargin);

        Label h1=new Label("Wastage");

        h1.addStyleName("headerText");
        createLayout.addComponent(h1);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("80%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");

        flightText=new ComboBox("Flight Number");
        flightText.setDescription("Flight Number");
        flightText.setSizeFull();
        flightText.setItems(connection.getFlightsNoList());

        submitList=new Button("Submit");
        createLayout.addComponent(submitList);
        submitList.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        clearButton=new Button("Clear");
        clearButton.addClickListener((Button.ClickListener) ClickEvent->
                clearText());

        exportToExcel=new Button();
        exportToExcel.setIcon(FontAwesome.FILE_EXCEL_O);

        printDetail=new Button();
        printDetail.setIcon(VaadinIcons.PRINT);

        sifNumberFld =new TextField("Sif Number");
        sifNumberFld.setDescription("Sif Number");
        sifNumberFld.setSizeFull();

        fromDateText=new DateField("From");
        fromDateText.setDescription("From");
        fromDateText.setSizeFull();
        fromDateText.setRequiredIndicatorVisible(true);

        toDateText=new DateField("To");
        toDateText.setDescription("To");
        toDateText.setSizeFull();
        toDateText.setRequiredIndicatorVisible(true);

        firstRow.addComponent(fromDateText);
        firstRow.addComponent(toDateText);

        firstRow.addComponent(sifNumberFld);
        firstRow.addComponent(flightText);

        HorizontalLayout submitBtnLayout=new HorizontalLayout();
        submitBtnLayout.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        submitBtnLayout.setStyleName("searchButton");
        submitBtnLayout.addComponents(submitList,clearButton);
        firstRow.addComponent(submitBtnLayout);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setMargin(Constants.noMargin);
        optionButtonRow.addComponents(exportToExcel,printDetail);

        addComponent(createLayout);

        wastageDetailGrid =new Grid();
        wastageDetailGrid.setSizeFull();

        createLayout.addComponent(firstRow);
        createLayout.addComponent(optionButtonRow);
        createLayout.addComponent(wastageDetailGrid);
        createLayout.setComponentAlignment(optionButtonRow, Alignment.MIDDLE_RIGHT);

        wastageDetailGrid.addColumn(WastageDetail::getitemId).setCaption("Item No");
        wastageDetailGrid.addColumn(WastageDetail::getquantity).setCaption("Quantity");
        wastageDetailGrid.addColumn(WastageDetail::getcartNo).setCaption("Cart No");
        wastageDetailGrid.addColumn(WastageDetail::getdrawer).setCaption("Drawer");
        wastageDetailGrid.addColumn(WastageDetail::getflightNo).setCaption("Flight No");
        wastageDetailGrid.addColumn(WastageDetail::getsifNo).setCaption("SIF No");
        wastageDetailGrid.addColumn(bean-> BackOfficeUtils.getDateStringFromDate(bean.getflightDate())).setCaption("Flight Date");
    }

    public void processList() {



        Object sifList= sifNumberFld.getValue();
        Object flightList=flightText.getValue();
        Object fromDate=fromDateText.getValue();
        Object toDate=toDateText.getValue();

        if(fromDateText.getValue()!=null&&!fromDateText.getValue().toString().isEmpty()&toDateText.getValue()!=null&&!toDateText.getValue().toString().isEmpty()) {

            List<WastageDetail> flightDetailListdatelis=connection.getWastage(flightList,sifList,fromDate,toDate,Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            wastageDetailGrid.setItems(flightDetailListdatelis);
            exportToExcel.setVisible(true);
            printDetail.setVisible(true);

            try {
                XSSFWorkbook workbook = new XSSFWorkbook();
                FileOutputStream out = new FileOutputStream(file);

                XSSFSheet Spreadsheet = workbook.createSheet("Wastage");
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                headerFont.setColor(IndexedColors.BLUE.getIndex());
                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);
                headerCellStyle.setWrapText(true);
                headerCellStyle.setShrinkToFit(true);

                String[] array = {"Item Id","Quantity","Cart No","Drawer","Flight No","SIF No","Flight Date"};
                Row r1 = Spreadsheet.createRow(0);

                for (int k = 0; k < array.length; k++) {

                    Cell c = r1.createCell(k);
                    c.setCellValue(array[k].toString());
                    c.setCellStyle(headerCellStyle);

                }

                for (int i = 0; i < flightDetailListdatelis.size(); i++) {
                    Row r = Spreadsheet.createRow(i + 1);

                    String s1 = flightDetailListdatelis.get(i).getitemId();
                    int s2 = flightDetailListdatelis.get(i).getquantity();
                    String s3 = flightDetailListdatelis.get(i).getcartNo();
                    String s4 = flightDetailListdatelis.get(i).getdrawer();
                    String s5 = flightDetailListdatelis.get(i).getflightNo();
                    String s6 = flightDetailListdatelis.get(i).getsifNo();
                    String s7 = flightDetailListdatelis.get(i).getflightDate().toString();



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
                }

                workbook.write(out);
                out.close();

                workbook.close();

                fid.extend(exportToExcel);

            } catch (Exception e) {
                Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

            }
        }
        else  {

            Notification.show("Please specify flight date range filter", Notification.Type.WARNING_MESSAGE);
        }




    }

    public void clearText() {
        fromDateText.clear();
        toDateText.clear();
        sifNumberFld.clear();
        flightText.clear();


    }
}

