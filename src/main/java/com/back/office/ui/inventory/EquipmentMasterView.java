package com.back.office.ui.inventory;

import java.io.File;
import java.io.FileOutputStream;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.back.office.entity.EquipmentMasterDetail;
import com.back.office.entity.HHCMaster;
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
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.themes.ValoTheme;

public class EquipmentMasterView extends UserEntryView implements View {

    protected Button submitBrn;
    protected VerticalLayout createLayout;
    protected DBConnection connection;
    protected Grid<EquipmentMasterDetail> equipmentMasterDetailGrid;
    protected Grid<HHCMaster> hhcMasterDetailGrid;
    protected Button clearButton;
    protected Button exportToExcel;
    protected Button printDetail;
    protected DateField fromDateText;
    protected DateField toDateText;
    protected File file = new File("EquipmentMaster.xlsx");
    protected FileResource fir = new FileResource(file);
    protected FileDownloader fid = new FileDownloader(fir);
    protected ComboBox masterType;

    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if (userName == null || userName.toString().isEmpty()) {
            getUI().getNavigator().navigateTo("login");
        }
    }


    public EquipmentMasterView() {
        super();
        createMainLayout();
        connection = DBConnection.getInstance();
        setMargin(false);
    }

    public void createMainLayout() {

        createLayout = new VerticalLayout();
        setMargin(Constants.leftBottomtMargin);
        Label h1 = new Label("HHC and Cart Usage");
        createLayout.setMargin(false);

        h1.addStyleName("headerText");
        createLayout.addComponent(h1);

        HorizontalLayout buttonLayoutSubmit = new HorizontalLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("80%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");

        submitBrn = new Button("Submit");
        createLayout.addComponent(submitBrn);
        submitBrn.addClickListener((Button.ClickListener) ClickEvent ->
                processList());

        clearButton = new Button("Clear");
        clearButton.addClickListener((Button.ClickListener) ClickEvent ->
                clearText());

        exportToExcel = new Button();
        exportToExcel.setIcon(FontAwesome.FILE_EXCEL_O);

        printDetail = new Button();
        printDetail.setIcon(VaadinIcons.PRINT);

        fromDateText = new DateField("Last Used Date From");
        fromDateText.setDescription("Last Used Date From");
        fromDateText.setRequiredIndicatorVisible(true);
        fromDateText.setSizeFull();
        fromDateText.setStyleName("datePickerStyle");

        toDateText = new DateField("Last Used Date To");
        toDateText.setDescription("Late Used Date To");
        toDateText.setRequiredIndicatorVisible(true);
        toDateText.setSizeFull();
        toDateText.setStyleName("datePickerStyle");

        masterType = new ComboBox("Type");
        masterType.setItems(Arrays.asList("Equipment", "HHC"));
        masterType.setValue("Equipment");
        masterType.setSizeFull();
        masterType.setEmptySelectionAllowed(false);

        firstRow.addComponent(fromDateText);
        firstRow.addComponent(toDateText);
        firstRow.addComponent(masterType);

        HorizontalLayout submitBtnLayout=new HorizontalLayout();
        submitBtnLayout.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        submitBtnLayout.setStyleName("searchButton");
        submitBtnLayout.addComponents(submitBrn,clearButton);

        firstRow.addComponent(submitBtnLayout);

        HorizontalLayout optionButtonRow = new HorizontalLayout();
        optionButtonRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        optionButtonRow.setSpacing(true);
        optionButtonRow.setMargin(Constants.noMargin);
        optionButtonRow.addComponents(exportToExcel,printDetail);

        addComponent(createLayout);

        equipmentMasterDetailGrid = new Grid();
        equipmentMasterDetailGrid.setSizeFull();

        equipmentMasterDetailGrid.addColumn(EquipmentMasterDetail::getEquipmentId).setCaption("Equipment ID");
        equipmentMasterDetailGrid.addColumn(EquipmentMasterDetail::getType).setCaption("Type");
        equipmentMasterDetailGrid.addColumn(EquipmentMasterDetail::getStatus).setCaption("Status");
        equipmentMasterDetailGrid.addColumn(EquipmentMasterDetail::getFlightNumber).setCaption("Last Flight");
        equipmentMasterDetailGrid.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getLastUsedDate())).setCaption("Date");

        hhcMasterDetailGrid = new Grid();
        hhcMasterDetailGrid.setSizeFull();

        hhcMasterDetailGrid.addColumn(HHCMaster::getHhcId).setCaption("HHC ID");
        hhcMasterDetailGrid.addColumn(HHCMaster::getType).setCaption("Type");
        hhcMasterDetailGrid.addColumn(HHCMaster::getStatus).setCaption("Status");
        hhcMasterDetailGrid.addColumn(HHCMaster::getFlightNo).setCaption("Last Flight");
        hhcMasterDetailGrid.addColumn(bean -> BackOfficeUtils.getDateStringFromDate(bean.getLastUsedDate())).setCaption("Date");

        createLayout.addComponent(firstRow);
        createLayout.addComponent(buttonLayoutSubmit);
        createLayout.addComponent(optionButtonRow);
        createLayout.setComponentAlignment(optionButtonRow, Alignment.MIDDLE_RIGHT);
        createLayout.addComponent(equipmentMasterDetailGrid);
        createLayout.addComponent(hhcMasterDetailGrid);
        hhcMasterDetailGrid.setVisible(false);
    }

    public void processList() {

        exportToExcel.setVisible(true);
        printDetail.setVisible(true);

        String masterTypeText = String.valueOf(masterType.getValue());

        if (fromDateText.getValue() != null && !fromDateText.getValue().toString().isEmpty() && toDateText.getValue() != null &&
                !toDateText.getValue().toString().isEmpty()) {

            if (masterTypeText != null && masterTypeText.equals("Equipment")) {
                List<com.back.office.entity.EquipmentMasterDetail> flightDetailListdatelis = connection.getEquipmentMasterDetails
                        (Date.from(fromDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                hhcMasterDetailGrid.setVisible(false);
                equipmentMasterDetailGrid.setVisible(true);
                equipmentMasterDetailGrid.setItems(flightDetailListdatelis);

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

                    String[] array = {"Equipment ID", "Type", "Status", "Last Used", "Flight Number", "Last Used Date"};
                    Row r1 = Spreadsheet.createRow(0);

                    for (int k = 0; k < array.length; k++) {

                        Cell c = r1.createCell(k);
                        c.setCellValue(array[k].toString());
                        c.setCellStyle(headerCellStyle);

                    }

                    for (int i = 0; i < flightDetailListdatelis.size(); i++) {
                        Row r = Spreadsheet.createRow(i + 1);

                        String s1 = flightDetailListdatelis.get(i).getEquipmentId();
                        String s2 = flightDetailListdatelis.get(i).getType();
                        String s3 = flightDetailListdatelis.get(i).getStatus();
                        String s5 = flightDetailListdatelis.get(i).getFlightNumber();
                        Date s6 = flightDetailListdatelis.get(i).getLastUsedDate();

                        Cell c = r.createCell(0);
                        c.setCellValue(s1);
                        Cell c1 = r.createCell(1);
                        c1.setCellValue(s2);
                        Cell c2 = r.createCell(2);
                        c2.setCellValue(s3);
                        Cell c4 = r.createCell(4);
                        c4.setCellValue(s5);
                        Cell c5 = r.createCell(5);
                        c5.setCellValue(s6);
                    }
                    workbook.write(out);
                    out.close();
                    workbook.close();
                    fid.extend(exportToExcel);

                } catch (Exception e) {
                    Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);
                }
            }
            else{
                List<HHCMaster> flightDetailListdatelis = connection.getHHCMasterDetails(Date.from(fromDateText.getValue().
                        atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(toDateText.getValue().atStartOfDay(ZoneId.systemDefault()).
                        toInstant()));
                equipmentMasterDetailGrid.setVisible(false);
                hhcMasterDetailGrid.setVisible(true);
                hhcMasterDetailGrid.setItems(flightDetailListdatelis);
            }
        } else {
            Notification.show("Please specify date range");
        }
    }

    public void clearText() {
        fromDateText.clear();
        toDateText.clear();
    }
}

