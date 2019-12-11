package com.back.office.ui.flightKitchen;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.ZoneId;
import java.util.*;

import com.back.office.entity.*;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.themes.ValoTheme;

public class FlightScheduleView extends UserEntryView implements View{

    protected Button getScheduleBtn;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected Grid<SIFDetails> flightSheduleDetailGrid;
    protected Button exportToExcel;
    protected Button exportPdf;
    protected DateField fromDateText;
    protected DateField toDateText;
    protected File file=new File("Schedule.xlsx");
    protected FileResource fir=new FileResource(file);
    protected FileDownloader excelFileDownloader = new FileDownloader(fir);
    List<SIFDetails> sifDetailsList;
    protected File fileList=new File("fileListPdf.pdf");
    protected FileResource fileResoce=new FileResource(fileList);
    FileDownloader pdfFileDownloader = new FileDownloader(fileResoce);
    Map<String,Flight> flightList;



    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }


    public FlightScheduleView() {
        super();
        setMargin(Constants.noMargin);
        connection=DBConnection.getInstance();
        createMainLayout();
    }

    public void createMainLayout() {

        createLayout = new VerticalLayout();
        flightList = new HashMap<>();
        List<Flight> flighs = (List<Flight>)connection.getAllValues("com.back.office.entity.Flight");
        flighs.stream().forEach(flight -> {
            flightList.put(flight.getObFlightNo(),flight);
        });
        Label h1=new Label("Daily Flights");

        h1.addStyleName("headerText");
        createLayout.addComponent(h1);
        createLayout.setMargin(false);

        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("70%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");


        getScheduleBtn =new Button("Get Schedule");
        getScheduleBtn.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        exportToExcel=new Button();
        exportToExcel.setIcon(FontAwesome.FILE_EXCEL_O);

        exportPdf=new Button();
        exportPdf.setIcon(VaadinIcons.PRINT);

        fromDateText=new DateField("Date From");
        fromDateText.setDescription("Date From");
        fromDateText.setSizeFull();
        fromDateText.setStyleName("datePickerStyle");
        fromDateText.setRequiredIndicatorVisible(true);

        toDateText=new DateField("Date To");
        toDateText.setDescription("Date To");
        toDateText.setRequiredIndicatorVisible(true);
        toDateText.setStyleName("datePickerStyle");
        toDateText.setSizeFull();

        HorizontalLayout submitBtnLayout=new HorizontalLayout();
        submitBtnLayout.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        submitBtnLayout.setSizeFull();
        submitBtnLayout.setStyleName("searchButton");
        submitBtnLayout.addComponents(getScheduleBtn);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);
        optionButtonRow.setMargin(Constants.noMargin);
        optionButtonRow.addComponents(exportToExcel,exportPdf);

        addComponent(createLayout);
        firstRow.addComponents(fromDateText,toDateText,submitBtnLayout);

        flightSheduleDetailGrid =new Grid();
        createLayout.addComponent(firstRow);
        createLayout.addComponent(optionButtonRow);
        createLayout.setComponentAlignment(optionButtonRow, Alignment.MIDDLE_RIGHT);
        createLayout.addComponent(flightSheduleDetailGrid);

        flightSheduleDetailGrid.setSizeFull();

        flightSheduleDetailGrid.addColumn(SIFDetails::getPackedFor).setCaption("Flight No").setExpandRatio(2);
        flightSheduleDetailGrid.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption("Dep Date").setExpandRatio(2);
        flightSheduleDetailGrid.addColumn(SIFDetails::getFlightFrom).setCaption("From").setExpandRatio(1);
        flightSheduleDetailGrid.addColumn(SIFDetails::getFlightTo).setCaption("To").setExpandRatio(1);
        flightSheduleDetailGrid.addColumn(SIFDetails::getPrograms).setCaption("Services").setExpandRatio(2);
        flightSheduleDetailGrid.addColumn(bean -> BackOfficeUtils.getDateStringFromDateTime(bean.getDownloaded())).setCaption("Start").setExpandRatio(4);
        flightSheduleDetailGrid.addColumn(bean -> BackOfficeUtils.getDateStringFromDateTime(bean.getPackedTime())).setCaption("End").setExpandRatio(4);
        flightSheduleDetailGrid.addColumn(SIFDetails::getTotalBuildTime).setCaption("Total Time").setExpandRatio(1);
        flightSheduleDetailGrid.addColumn(bean -> getStatus(bean)).setCaption("Status").setExpandRatio(2);

        flightSheduleDetailGrid.addItemClickListener(itemClick -> {
            showSealAndCartDetails(itemClick.getItem());
        });
    }

    private String getStatus(SIFDetails sif){
        if(sif.getDownloaded() != null && sif.getPackedTime() != null) return "Completed";
        else if(sif.getDownloaded() != null && sif.getPackedTime() == null) return "Open";
        else return "In Progress";
     }

    private void showSealAndCartDetails(SIFDetails item){
        Window window = new Window("Seal and Cart Details");
        window.setWidth("40%");
        window.setHeight(500,Unit.PIXELS);
        window.center();

        List<SealNo> sealNoList = connection.getSealNumbersFromSifNo(String.valueOf(item.getSIFNo()));
        List<Cart> cartList = connection.getCartNoFromSifNo(String.valueOf(item.getSIFNo()));
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label("Cart Numbers"));
        for(Cart cart : cartList){
            layout.addComponent(new Label("\t * "+cart.getCartNumber()));
        }
        layout.addComponent(new Label(" "));
        layout.addComponent(new Label("Seal Numbers"));
        for(SealNo sealNo : sealNoList){
            layout.addComponent(new Label("\t * "+sealNo.getSealNumber()));
        }
        layout.addComponent(new Label(" "));
        window.setContent(layout);
        getUI().addWindow(window);
    }

    private void downloadPdf(){
        try {


            String[] array = {"Date","Time","ACFT Reg","Type","flight Number","services","base Station"};

            Document document = new Document();

            try
            {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileList));
                document.open();
                document.add(new Paragraph("Flight"));


                PdfPTable table = new PdfPTable(array.length);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);
                table.setSpacingAfter(10f);
                for(int i=0;i<array.length;i++) {
                    PdfPCell cellDetails = new PdfPCell(new Paragraph(array[i]));
                    table.addCell(cellDetails);


                }

                /*for(int k=0;k<sifDetailsList.size();k++) {

                    String s0 = sifDetailsList.get(k).getFlightDateTime().toString();
                    String s1 = sifDetailsList.get(k).getFlightTime();
                    String s2 = sifDetailsList.get(k).getAircraftRegistration();
                    String s3 = sifDetailsList.get(k).getAircraftType();
                    String s4 = sifDetailsList.get(k).getFlightNumber();
                    String s6 = sifDetailsList.get(k).getServices();
                    String s7 = sifDetailsList.get(k).getBaseStation();
                    PdfPCell cellDetails=new PdfPCell(new Paragraph(s0));
                    PdfPCell cellDetail1=new PdfPCell(new Paragraph(s1));
                    PdfPCell cellDetail2=new PdfPCell(new Paragraph(s2));
                    PdfPCell cellDetail3=new PdfPCell(new Paragraph(s3));
                    PdfPCell cellDetail4=new PdfPCell(new Paragraph(s4));
                    PdfPCell cellDetail6=new PdfPCell(new Paragraph(s6));
                    PdfPCell cellDetail7=new PdfPCell(new Paragraph(s7));
                    table.addCell(cellDetails);
                    table.addCell(cellDetail1);
                    table.addCell(cellDetail2);
                    table.addCell(cellDetail3);
                    table.addCell(cellDetail4);
                    table.addCell(cellDetail6);
                    table.addCell(cellDetail7);
                }*/
                document.add(table);

                document.close();
                writer.close();
            } catch (DocumentException e)
            {
                e.printStackTrace();
            } catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            pdfFileDownloader.extend(exportPdf);

        } catch (Exception e) {
            Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

        }
    }

    public void processList() {

        exportToExcel.setVisible(true);
        exportPdf.setVisible(true);
        if(fromDateText.getValue()!=null&&!toDateText.getValue().toString().isEmpty()) {

            //String baseStation = UI.getCurrent().getSession().getAttribute("baseStation").toString();
            sifDetailsList = connection.getSifDetailsForDailyFlights(Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            sifDetailsList.stream().forEach(sif->{
                Flight flight = flightList.get(sif.getPackedFor());
                if(flight != null){
                    sif.setFlightFrom(flight.getObFlightFrom());
                    sif.setFlightTo(flight.getObFlightTo());
                }
            });

            flightSheduleDetailGrid.setItems(sifDetailsList);

            try {
                XSSFWorkbook workbook = new XSSFWorkbook();
                FileOutputStream out = new FileOutputStream(file);

                XSSFSheet Spreadsheet = workbook.createSheet("Schedule");
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                headerFont.setColor(IndexedColors.BLUE.getIndex());
                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);
                headerCellStyle.setWrapText(true);
                headerCellStyle.setShrinkToFit(true);

                String[] array = {"Date","Time","ACFT Reg","Type","flight Number","From","To","services"};
                Row r1 = Spreadsheet.createRow(0);

                for (int k = 0; k < array.length; k++) {

                    Cell c = r1.createCell(k);
                    c.setCellValue(array[k].toString());
                    c.setCellStyle(headerCellStyle);

                }

                /*for (int i = 0; i < sifDetailsList.size(); i++) {
                    Row r = Spreadsheet.createRow(i + 1);

                    Date s1 = sifDetailsList.get(i).getFlightDateTime();
                    String s2 = sifDetailsList.get(i).getFlightTime();
                    String s3 = sifDetailsList.get(i).getAircraftRegistration();
                    String s4 = sifDetailsList.get(i).getAircraftType();
                    String s5 = sifDetailsList.get(i).getFlightNumber();
                    String s6 = sifDetailsList.get(i).getFrom();
                    String s7 = sifDetailsList.get(i).getTo();
                    String s8 = sifDetailsList.get(i).getServices();

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
                }*/

                workbook.write(out);
                out.close();

                workbook.close();

                excelFileDownloader.extend(exportToExcel);


            } catch (Exception e) {
                Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

            }
        }else {
            List<SIFDetails> flightDetailListdatelis=connection.getSifDetails(new Date(),new Date());
            flightSheduleDetailGrid.setItems(flightDetailListdatelis);

            try {
                XSSFWorkbook workbook = new XSSFWorkbook();
                FileOutputStream out = new FileOutputStream(file);

                XSSFSheet Spreadsheet = workbook.createSheet("request");
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                headerFont.setColor(IndexedColors.BLUE.getIndex());
                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);
                headerCellStyle.setWrapText(true);
                //headerCellStyle.setShrinkToFit(true);

                String[] array = {"Flight Date Time","Flight Time","Aircraft Registration","Aircraft Type","Flight Number","Root","Services","Base Station"};
                Row r1 = Spreadsheet.createRow(0);

                for (int k = 0; k < array.length; k++) {

                    Cell c = r1.createCell(k);
                    c.setCellValue(array[k].toString());
                    c.setCellStyle(headerCellStyle);

                }

                /*for (int i = 0; i < flightDetailListdatelis.size(); i++) {
                    Row r = Spreadsheet.createRow(i + 1);

                    String s1 = flightDetailListdatelis.get(i).getFlightDateTime().toString();
                    String s2 = flightDetailListdatelis.get(i).getFlightTime();
                    String s3 = flightDetailListdatelis.get(i).getAircraftRegistration();
                    String s4 = flightDetailListdatelis.get(i).getAircraftType();
                    String s5 = flightDetailListdatelis.get(i).getFlightNumber();
                    String s7 = flightDetailListdatelis.get(i).getServices();
                    String s8 = flightDetailListdatelis.get(i).getBaseStation();


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
                    Cell c6 = r.createCell(6);
                    c6.setCellValue(s7);
                    Cell c7 = r.createCell(7);
                    c7.setCellValue(s8);


                }*/

                workbook.write(out);
                out.close();

                workbook.close();

                excelFileDownloader.extend(exportToExcel);

            } catch (Exception e) {
                Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

            }

        }
        downloadPdf();


    }

    public void clearText() {
        fromDateText.clear();
        toDateText.clear();


    }
}
