package com.back.office.ui.flightKitchen;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.back.office.framework.UserEntryView;
import com.back.office.utils.Constants;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.back.office.db.DBConnection;
import com.back.office.entity.FlightSheduleDetail;
import com.poiji.bind.Poiji;
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
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class FlightShedule extends UserEntryView implements View{

    protected Button flightShedul;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected Button ExportToExcel;
    protected Button print;
    protected Grid<FlightSheduleDetail> flightList;
    protected Button clearButton;
    protected Button exportToExcel;
    protected Button printDetail;
    protected DateField fromDateText;
    protected DateField toDateText;
    protected File file=new File("Schedule.xlsx");
    protected FileResource fir=new FileResource(file);
    protected FileDownloader fid=new FileDownloader(fir);
    Upload uploadButton;
    Button rssFeedBtn;
    protected File fileData;
    List uploadedFlightList;

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }


    public FlightShedule() {
        super();
        createMainLayout();
        connection=DBConnection.getInstance();
    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        createLayout.setMargin(Constants.noMargin);

        Label h1=new Label("Flight Schedule");

        h1.addStyleName(ValoTheme.LABEL_H1);
        createLayout.addComponent(h1);

        HorizontalLayout buttonLayoutSubmit=new HorizontalLayout();
        HorizontalLayout buttonLayoutExportExcel=new HorizontalLayout();
        HorizontalLayout dateText=new HorizontalLayout();
        HorizontalLayout dataLayout=new HorizontalLayout();

        rssFeedBtn = new Button("RSS Feed");



        flightShedul=new Button("Flight Schedule");
        createLayout.addComponent(flightShedul);
        flightShedul.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        clearButton=new Button("Clear");
        buttonLayoutSubmit.addComponent(clearButton);
        clearButton.addClickListener((Button.ClickListener) ClickEvent->
                clearText());

        Button processButton=new Button("Process");
        dataLayout.addComponent(processButton);

        processButton.setVisible(false);
        processButton.addClickListener((Button.ClickListener)ClickEvent->
                processFile());


        exportToExcel=new Button("Export To Excel");
        exportToExcel.setVisible(false);

        printDetail=new Button("Print");
        printDetail.setVisible(false);

        fromDateText=new DateField("Date From");
        fromDateText.setDescription("Date From");
        fromDateText.setRequiredIndicatorVisible(true);

        toDateText=new DateField("Date From");
        toDateText.setDescription("Date From");
        toDateText.setRequiredIndicatorVisible(true);

        dateText.addComponent(fromDateText);
        dateText.addComponent(toDateText);

        buttonLayoutSubmit.addComponent(flightShedul);
        buttonLayoutSubmit.addComponent(clearButton);

        buttonLayoutExportExcel.addComponent(exportToExcel);
        buttonLayoutExportExcel.addComponent(printDetail);

        addComponent(createLayout);

        flightList=new Grid();
        flightList.setSizeFull();
        flightList.setWidth("70%");

        flightList.addColumn(FlightSheduleDetail::getflightDateTime).setCaption("Date");
        flightList.addColumn(FlightSheduleDetail::getflightTime).setCaption("Time");
        flightList.addColumn(FlightSheduleDetail::getaircraftRegistration).setCaption("ACFT Reg");
        flightList.addColumn(FlightSheduleDetail::getaircraftType).setCaption("Type");
        flightList.addColumn(FlightSheduleDetail::getflightNumber).setCaption("Flight Number");
        flightList.addColumn(FlightSheduleDetail::getFrom).setCaption("From");
        flightList.addColumn(FlightSheduleDetail::getTo).setCaption("To");
        flightList.addColumn(FlightSheduleDetail::getservices).setCaption("Services");


        List<String> allowedMimeTypes = new ArrayList<>();
        allowedMimeTypes.add("text/xml");
        allowedMimeTypes.add("application/xls");
        allowedMimeTypes.add("application/vnd.ms-excel");
        allowedMimeTypes.add("application/octet-stream");

        uploadButton = new Upload("",new Upload.Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                try {
                    fileData = File.createTempFile("temp",".xls");
                    return new FileOutputStream(fileData);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });

        uploadButton.addStartedListener((Upload.StartedListener) event -> {

            String contentType = event.getMIMEType();
            boolean allowed = false;
            for (int i = 0; i < allowedMimeTypes.size(); i++) {
                if (contentType.equalsIgnoreCase(allowedMimeTypes.get(i))) {
                    allowed = true;
                    break;
                }
            }
            if (allowed) {
                Notification.show("Upload started: ", Type.HUMANIZED_MESSAGE);
            } else {
                Notification.show("Error", "Not a valid file ", Type.WARNING_MESSAGE);
                uploadButton.interruptUpload();
            }
        });

        uploadButton.addFinishedListener((Upload.FinishedListener) finishedEvent -> {
            flightList.setVisible(true);
            dataList();
            processButton.setVisible(true);

        });

        //createLayout.addComponent(dateText);
        //createLayout.addComponent(buttonLayoutSubmit);
        VerticalLayout btnLayout = new VerticalLayout();
        btnLayout.setMargin(Constants.noMargin);

        HorizontalLayout rssFeedLayout = new HorizontalLayout();
        rssFeedLayout.addComponents(rssFeedBtn,new Label("Last Updated at : " + new Date()));
        btnLayout.addComponents(uploadButton,rssFeedLayout);
        createLayout.addComponents(btnLayout);
        uploadButton.setButtonCaption("Upload Excel");
        createLayout.addComponent(buttonLayoutExportExcel);
        createLayout.addComponent(flightList);
        createLayout.addComponent(dataLayout);
    }

    public void processList() {

        flightList.setVisible(true);

        exportToExcel.setVisible(true);
        printDetail.setVisible(true);

        String[] array = {"Date","Time","ACFT Reg","Type","flight Number","From","To","Services"};
        if(fromDateText.getValue()!=null&&!toDateText.getValue().toString().isEmpty()) {

            List<FlightSheduleDetail> flightDetailListdatelis=connection.getFlightShedule("datethisgre",Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            flightList.setItems(flightDetailListdatelis);
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

                Row r1 = Spreadsheet.createRow(0);

                for (int k = 0; k < array.length; k++) {

                    Cell c = r1.createCell(k);
                    c.setCellValue(array[k]);
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

                fid.extend(exportToExcel);

            } catch (Exception e) {
                Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

            }
        }else {
            List<FlightSheduleDetail> flightDetailListdatelis=connection.getFlightShedule("datethis",new Date(),new Date());
            flightList.setItems(flightDetailListdatelis);

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
                headerCellStyle.setShrinkToFit(true);

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
                fid.extend(exportToExcel);

            } catch (Exception e) {
                Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);
            }
        }
    }

    private void processFile(){
        try {

            for (Object object : uploadedFlightList) {
                connection.insertObjectHBM(object);
            }
            Notification.show("Successfully updated.");
            uploadedFlightList = new ArrayList();
            flightList.setItems(uploadedFlightList);
            fileData.delete();
        }
        catch (Exception e){
            Notification.show("Error", "Something wrong with the input file. Please check the file and upload again ", Type.WARNING_MESSAGE);
            fileData.delete();
        }
    }

    public void dataList() {
            uploadedFlightList = Poiji.fromExcel(fileData, FlightSheduleDetail.class);
            flightList.setItems(uploadedFlightList);
    }

    public void clearText() {
        fromDateText.clear();
        toDateText.clear();


    }
}

