package com.back.office.ui.flightKitchen;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.back.office.framework.UserEntryView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.back.office.db.DBConnection;
import com.back.office.entity.FlightSheduleDetail;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class FlightScheduleView extends UserEntryView implements View{

    protected Button flightShedul;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected Button ExportToExcel;
    protected Button print;
    protected Grid<FlightSheduleDetail> flightSheduleDetailGrid;
    protected List<FlightSheduleDetail> flightDetList;
    protected Button clearButton;
    protected Button exportToExcel;
    protected Button exportPdf;
    protected DateField fromDateText;
    protected DateField toDateText;
    protected File file=new File("Schedule.xlsx");
    protected FileResource fir=new FileResource(file);
    protected FileDownloader excelFileDownloader = new FileDownloader(fir);
    List<FlightSheduleDetail> flightDetailListdatelis;
    protected File fileList=new File("fileListPdf.pdf");
    protected FileResource fileResoce=new FileResource(fileList);
    FileDownloader pdfFileDownloader = new FileDownloader(fileResoce);



    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }


    public FlightScheduleView() {
        super();
        setMargin(Constants.noMargin);
        createMainLayout();
        connection=DBConnection.getInstance();
    }

    public void createMainLayout() {

        createLayout = new VerticalLayout();

        Label h1=new Label("Daily Flights");

        h1.addStyleName("headerText");
        createLayout.addComponent(h1);
        createLayout.setMargin(false);

        HorizontalLayout buttonLayoutSubmit=new HorizontalLayout();
        HorizontalLayout buttonLayoutExportExcel=new HorizontalLayout();
        HorizontalLayout dateLayout = new HorizontalLayout();
        dateLayout.setMargin(Constants.topMarginInfo);
        buttonLayoutSubmit.setMargin(Constants.noMargin);



        flightShedul=new Button("Get Flight Schedule");
        //createLayout.addComponent(flightShedul);
        flightShedul.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        clearButton=new Button("Clear");
        buttonLayoutSubmit.addComponent(clearButton);
        clearButton.addClickListener((Button.ClickListener) ClickEvent->
                clearText());

        exportToExcel=new Button("Export To");
        exportToExcel.setVisible(false);

        exportPdf=new Button("Print");
        exportPdf.setVisible(false);

        fromDateText=new DateField("Date From");
        fromDateText.setDescription("Date From");
        fromDateText.setRequiredIndicatorVisible(true);

        toDateText=new DateField("Date From");
        toDateText.setDescription("Date From");
        toDateText.setRequiredIndicatorVisible(true);

        dateLayout.addComponent(fromDateText);
        dateLayout.addComponent(toDateText);

        buttonLayoutSubmit.addComponent(flightShedul);
        buttonLayoutSubmit.addComponent(clearButton);

        buttonLayoutExportExcel.addComponent(exportToExcel);
        buttonLayoutExportExcel.addComponent(exportPdf);

        addComponent(createLayout);


        flightSheduleDetailGrid =new Grid();
        createLayout.addComponent(dateLayout);
        createLayout.addComponent(buttonLayoutSubmit);
        createLayout.addComponent(flightSheduleDetailGrid);
        createLayout.addComponent(buttonLayoutExportExcel);

        flightSheduleDetailGrid.setSizeFull();

        flightSheduleDetailGrid.addColumn(FlightSheduleDetail::getflightDateTime).setCaption("Date");
        flightSheduleDetailGrid.addColumn(FlightSheduleDetail::getflightTime).setCaption("Time");
        flightSheduleDetailGrid.addColumn(FlightSheduleDetail::getaircraftRegistration).setCaption("ACFT Reg");
        flightSheduleDetailGrid.addColumn(FlightSheduleDetail::getaircraftType).setCaption("Type");
        flightSheduleDetailGrid.addColumn(FlightSheduleDetail::getflightNumber).setCaption("Flight Number");
        flightSheduleDetailGrid.addColumn(FlightSheduleDetail::getFrom).setCaption("From");
        flightSheduleDetailGrid.addColumn(FlightSheduleDetail::getTo).setCaption("To");
        flightSheduleDetailGrid.addColumn(FlightSheduleDetail::getservices).setCaption("Services");
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

                for(int k=0;k<flightDetailListdatelis.size();k++) {

                    String s0 = flightDetailListdatelis.get(k).getflightDateTime().toString();
                    String s1 = flightDetailListdatelis.get(k).getflightTime();
                    String s2 = flightDetailListdatelis.get(k).getaircraftRegistration();
                    String s3 = flightDetailListdatelis.get(k).getaircraftType();
                    String s4 = flightDetailListdatelis.get(k).getflightNumber();
                    String s6 = flightDetailListdatelis.get(k).getservices();
                    String s7 = flightDetailListdatelis.get(k).getbaseStation();
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
                }
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

        flightSheduleDetailGrid.setVisible(true);

        exportToExcel.setVisible(true);
        exportPdf.setVisible(true);


        if(fromDateText.getValue()!=null&&!toDateText.getValue().toString().isEmpty()) {

            String baseStation = UI.getCurrent().getSession().getAttribute("baseStation").toString();
            flightDetailListdatelis = connection.getFlightShedule(Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),baseStation);
            flightSheduleDetailGrid.setItems(flightDetailListdatelis);

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

                for (int i = 0; i < flightDetailListdatelis.size(); i++) {
                    Row r = Spreadsheet.createRow(i + 1);

                    Date s1 = flightDetailListdatelis.get(i).getflightDateTime();
                    String s2 = flightDetailListdatelis.get(i).getflightTime();
                    String s3 = flightDetailListdatelis.get(i).getaircraftRegistration();
                    String s4 = flightDetailListdatelis.get(i).getaircraftType();
                    String s5 = flightDetailListdatelis.get(i).getflightNumber();
                    String s6 = flightDetailListdatelis.get(i).getFrom();
                    String s7 = flightDetailListdatelis.get(i).getTo();
                    String s8 = flightDetailListdatelis.get(i).getservices();

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
                }

                workbook.write(out);
                out.close();

                workbook.close();

                excelFileDownloader.extend(exportToExcel);


            } catch (Exception e) {
                Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

            }
        }else {
            List<FlightSheduleDetail> flightDetailListdatelis=connection.getFlightShedule("datethis",new Date(),new Date());
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

                for (int i = 0; i < flightDetailListdatelis.size(); i++) {
                    Row r = Spreadsheet.createRow(i + 1);

                    String s1 = flightDetailListdatelis.get(i).getflightDateTime().toString();
                    String s2 = flightDetailListdatelis.get(i).getflightTime();
                    String s3 = flightDetailListdatelis.get(i).getaircraftRegistration();
                    String s4 = flightDetailListdatelis.get(i).getaircraftType();
                    String s5 = flightDetailListdatelis.get(i).getflightNumber();
                    String s7 = flightDetailListdatelis.get(i).getservices();
                    String s8 = flightDetailListdatelis.get(i).getbaseStation();


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


                }

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
