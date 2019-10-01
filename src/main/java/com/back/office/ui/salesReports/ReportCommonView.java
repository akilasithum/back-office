package com.back.office.ui.salesReports;

import com.back.office.db.DBConnection;
import com.back.office.framework.OnDemandFileDownloader;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.Constants;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.vaadin.haijian.Exporter;

import java.io.*;

public abstract class ReportCommonView extends UserEntryView implements View {
    protected DBConnection connection;
    protected String pageHeader = "";
    protected String reportExcelHeader = "";
    protected VerticalLayout headerLayout;
    protected VerticalLayout userFormLayout;
    protected VerticalLayout mainTableLayout;
    protected HorizontalLayout tableLayout;
    protected VerticalLayout mainUserInputLayout;
    protected Button searchButton;
    protected HorizontalLayout buttonRow;
    protected Label filterCriteriaText;
    protected Button downloadExcelBtn;
    protected Button printBtn;
    OnDemandFileDownloader onDemandFileDownloader = null;
    OnDemandFileDownloader.OnDemandStreamResource onDemandStreamResource;
    protected HorizontalLayout optionButtonRow;

    protected CellStyle dateCellStyle;
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if(userName == null|| userName.toString().isEmpty()){
            getUI().getNavigator().navigateTo("login");
        }
    }

    public ReportCommonView(){
        super();
        connection = DBConnection.getInstance();
        defineStringFields();
        createMainLayout();
        setSizeFull();
    }

    protected void createMainLayout() {

        //setSpacing(true);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        headerLayout.setMargin(Constants.noMargin);
        addComponent(headerLayout);
        Label h1 = new Label(pageHeader);
        h1.addStyleName("headerText");
        headerLayout.addComponent(h1);

        userFormLayout = new VerticalLayout();
        userFormLayout.setMargin(false);
        addComponent(userFormLayout);
        mainTableLayout = new VerticalLayout();
        addComponent(mainTableLayout);
        mainTableLayout.setMargin(Constants.noMargin);
        tableLayout = new HorizontalLayout();
        tableLayout.setMargin(Constants.noMargin);
        tableLayout.setSizeFull();

        mainUserInputLayout = new VerticalLayout();
        mainUserInputLayout.setMargin(Constants.noMargin);
        mainUserInputLayout.setSizeFull();
        userFormLayout.addComponent(mainUserInputLayout);

        buttonRow = new HorizontalLayout();
        buttonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        buttonRow.setStyleName("searchButton");
        buttonRow.setSpacing(true);
        //userFormLayout.addComponent(buttonRow);

        searchButton = new Button("Search");
        searchButton.addClickListener((Button.ClickListener) clickEvent -> showFilterData());
        buttonRow.addComponent(searchButton);

        optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);
        optionButtonRow.setMargin(false);

        printBtn = new Button();
        printBtn.setIcon(VaadinIcons.PRINT);
        downloadExcelBtn = new Button();
        downloadExcelBtn.setIcon(FontAwesome.FILE_EXCEL_O);
        downloadExcelBtn.setId("DownloadButtonID");
        optionButtonRow.addComponents(printBtn,downloadExcelBtn);
        filterCriteriaText = new Label("");
        filterCriteriaText.addStyleName(ValoTheme.LABEL_H4);
        mainTableLayout.addComponent(filterCriteriaText);
        mainTableLayout.addComponent(optionButtonRow);
        mainTableLayout.addComponent(tableLayout);
        mainTableLayout.setComponentAlignment(optionButtonRow, Alignment.MIDDLE_RIGHT);
        setComponentAlignment(mainTableLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(userFormLayout,Alignment.MIDDLE_LEFT);
        setComponentAlignment(headerLayout,Alignment.MIDDLE_LEFT);
    }

    protected TextField getColumnFilterField() {
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        return filter;
    }

    public File exportToExcel(String sheetName, String[] columns){
        Workbook workbook = new HSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet(sheetName);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

        sheet = getWorkbook(sheet);
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            File oldFile = new File(sheetName+".xls");
            oldFile.delete();
            File file = new File(sheetName+".xls");

            FileOutputStream fileOut = new FileOutputStream(file,true);
            workbook.write(fileOut);

            fileOut.close();
            workbook.close();
            return file;
        }
        catch (Exception e){
            Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);
            return null;
        }
    }

    public Button getDownloadExcelBtn(String sheetName,File file){
        Button dwnButton = new Button();
        dwnButton.setIcon(FontAwesome.FILE_EXCEL_O);
        onDemandStreamResource = new  OnDemandFileDownloader.OnDemandStreamResource()
        {
            @Override
            public String getFilename()
            {
                return  sheetName+".xls";
            }

            @Override
            public InputStream getStream()
            {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        onDemandFileDownloader = new OnDemandFileDownloader(
                onDemandStreamResource);
        onDemandFileDownloader.extend(dwnButton);
        return dwnButton;
    }

    protected abstract Sheet getWorkbook(Sheet sheet);

    protected abstract void defineStringFields();

    protected abstract void showFilterData();

}
