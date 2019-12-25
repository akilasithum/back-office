package com.back.office.ui.salesReports;

import com.back.office.db.DBConnection;
import com.back.office.entity.SIFDetails;
import com.back.office.framework.OnDemandFileDownloader;
import com.back.office.framework.UserEntryView;
import com.back.office.utils.Constants;
import com.back.office.utils.DownloadHelper;
import com.back.office.utils.DownloadHelperImpl;
import com.itextpdf.text.Document;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Font;

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
    protected HorizontalLayout errorLayout;
    protected HorizontalLayout additionalBtnLayout;
    protected String[] excelColumnArr;
    protected float[] pdfTableWithArr;


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
        additionalBtnLayout = new HorizontalLayout();
        additionalBtnLayout.setMargin(false);
        addComponent(additionalBtnLayout);

        userFormLayout = new VerticalLayout();
        userFormLayout.setMargin(false);
        addComponent(userFormLayout);
        errorLayout = new HorizontalLayout();
        addComponent(errorLayout);
        errorLayout.setVisible(false);
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

        downloadExcelBtn.addClickListener(event -> {
            exportToExcel();
        });
        printBtn.addClickListener(event -> {
           exportToPDF();
        });

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

    public void exportToExcel(){
        Workbook workbook = new HSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet(reportExcelHeader);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.BLUE.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);
        for(int i = 0; i < excelColumnArr.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(excelColumnArr[i]);
            cell.setCellStyle(headerCellStyle);
        }

        dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

        sheet = getWorkbook(sheet);
        for(int i = 0; i < excelColumnArr.length; i++) {
            sheet.autoSizeColumn(i);
        }
        downloadFile(workbook);
    }

    private void downloadFile(Workbook workbook){
        DownloadHelper helper = new DownloadHelperImpl();
        helper.createExcelFile(new DownloadHelper.DownloadServiceListener() {
            @Override
            public void onComplete(String path) {
                if (path != null && !path.equals("")) {
                    try {
                        File file = new File(path);
                        StreamResource resource = getExistingFile(reportExcelHeader.
                                replace(" ","_")+".xls", path);
                        getUI().getPage().open(resource, "_blank", false);
                        file.deleteOnExit();
                    } catch (Exception e) {
                        Notification.show("Something went wrong", Notification.Type.WARNING_MESSAGE);
                    }
                }
            }
            @Override
            public void onFail() {

            }
        },workbook);
    }

    private void downloadPdfFile(PdfPTable table){
        DownloadHelper helper = new DownloadHelperImpl();
        helper.createPDFFile(new DownloadHelper.DownloadServiceListener() {
            @Override
            public void onComplete(String path) {
                if (path != null && !path.equals("")) {
                    try {
                        File file = new File(path);
                        StreamResource resource = getExistingFile(reportExcelHeader.
                                replace(" ","_")+".pdf", path);
                        getUI().getPage().open(resource, "_blank", false);
                        file.deleteOnExit();
                    } catch (Exception e) {
                        Notification.show("Something went wrong", Notification.Type.WARNING_MESSAGE);
                    }
                }
            }
            @Override
            public void onFail() {

            }
        },table,reportExcelHeader);
    }

    public File exportToPDF(String sheetName, String[] columns,float[] widthArr){
        com.itextpdf.text.Font redFont = FontFactory.getFont(FontFactory.TIMES, 10);
        com.itextpdf.text.Font greenFont = FontFactory.getFont(FontFactory.TIMES, 12);
        com.itextpdf.text.Font yellowFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, com.itextpdf.text.Font.BOLD);

        try {
            Document document = new Document();
            /*File oldFile = new File(sheetName+".pdf");
            oldFile.delete();*/
            File pdfFile = File.createTempFile("tmp","pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.setPageSize(PageSize.A4);
            Paragraph header = new Paragraph(sheetName, yellowFont);
            header.setAlignment(1);
            document.add(header);
            document.add(Chunk.NEWLINE);

            PdfPTable itemTable = new PdfPTable(widthArr);
            itemTable.setWidthPercentage(100);
            for(int i = 0 ; i< widthArr.length;i++){
                itemTable.addCell(new Paragraph(columns[i],greenFont));
            }

            itemTable = getPdfTable(itemTable,redFont);

            document.add(itemTable);
            document.add( Chunk.NEWLINE );
            document.close();
            writer.close();
            return pdfFile;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void exportToPDF(){
        com.itextpdf.text.Font redFont = FontFactory.getFont(FontFactory.TIMES, 10);
        com.itextpdf.text.Font greenFont = FontFactory.getFont(FontFactory.TIMES, 12);
        try {

            PdfPTable itemTable = new PdfPTable(pdfTableWithArr);
            itemTable.setWidthPercentage(100);
            for(int i = 0 ; i< pdfTableWithArr.length;i++){
                itemTable.addCell(new Paragraph(excelColumnArr[i],greenFont));
            }
            itemTable = getPdfTable(itemTable,redFont);
            downloadPdfFile(itemTable);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public StreamResource getExistingFile(String destinationFileName, String sourceFilePath) {
        if (sourceFilePath == null || sourceFilePath.equals("")) return null;
        File file = new File(sourceFilePath);
        String filename = file.getName();
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        StreamResource resource = new StreamResource(new StreamResource.StreamSource() {
            private static final long serialVersionUID = 1L;
            @Override
            public InputStream getStream() {
                try {
                    return new FileInputStream(new File(sourceFilePath));
                } catch (FileNotFoundException e) {
                    return null;
                }
            }
        }, (destinationFileName));
        resource.setMIMEType("application/" + fileType);
        resource.getStream().setParameter("Content-Disposition", "attachment; filename=" + (destinationFileName));
        return resource;
    }

    protected abstract Sheet getWorkbook(Sheet sheet);

    protected abstract void defineStringFields();

    protected abstract void showFilterData();

    protected abstract PdfPTable getPdfTable(PdfPTable sheet,com.itextpdf.text.Font redFont);
}
