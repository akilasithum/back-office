package com.back.office.ui.download;

import com.back.office.db.DBConnection;
import com.back.office.entity.AircraftDetails;
import com.back.office.entity.BlackListCC;
import com.back.office.entity.CurrencyDetails;
import com.back.office.entity.EquipmentDetails;
import com.back.office.entity.ItemDetails;
import com.back.office.entity.KitCodes;
import com.back.office.entity.Voucher;
import com.back.office.utils.Constants;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.List;


public class DownloadView<T> extends VerticalLayout implements View {
    protected DBConnection connection;
    protected String pageHeader = "Download files";
    protected VerticalLayout headerLayout;
    protected VerticalLayout userFormLayout;
    protected HorizontalLayout optionButtonRow;
    protected ComboBox downloadTypeComboBox;
    protected String comboboxd;
    protected String comboboxvalue;
    protected Button downloadbuttonh;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        Object userName = UI.getCurrent().getSession().getAttribute("userName");
        if (userName == null || userName.toString().isEmpty()) {
            getUI().getNavigator().navigateTo("login");
        }
    }

    public DownloadView() {
        connection = DBConnection.getInstance();
        setMargin(true);
        createMainLayout();
        processFile();

    }

    protected void createMainLayout() {

        setSpacing(true);
        headerLayout = new VerticalLayout();
        headerLayout.setSizeFull();
        headerLayout.setMargin(Constants.noMargin);
        addComponent(headerLayout);
        Label h1 = new Label("Download files");
        h1.addStyleName("headerText");
        headerLayout.addComponent(h1);

        userFormLayout = new VerticalLayout();
        userFormLayout.setSpacing(true);
        userFormLayout.setMargin(Constants.noMargin);
        addComponent(userFormLayout);

        downloadTypeComboBox = new ComboBox("Entity Type");
        downloadTypeComboBox.setDescription("Entity Type");
        downloadTypeComboBox.setItems("Aircraft Type", "Currency", "Create Items", "Create Kit Codes",
                "Equipment Types", "CC Black List", "Vouchers");
        downloadTypeComboBox.setEmptySelectionAllowed(false);
        downloadTypeComboBox.addSelectionListener(SelectionListener -> processFile());
        downloadTypeComboBox.setRequiredIndicatorVisible(true);

        downloadbuttonh = new Button("Download");
        userFormLayout.addComponent(downloadTypeComboBox);
        userFormLayout.addComponent(downloadbuttonh);

    }

    protected void processFile() {
//		downloadTypeComboBox.addSelectionListener(SelectionListener->processFile());
        comboboxd = String.valueOf(downloadTypeComboBox.getValue());
        if (downloadTypeComboBox.getValue() != null && !downloadTypeComboBox.getValue().toString().isEmpty()) {
            if (comboboxd == "Aircraft Type") {
                comboboxvalue = "getAllFlights";
                List<AircraftDetails> lista = connection.getAllFlights();

                try {
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    File file = new File("Aircrafttype.xlsx");
                    FileOutputStream out = new FileOutputStream(file);

                    XSSFSheet Spreadsheet = workbook.createSheet("Aircraft Type");
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerFont.setFontHeightInPoints((short) 12);
                    headerFont.setColor(IndexedColors.BLUE.getIndex());
                    CellStyle headerCellStyle = workbook.createCellStyle();
                    headerCellStyle.setFont(headerFont);
                    headerCellStyle.setWrapText(true);
                    headerCellStyle.setShrinkToFit(true);

                    String[] array1 = { "aircraftId", "aircraftName", "galleyPosition", "UpdateDateAndtime",
                            "registrationNumber", "ecoClassSeatCount", "businessClassSeatCount", "frontFullCarts",
                            "frontHalfCarts", " frontContainers ", "middleFullCarts", " middleHalfCarts",
                            " middleContainers", " rearFullCarts", " rearHalfCarts", " rearContainers",
                            " recordStatus" };
                    Row r1 = Spreadsheet.createRow(0);

                    for (int k = 0; k < array1.length; k++) {

                        Cell c = r1.createCell(k);
                        c.setCellValue(array1[k].toString());
                        c.setCellStyle(headerCellStyle);

                    }

                    for (int i = 0; i < lista.size(); i++) {
                        Row r = Spreadsheet.createRow(i + 1);

                        int s1 = lista.get(i).getAircraftId();
                        String s2 = lista.get(i).getAircraftName();
                        String s3 = lista.get(i).getGalleyPosition();
                        String s4 = String.valueOf(lista.get(i).getUpdateDateAndtime());
                        String s5 = lista.get(i).getRegistrationNumber();
                        int s6 = lista.get(i).getEcoClassSeatCount();
                        int s7 = lista.get(i).getBusinessClassSeatCount();
                        int s8 = lista.get(i).getFrontFullCarts();
                        int s9 = lista.get(i).getFrontHalfCarts();
                        int s10 = lista.get(i).getFrontContainers();
                        int s11 = lista.get(i).getMiddleFullCarts();
                        int s12 = lista.get(i).getMiddleHalfCarts();
                        int s13 = lista.get(i).getMiddleContainers();
                        int s14 = lista.get(i).getRearFullCarts();
                        int s15 = lista.get(i).getRearHalfCarts();
                        int s16 = lista.get(i).getRearContainers();
                        int s17 = lista.get(i).getRecordStatus();
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
                        c9.setCellValue(s10);
                        Cell c10 = r.createCell(10);
                        c10.setCellValue(s11);
                        Cell c11 = r.createCell(11);
                        c11.setCellValue(s12);
                        Cell c12 = r.createCell(12);
                        c12.setCellValue(s13);
                        Cell c13 = r.createCell(13);
                        c13.setCellValue(s14);
                        Cell c14 = r.createCell(14);
                        c14.setCellValue(s15);
                        Cell c15 = r.createCell(15);
                        c15.setCellValue(s16);
                        Cell c16 = r.createCell(16);
                        c16.setCellValue(s17);

                    }

                    workbook.write(out);
                    out.close();

                    downloaddatah(file);

                    workbook.close();
//					file.delete();

                } catch (Exception e) {
                    Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

                }

            } else if (comboboxd == "Currency") {
                comboboxvalue = "getAllCurrencies";
                List<CurrencyDetails> lista = connection.getAllCurrencies();

                try {
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    File file = new File("Currency.xlsx");
                    FileOutputStream out = new FileOutputStream(file);
                    XSSFSheet Spreadsheet = workbook.createSheet("Currency");
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerFont.setFontHeightInPoints((short) 12);
                    headerFont.setColor(IndexedColors.BLUE.getIndex());
                    CellStyle headerCellStyle = workbook.createCellStyle();
                    headerCellStyle.setFont(headerFont);
                    headerCellStyle.setWrapText(true);

                    String[] array1 = { "Currency Code", "Currency Description", "Currency Rate", "Currency Type",
                            "Priority Order", "Effective Date" };
                    Row r1 = Spreadsheet.createRow(0);

                    for (int k = 0; k < array1.length; k++) {

                        Cell c = r1.createCell(k);
                        c.setCellValue(array1[k].toString());
                        c.setCellStyle(headerCellStyle);

                    }

                    for (int i = 0; i < lista.size(); i++) {
                        Row r = Spreadsheet.createRow(i + 1);

                        String s1 = lista.get(i).getCurrencyCode();
                        String s2 = lista.get(i).getCurrencyDesc();
                        Float s3 = lista.get(i).getCurrencyRate();
                        String s4 = lista.get(i).getCurrencyType();
                        String s5 = lista.get(i).getPriorityOrder();
                        String s6 = lista.get(i).getEffectiveDate();
                        Cell c = r.createCell(0);
                        c.setCellValue(s1);
                        Cell c1 = r.createCell(1);
                        c1.setCellValue(s2);
                        Cell c2 = r.createCell(2);
                        c2.setCellValue(s3);
                        Cell c3 = r.createCell(3);
                        c3.setCellValue(s4);
                        Cell c4 = r.createCell(4);
                        Cell c5 = r.createCell(5);
                        c4.setCellValue(s5);
                        c5.setCellValue(s6);

                    }

                    downloaddatah(file);

                    workbook.write(out);
                    out.close();
                    workbook.close();
//					file.delete();

                } catch (Exception e) {
                    Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

                }
            } else if (comboboxd == "Create Items") {
                comboboxvalue = "getAllItems";
                List<ItemDetails> lista = connection.getAllItems();

                try {
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    File file = new File("Createitem.xlsx");
                    FileOutputStream out = new FileOutputStream(file);
                    XSSFSheet Spreadsheet = workbook.createSheet("Create_Item");
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerFont.setFontHeightInPoints((short) 12);
                    headerFont.setColor(IndexedColors.BLUE.getIndex());
                    CellStyle headerCellStyle = workbook.createCellStyle();
                    headerCellStyle.setFont(headerFont);
                    headerCellStyle.setWrapText(true);

                    String[] array1 = { "recordStatusc", "itemId", "Item Name", "Service Type", "Category",
                            "Catalogue No", "Weight (Grams)", "Cost Currency", "Cost Price", "Base Currency",
                            "Base Price", "Activate Date", "Item No", "Second Currency", "Second Price", "De listed",
                            "RFIcccD", "Barccode" };
                    Row r1 = Spreadsheet.createRow(0);

                    for (int k = 0; k < array1.length; k++) {

                        Cell c = r1.createCell(k);
                        c.setCellValue(array1[k].toString());
                        c.setCellStyle(headerCellStyle);

                    }

                    for (int i = 0; i < lista.size(); i++) {
                        Row r = Spreadsheet.createRow(i + 1);

                        int s1 = lista.get(i).getRecordStatus();
                        int s2 = lista.get(i).getItemId();
                        String s3 = lista.get(i).getItemName();
                        String s5 = lista.get(i).getCategory();
                        String s6 = lista.get(i).getCatalogue();
                        String s8 = lista.get(i).getCostCurrency();
                        float s9 = lista.get(i).getCostPrice();
                        String s10 = lista.get(i).getBaseCurrency();
                        float s11 = lista.get(i).getBasePrice();
                        String s12 = lista.get(i).getActivateDate();
                        String s13 = lista.get(i).getItemCode();
                        String s14 = lista.get(i).getSecondCurrency();
                        float s15 = lista.get(i).getSecondPrice();
                        String s16 = lista.get(i).getDeListed();
                        Cell c = r.createCell(0);
                        c.setCellValue(s1);
                        Cell c1 = r.createCell(1);
                        c1.setCellValue(s2);
                        Cell c2 = r.createCell(2);
                        c2.setCellValue(s3);
                        Cell c3 = r.createCell(3);
                        Cell c4 = r.createCell(4);
                        c4.setCellValue(s5);
                        Cell c5 = r.createCell(5);
                        c5.setCellValue(s6);
                        Cell c6 = r.createCell(6);
                        Cell c7 = r.createCell(7);
                        c7.setCellValue(s8);
                        Cell c8 = r.createCell(8);
                        c8.setCellValue(s9);
                        Cell c9 = r.createCell(9);
                        c9.setCellValue(s10);
                        Cell c10 = r.createCell(10);
                        c10.setCellValue(s11);
                        Cell c11 = r.createCell(11);
                        c11.setCellValue(s12);
                        Cell c12 = r.createCell(12);
                        c12.setCellValue(s13);
                        Cell c13 = r.createCell(13);
                        c13.setCellValue(s14);
                        Cell c14 = r.createCell(14);
                        c14.setCellValue(s15);
                        Cell c15 = r.createCell(15);
                        c15.setCellValue(s16);
                        Cell c16 = r.createCell(16);

                    }

                    downloaddatah(file);

                    workbook.write(out);
                    out.close();
                    workbook.close();

                } catch (Exception e) {
                    Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

                }
            } else if (comboboxd == "Create Kit Codes") {
                comboboxvalue = "getAllKitCodes";
                List<KitCodes> lista = connection.getAllKitCodes();

                try {
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    File file = new File("CreateKitCode.xlsx");
                    FileOutputStream out = new FileOutputStream(file);
                    XSSFSheet Spreadsheet = workbook.createSheet("Create_kit_code");
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerFont.setFontHeightInPoints((short) 12);
                    headerFont.setColor(IndexedColors.BLUE.getIndex());
                    CellStyle headerCellStyle = workbook.createCellStyle();
                    headerCellStyle.setFont(headerFont);
                    headerCellStyle.setWrapText(true);

                    String[] array1 = { "recordStatus", "kitCodeId", "Kit Code", "Description", "Service Type",
                            "Activate Date", "No of Equipments", "Pack Types" };
                    Row r1 = Spreadsheet.createRow(0);

                    for (int k = 0; k < array1.length; k++) {

                        Cell c = r1.createCell(k);
                        c.setCellValue(array1[k].toString());
                        c.setCellStyle(headerCellStyle);

                    }

                    for (int i = 0; i < lista.size(); i++) {
                        Row r = Spreadsheet.createRow(i + 1);

                        int s1 = lista.get(i).getRecordStatus();
                        int s2 = lista.get(i).getKitCodeId();
                        String s3 = lista.get(i).getKitCode();
                        String s4 = lista.get(i).getDescription();
                        String s5 = lista.get(i).getServiceType();
                        String s6 = lista.get(i).getActivateDate();
                        int s7 = lista.get(i).getNoOfEquipments();
                        String s8 = lista.get(i).getPackTypes();
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
                        c7.setCellValue(s8);
                        c.setCellStyle(headerCellStyle);

                    }

                    downloaddatah(file);

                    workbook.write(out);
                    out.close();
                    workbook.close();

                } catch (Exception e) {
                    Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

                }
            } else if (comboboxd == "Equipment Types") {
                comboboxvalue = "EquipmentDetails";
//				list = connection.getAllFlights();
                String EquipmentDetails = "EquipmentDetails";
                List<EquipmentDetails> lista = (List<EquipmentDetails>) connection
                        .getAllValues("com.back.office.entity.EquipmentDetails");
                List listb = connection.getAllItems();

                try {
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    File file = new File("EquipmentDetails.xlsx");
                    FileOutputStream out = new FileOutputStream(file);

                    XSSFSheet Spreadsheet = workbook.createSheet("EquipmentDetails");
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerFont.setFontHeightInPoints((short) 12);
                    headerFont.setColor(IndexedColors.BLUE.getIndex());
                    CellStyle headerCellStyle = workbook.createCellStyle();
                    headerCellStyle.setFont(headerFont);
                    headerCellStyle.setWrapText(true);
                    headerCellStyle.setShrinkToFit(true);

                    String[] array1 = { "recordStatus", "equipmentId", "Pack Type", "Pack Description",
                            "Equipment Type", "No of Drawers", "No of Seals" };
                    Row r1 = Spreadsheet.createRow(0);

                    for (int k = 0; k < array1.length; k++) {

                        Cell c = r1.createCell(k);
                        c.setCellValue(array1[k].toString());
                        c.setCellStyle(headerCellStyle);

                    }

                    for (int i = 0; i < lista.size(); i++) {
                        Row r = Spreadsheet.createRow(i + 1);

                        int s1 = lista.get(i).getRecordStatus();
                        int s2 = lista.get(i).getEquipmentId();
                        String s3 = lista.get(i).getPackType();
                        String s4 = lista.get(i).getPackDescription();
                        String s5 = lista.get(i).getEquipmentType();
                        int s6 = lista.get(i).getNoOfDrawers();
                        int s7 = lista.get(i).getNoOfSeals();

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

                    }

                    workbook.write(out);
                    out.close();

                    downloaddatah(file);
                    workbook.close();

                } catch (Exception e) {
                    Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

                }
            } else if (comboboxd == "CC Black List") {
                comboboxvalue = "BlackList";
                List<BlackListCC> lista = (List<BlackListCC>) connection
                        .getAllValues("com.back.office.entity.BlackListCC");
                try {
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    File file = new File("BlackList.xlsx");
                    FileOutputStream out = new FileOutputStream(file);

                    XSSFSheet Spreadsheet = workbook.createSheet("BlackList");
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerFont.setFontHeightInPoints((short) 12);
                    headerFont.setColor(IndexedColors.BLUE.getIndex());
                    CellStyle headerCellStyle = workbook.createCellStyle();
                    headerCellStyle.setFont(headerFont);
                    headerCellStyle.setWrapText(true);
                    headerCellStyle.setShrinkToFit(true);

                    String[] array1 = { "creditCardId", "creditCardNumber", "status", "recordStatus" };
                    Row r1 = Spreadsheet.createRow(0);

                    for (int k = 0; k < array1.length; k++) {

                        Cell c = r1.createCell(k);
                        c.setCellValue(array1[k].toString());
                        c.setCellStyle(headerCellStyle);

                    }

                    for (int i = 0; i < lista.size(); i++) {
                        Row r = Spreadsheet.createRow(i + 1);

                        int s1 = lista.get(i).getCreditCardId();
                        String s2 = lista.get(i).getCreditCardNumber();
                        String s3 = lista.get(i).getStatus();
                        int s4 = lista.get(i).getRecordStatus();

                        Cell c = r.createCell(0);
                        c.setCellValue(s1);
                        Cell c1 = r.createCell(1);
                        c1.setCellValue(s2);
                        Cell c2 = r.createCell(2);
                        c2.setCellValue(s3);
                        Cell c3 = r.createCell(3);
                        c3.setCellValue(s4);
                        Cell c4 = r.createCell(4);
                        c4.setCellValue(s4);

                    }

                    workbook.write(out);
                    out.close();

                    workbook.close();
                    file.delete();

                } catch (Exception e) {
                    Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

                }
            } else if (comboboxd == "Vouchers") {
                comboboxvalue = "Voucher";
                List<Voucher> lista = (List<Voucher>) connection.getAllValues("com.back.office.entity.Voucher");

                try {
                    XSSFWorkbook workbook = new XSSFWorkbook();
                    File file = new File("Voucher.xlsx");
                    FileOutputStream out = new FileOutputStream(file);

                    XSSFSheet Spreadsheet = workbook.createSheet("Voucher");
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerFont.setFontHeightInPoints((short) 12);
                    headerFont.setColor(IndexedColors.BLUE.getIndex());
                    CellStyle headerCellStyle = workbook.createCellStyle();
                    headerCellStyle.setFont(headerFont);
                    headerCellStyle.setWrapText(true);
                    headerCellStyle.setShrinkToFit(true);

                    String[] array1 = { "recordStatus", "voucherId", "voucherName", "voucherType", "discount",
                            "activateDate", "endDate", "amount" };
                    Row r1 = Spreadsheet.createRow(0);

                    for (int k = 0; k < array1.length; k++) {

                        Cell c = r1.createCell(k);
                        c.setCellValue(array1[k].toString());
                        c.setCellStyle(headerCellStyle);

                    }

                    for (int i = 0; i < lista.size(); i++) {
                        Row r = Spreadsheet.createRow(i + 1);

                        int s1 = lista.get(i).getRecordStatus();
                        int s2 = lista.get(i).getVoucherId();
                        String s3 = lista.get(i).getVoucherName();
                        String s4 = lista.get(i).getVoucherType();
                        String s5 = lista.get(i).getDiscount();
                        String s6 = lista.get(i).getActivateDate();
                        String s7 = lista.get(i).getEndDate();

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

                    downloaddatah(file);

                    workbook.close();

                } catch (Exception e) {
                    Notification.show("Something wrong", Notification.Type.WARNING_MESSAGE);

                }
            }
        } else {

        }

    }

    public void downloaddatah(File file) {

        com.vaadin.server.FileResource fir = new com.vaadin.server.FileResource(file);
        FileDownloader fid = new FileDownloader(fir);
        fid.setOverrideContentType(false);
        downloadbuttonh.addClickListener(Listener -> pageseth(fid));
        fid.extend(downloadbuttonh);

    }

    public void pageseth(FileDownloader filedown) {
        Page.getCurrent().reload();

    }

}
