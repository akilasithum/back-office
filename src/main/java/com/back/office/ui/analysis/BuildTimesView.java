package com.back.office.ui.analysis;

import java.io.File;
import java.io.FileOutputStream;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.back.office.entity.BuildTime;
import com.back.office.utils.Constants;
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
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class BuildTimesView extends VerticalLayout implements View{

    protected Button submitList;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected Button ExportToExcel;
    protected Button print;
    protected Grid<com.back.office.entity.BuildTime> buildTimeGrid;
    protected Button clearButton;
    protected Button exportToExcel;
    protected Button printDetail;
    protected DateField fromDateText;
    protected DateField toDateText;
    protected File file=new File("BuildTimes.xlsx");
    protected FileResource fir=new FileResource(file);
    protected FileDownloader fid=new FileDownloader(fir);
    protected TextField listText;
    protected ComboBox flightText;

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }


    public BuildTimesView() {
        connection=DBConnection.getInstance();
        setMargin(Constants.noMargin);
        createMainLayout();
        setStyleName("backColorGrey");
    }

    public void createMainLayout() {

        createLayout=new VerticalLayout();
        createLayout.setMargin(Constants.leftBottomtMargin);

        Label h1=new Label("Build Times");

        h1.addStyleName(ValoTheme.LABEL_H1);
        createLayout.addComponent(h1);

        HorizontalLayout buttonLayoutSubmit=new HorizontalLayout();
        HorizontalLayout buttonLayoutExportExcel=new HorizontalLayout();
        HorizontalLayout dateText=new HorizontalLayout();
        dateText.setMargin(Constants.noMargin);
        HorizontalLayout flightLayout=new HorizontalLayout();


        submitList=new Button("Submit");
        createLayout.addComponent(submitList);
        submitList.addClickListener((Button.ClickListener) ClickEvent->
                processList());

        clearButton=new Button("Clear");
        buttonLayoutSubmit.addComponent(clearButton);
        clearButton.addClickListener((Button.ClickListener) ClickEvent->
                clearText());

        exportToExcel=new Button("Export To Excel");
        exportToExcel.setVisible(false);

        printDetail=new Button("Print");
        printDetail.setVisible(false);

        listText=new TextField("SIF No");
        listText.setDescription("Equipment Name");

        flightText=new ComboBox("Flight Number");
        flightText.setItems(connection.getFlightsNoList());

        fromDateText=new DateField("Last Used Date From");
        fromDateText.setDescription("Last Used Date From");
        fromDateText.setRequiredIndicatorVisible(true);

        toDateText=new DateField("Last Used Date To");
        toDateText.setDescription("Late Used Date To");
        toDateText.setRequiredIndicatorVisible(true);

        dateText.addComponent(fromDateText);
        dateText.addComponent(toDateText);

        buttonLayoutSubmit.addComponent(submitList);

        buttonLayoutSubmit.addComponent(clearButton);
        flightLayout.addComponent(flightText);

        flightLayout.addComponent(listText);

        buttonLayoutExportExcel.addComponent(exportToExcel);
        buttonLayoutExportExcel.addComponent(printDetail);

        addComponent(createLayout);

        buildTimeGrid =new Grid();
        buildTimeGrid.setSizeFull();
        buildTimeGrid.setWidth("70%");

        createLayout.addComponent(dateText);
        createLayout.addComponent(flightLayout);
        createLayout.addComponent(buttonLayoutSubmit);
        createLayout.addComponent(buildTimeGrid);
        createLayout.addComponent(buttonLayoutExportExcel);

       // buildTimeGrid.setVisible(false);

        buildTimeGrid.addColumn(com.back.office.entity.BuildTime::getdeviceId).setCaption("Device Id");
        buildTimeGrid.addColumn(com.back.office.entity.BuildTime::getdownloaded).setCaption("Downloaded");
        buildTimeGrid.addColumn(com.back.office.entity.BuildTime::getpackedFor).setCaption("Packed For");
        buildTimeGrid.addColumn(com.back.office.entity.BuildTime::getpackedTime).setCaption("Packed Time");
        buildTimeGrid.addColumn(com.back.office.entity.BuildTime::getcrewOpenedTime).setCaption("Crew Opened Time");
        buildTimeGrid.addColumn(com.back.office.entity.BuildTime::getcrewClosedTime).setCaption("Crew Closed Time");
        buildTimeGrid.addColumn(com.back.office.entity.BuildTime::getbuildTime).setCaption("Build Time (minutes)");


    }

    public void processList() {


        String sifList=listText.getValue();
        Object flightName=flightText.getValue();

            int baseList=(sifList != null && !sifList.isEmpty()) ? Integer.parseInt(sifList) : 0;

                if(fromDateText.getValue() == null || toDateText.getValue() == null){
                    Notification.show("Enter flight date from and to field values", Notification.Type.WARNING_MESSAGE);
                    return;
                }
                List<BuildTime> flightDetList=connection.getBuildTimeList(flightName,Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),baseList);
                exportToExcel.setVisible(true);
                printDetail.setVisible(true);
                buildTimeGrid.setItems(flightDetList);
                try {
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    FileOutputStream out = new FileOutputStream(file);

                    XSSFSheet Spreadsheet = workbook.createSheet("Equipment");
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerFont.setFontHeightInPoints((short) 12);
                    headerFont.setColor(IndexedColors.BLUE.getIndex());
                    CellStyle headerCellStyle = workbook.createCellStyle();
                    headerCellStyle.setFont(headerFont);
                    headerCellStyle.setWrapText(true);
                    headerCellStyle.setShrinkToFit(true);

                    String[] array = {"Sif No","Device Id","Downloaded","Packed For","Packed Time","Crew Opened Time","Crew Closed Time","Build Time"};
                    Row r1 = Spreadsheet.createRow(0);

                    for (int k = 0; k < array.length; k++) {

                        Cell c = r1.createCell(k);
                        c.setCellValue(array[k]);
                        c.setCellStyle(headerCellStyle);

                    }

                    for (int i = 0; i < flightDetList.size(); i++) {
                        Row r = Spreadsheet.createRow(i + 1);

                        int s1 = flightDetList.get(i).getSIFNo();
                        String s2 = flightDetList.get(i).getdeviceId();
                        String s3 = flightDetList.get(i).getdownloaded().toString();
                        String s4 = flightDetList.get(i).getpackedFor();
                        String s5 = flightDetList.get(i).getpackedTime().toString();
                        String s6 = String.valueOf(flightDetList.get(i).getcrewOpenedTime());
                        String s7 = String.valueOf(flightDetList.get(i).getcrewClosedTime());
                        long flightData=flightDetList.get(i).getbuildTime();

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
                        c7.setCellValue(flightData);
                    }
                    workbook.write(out);
                    out.close();
                    workbook.close();
                    fid.extend(exportToExcel);

                } catch (Exception e) {
                    Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

            }
    }

    public void clearText() {
        fromDateText.clear();
        toDateText.clear();
        listText.clear();
        flightText.clear();


    }
}
