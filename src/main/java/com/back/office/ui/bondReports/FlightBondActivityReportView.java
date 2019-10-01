package com.back.office.ui.bondReports;

import com.back.office.entity.*;
import com.back.office.ui.salesReports.ReportCommonView;
import com.back.office.utils.BackOfficeUtils;
import com.back.office.utils.Constants;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.poi.ss.usermodel.*;
import org.vaadin.addons.filteringgrid.FilterGrid;
import org.vaadin.addons.filteringgrid.filters.InMemoryFilter;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class FlightBondActivityReportView extends ReportCommonView {

    protected DateField flightDateFromDateField;
    protected DateField flightDateToDateField;
    protected FilterGrid<SIFDetails> detailsTable;
    List<SIFDetails> list;

    private final String FLIGHT_DATE_FROM = "Flight Date(From)";
    private final String FLIGHT_DATE_TO = "Flight Date(To)";
    private final String SIF_NO = "SIF No";
    private final String DEVICE_ID = "HHC No";
    private final String DOWNLOADED = "Downloaded";
    private final String PACKED_FOR =  "Packed For";
    private final String PACKED_TIME = "Packed";
    private final String CREW_OPEN_TIME = "Crew Opened";
    private final String CREW_CLOSE_TIME = "Crew Closed";
    private final String DEPARTURE_DATE = "Departure Date";
    private final String STATUS = "Status";
    private final String PROGRAM = "Service";
    Button downloadPdfBtn;

    public FlightBondActivityReportView(){
        super();
    }

    @Override
    protected void createMainLayout() {
        super.createMainLayout();
        HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.addStyleName(ValoTheme.LAYOUT_HORIZONTAL_WRAPPING);
        firstRow.setSpacing(true);
        firstRow.setSizeFull();
        firstRow.setWidth("60%");
        firstRow.setMargin(Constants.noMargin);
        firstRow.addStyleName("report-filter-panel");

        mainUserInputLayout.addComponent(firstRow);
        downloadPdfBtn = new Button();
        Date date = new Date();
        LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        flightDateFromDateField = new DateField(FLIGHT_DATE_FROM);
        flightDateFromDateField.setValue(today);
        flightDateFromDateField.setSizeFull();
        flightDateFromDateField.setStyleName("datePickerStyle");
        firstRow.addComponent(flightDateFromDateField);

        flightDateToDateField = new DateField(FLIGHT_DATE_TO);
        flightDateToDateField.setValue(today);
        flightDateToDateField.setSizeFull();
        flightDateToDateField.setStyleName("datePickerStyle");
        firstRow.addComponents(flightDateToDateField,buttonRow);

        detailsTable = new FilterGrid<>();
        detailsTable.setColumnReorderingAllowed(true);
        detailsTable.setSizeFull();
        tableLayout.addComponent(detailsTable);
        //userFormLayout.setWidth("40%");
        createShowTableHeader();
        GridContextMenu<SIFDetails> gridMenu = new GridContextMenu<>(detailsTable);
        gridMenu.addGridBodyContextMenuListener(this::updateGridBodyMenu);
    }

    protected void updateGridBodyMenu(GridContextMenu.GridContextMenuOpenListener.GridContextMenuOpenEvent<?> event) {
        event.getContextMenu().removeItems();
        if (event.getItem() != null) {
            SIFDetails sifDetails = (SIFDetails) event.getItem();
            if(sifDetails.getPrograms() != null && !sifDetails.getPrograms().isEmpty()) {
                List<String> serviceTypes = Arrays.asList(sifDetails.getPrograms().split(","));
                if (serviceTypes.contains("BOB")) {
                    event.getContextMenu().addItem("Create BOB SIF Form", VaadinIcons.EDIT, selectedItem -> {
                        downloadPDF("Buy on board",sifDetails);
                    });
                }
                if (serviceTypes.contains("DTF")) {
                    event.getContextMenu().addItem("Create DTF SIF Form", VaadinIcons.EDIT, selectedItem -> {
                        downloadPDF("Duty Free",sifDetails);
                    });
                }
                if (serviceTypes.contains("DTP")) {
                    event.getContextMenu().addItem("Create DTP SIF Form", VaadinIcons.EDIT, selectedItem -> {

                    });
                }
                if (serviceTypes.contains("VRT")) {
                    event.getContextMenu().addItem("Create VRT SIF Form", VaadinIcons.EDIT, selectedItem -> {

                    });
                }
                event.getContextMenu().addItem("Download Galley Weight Report", VaadinIcons.DOWNLOAD, selectedItem -> {
                    downloadGalleyWeightReport(sifDetails);
                });
            }
        }
    }

    private void downloadGalleyWeightReport(SIFDetails sifDetails){

        List<CartNumber> cartNumbers = connection.getCartNumbersFromSIF(String.valueOf(sifDetails.getSIFNo()));
        Map<String,ItemDetails> itemDetailsMap = connection.getItemCodeDetailsMap();
        List<String> equipmentTypeList = cartNumbers.stream().map( n -> n.getPackType()).collect( Collectors.toList() );
        List<EquipmentDetails> equipmentDetails = connection.getEquipmentsFromType(equipmentTypeList);
        Map<String, EquipmentDetails> equipmentDetailsMap = new HashMap<>();
        for(EquipmentDetails eq : equipmentDetails){
            if(!equipmentDetailsMap.containsKey(eq.getPackType())){
                equipmentDetailsMap.put(eq.getPackType(),eq);
            }
        }

        Map<String,List<String>> serviceTypeCartNoMap = new HashMap<>();
        for(CartNumber cartNumber : cartNumbers){
            if(serviceTypeCartNoMap.containsKey(cartNumber.getServiceType())){
                List<String> cartNumberList = serviceTypeCartNoMap.get(cartNumber.getServiceType());
                cartNumberList.add((cartNumber.getCartNumber()));
                serviceTypeCartNoMap.put(cartNumber.getServiceType(),cartNumberList);
            }
            else{
                List<String> cartNumberList = new ArrayList<>();
                cartNumberList.add(cartNumber.getCartNumber());
                serviceTypeCartNoMap.put(cartNumber.getServiceType(),cartNumberList);
            }
        }

        Map<String,String> cartNoTypeMap = new HashMap<>();
        for(CartNumber cartNumber : cartNumbers){
            cartNoTypeMap.put(cartNumber.getCartNumber(),cartNumber.getPackType());
        }
        Document document = new Document();

        File pdfFile = new File(sifDetails.getSIFNo()+"_galley_weight.pdf");
        FileResource fileResoce = new FileResource(pdfFile);
        FileDownloader pdfFileDownloader = new FileDownloader(fileResoce);

        Font redFont = FontFactory.getFont(FontFactory.TIMES, 10);
        Font greenFont = FontFactory.getFont(FontFactory.TIMES, 12);
        Font yellowFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD);

        Font serviceTypeHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD);
        Font cartNoHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 10, Font.BOLD);
        try {

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.setPageSize(PageSize.A4);
            Paragraph header = new Paragraph("Galley weight (SIF) - " + sifDetails.getSIFNo(), yellowFont);
            header.setAlignment(1);
            document.add(header);
            document.add(Chunk.NEWLINE);

            String depDateFromToStr = "Flight No : " +sifDetails.getPackedFor();
            String flightDateStr = "flight Date : " + BackOfficeUtils.getDateStringFromDate(sifDetails.getFlightDate());
            String posDeviceId = "POS ID : " + sifDetails.getDeviceId();
            Paragraph depDateFromToParagraph = new Paragraph(depDateFromToStr,redFont);
            document.add(depDateFromToParagraph);
            Paragraph flightDateParagraph = new Paragraph(flightDateStr,redFont);
            document.add(flightDateParagraph);
            Paragraph posDeviceIdParagraph = new Paragraph(posDeviceId,redFont);
            document.add(posDeviceIdParagraph);

            for(Map.Entry<String,List<String>> serviceType : serviceTypeCartNoMap.entrySet()){
                String para = serviceType.getKey() + " Carts  : ";
                for(String cartNo : serviceType.getValue()){
                    para += cartNo + "  ";
                }
                Paragraph serviceTypePara = new Paragraph(para,redFont);
                document.add(serviceTypePara);
            }

            float toalSifWeight = 0;
            for(Map.Entry<String,List<String>> serviceType : serviceTypeCartNoMap.entrySet()){
                document.add( Chunk.NEWLINE );
                String para = "Service Type  : " +serviceType.getKey();
                Paragraph serviceTypePara = new Paragraph(para,serviceTypeHeader);
                document.add(serviceTypePara);

                for(String cartNo : serviceType.getValue()){
                    float totalWeight;
                    Paragraph cartoNoPara = new Paragraph("Cart No : "+cartNo,cartNoHeader);
                    document.add(cartoNoPara);
                    document.add( Chunk.NEWLINE );
                    EquipmentDetails eq = equipmentDetailsMap.get(cartNoTypeMap.get(cartNo));
                    document.add(new Paragraph("Cart Weight : " + eq.getCartWeight() + "g",greenFont));
                    document.add(new Paragraph("Drawer Weight : " + eq.getDraweWeight() * eq.getNoOfDrawers() + "g",greenFont));
                    totalWeight = eq.getCartWeight() + eq.getNoOfDrawers()*eq.getDraweWeight();

                    document.add( Chunk.NEWLINE );
                    List<OpeningInventory> cartInventory = connection.getOpeningInventoryFromSIF(sifDetails.getSIFNo(),cartNo);
                    PdfPTable itemTable = new PdfPTable(new float[] {1,2,1 });
                    itemTable.setWidthPercentage(60);
                    itemTable.addCell(new Paragraph("Item No",greenFont));
                    itemTable.addCell(new Paragraph("Description",greenFont));
                    itemTable.addCell(new Paragraph("Weight (Grams) ",greenFont));

                    for(OpeningInventory inventory : cartInventory){
                        ItemDetails item = itemDetailsMap.get(inventory.getItemCode());
                        totalWeight += item.getWeight() * inventory.getQuantity();
                        itemTable.addCell(new Paragraph(item.getItemCode(),redFont));
                        itemTable.addCell(new Paragraph(item.getItemName(),redFont));
                        itemTable.addCell(new Paragraph(item.getWeight() * inventory.getQuantity()+"",redFont));
                    }
                    document.add(itemTable);
                    document.add(new Paragraph("Cart Total Weight : " + totalWeight/1000 + "Kg",redFont));
                    toalSifWeight += totalWeight;
                }
            }
            document.add( Chunk.NEWLINE );
            document.add(new Paragraph("Total SIF Weight : " + toalSifWeight/1000 + "Kg",yellowFont));
            document.close();
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        pdfFileDownloader.extend(printBtn);
    }

    private void downloadPDF(String type,SIFDetails sifDetails){
        Document document = new Document();
        Date date = new Date();

        File pdfFile = new File(date.getTime()+"bob_sif1.pdf");
        FileResource fileResoce = new FileResource(pdfFile);
        FileDownloader pdfFileDownloader = new FileDownloader(fileResoce);

        Font redFont = FontFactory.getFont(FontFactory.TIMES, 10);
        Font yellowFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD);

        Font tableHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 8, Font.BOLD);
        Font tableContent = FontFactory.getFont(FontFactory.TIMES, 8, Font.BOLD);
        try {

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.setPageSize(PageSize.A4);
            Paragraph header = new Paragraph("SIF Details - " + type,yellowFont);
            header.setAlignment(1);
            document.add(header);
            document.add( Chunk.NEWLINE );

            String depDateFromToStr = "Dep date : " +BackOfficeUtils.getDateStringFromDate(sifDetails.getFlightDate()) + " From : YYZ" + " To : UUL";
            Paragraph depDateFromToParagraph = new Paragraph(depDateFromToStr,redFont);
            depDateFromToParagraph.setAlignment(2);
            document.add(depDateFromToParagraph);
            document.add( Chunk.NEWLINE );

            PdfPTable table = new PdfPTable(new float[] { 2, 1,1 });
            table.setWidthPercentage(100);
            PdfPCell cell1 = new PdfPCell(new Paragraph("SIF No : " + sifDetails.getSIFNo(),redFont));
            cell1.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell1);
            PdfPCell cell2 = new PdfPCell(new Paragraph("Flight No : " + sifDetails.getPackedFor(),redFont));
            cell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell2);
            PdfPCell cell3 = new PdfPCell(new Paragraph("Outbound",redFont));
            cell3.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell3);

            PdfPCell cell4 = new PdfPCell(new Paragraph("KIT CODE : " + sifDetails.getSIFNo(),redFont));
            cell4.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell4);
            PdfPCell cell5 = new PdfPCell(new Paragraph("Flight No : " + sifDetails.getPackedFor(),redFont));
            cell5.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell5);
            PdfPCell cell6 = new PdfPCell(new Paragraph("Inbound",redFont));
            cell6.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell6);

            document.add(table);
            document.add( Chunk.NEWLINE );

            PdfPTable cartNoTable = new PdfPTable(2);
            cartNoTable.setWidthPercentage(100);
            PdfPCell cartNoCell = new PdfPCell(new Paragraph("Cart 1. \n A 001 \n 2. 002 \n 3. 003" ,redFont));
            cartNoCell.setVerticalAlignment(0);
            cartNoCell.setBorder(Rectangle.NO_BORDER);
            cartNoTable.addCell(cartNoCell);

            PdfPCell stationCell = new PdfPCell(new Paragraph("Station : YYZ  Packed by : " + sifDetails.getPackedUser()
                    + " Date : " +BackOfficeUtils.getDateStringFromDate(sifDetails.getPackedTime()),redFont));
            stationCell.setBorder(Rectangle.NO_BORDER);
            cartNoTable.addCell(stationCell);

            document.add(cartNoTable);
            document.add( Chunk.NEWLINE );

            PdfPTable itemTable = new PdfPTable(new float[] {1,2,1,1,1,1,1,1,1,1,1,1,1 });
            itemTable.setWidthPercentage(100);
            itemTable.addCell(new Paragraph("Item No",tableHeader));
            itemTable.addCell(new Paragraph("Description",tableHeader));
            itemTable.addCell(new Paragraph("Cart",tableHeader));
            itemTable.addCell(new Paragraph("drawer",tableHeader));
            itemTable.addCell(new Paragraph("Price",tableHeader));
            itemTable.addCell(new Paragraph("Opening",tableHeader));
            itemTable.addCell(new Paragraph("Sold",tableHeader));
            itemTable.addCell(new Paragraph("Closing",tableHeader));
            itemTable.addCell(new Paragraph("Opening",tableHeader));
            itemTable.addCell(new Paragraph("Sold",tableHeader));
            itemTable.addCell(new Paragraph("Closing",tableHeader));
            itemTable.addCell(new Paragraph("Bound",tableHeader));
            itemTable.addCell(new Paragraph("Variance",tableHeader));

            List<OpeningInventory> openingInventories = connection.getOpeningInventory(String.valueOf(sifDetails.getSIFNo()));
            Map<String, ItemDetails> itemDetailsMap = connection.getItemCodeDetailsMap();
            for(OpeningInventory inventory : openingInventories){
                ItemDetails item = itemDetailsMap.get(inventory.getItemCode());
                itemTable.addCell(new Paragraph(inventory.getItemCode(),tableContent));
                itemTable.addCell(new Paragraph(item.getItemName(),tableContent));
                itemTable.addCell(new Paragraph(inventory.getCartNo(),tableContent));
                itemTable.addCell(new Paragraph(inventory.getDrawer(),tableContent));
                itemTable.addCell(new Paragraph(String.valueOf(item.getBasePrice()),tableContent));
                itemTable.addCell(new Paragraph(String.valueOf(inventory.getQuantity()),tableContent));
                itemTable.addCell(new Paragraph(" ",tableContent));
                itemTable.addCell(new Paragraph(" ",tableContent));
                itemTable.addCell(new Paragraph(" ",tableContent));
                itemTable.addCell(new Paragraph(" ",tableContent));
                itemTable.addCell(new Paragraph(" ",tableContent));
                itemTable.addCell(new Paragraph(" ",tableContent));
                itemTable.addCell(new Paragraph(" ",tableContent));
            }
            document.add(itemTable);
            document.add( Chunk.NEWLINE );
            document.close();
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        pdfFileDownloader.extend(printBtn);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printBtn.click();
    }

    private void exportToExcel(){
        String[] arr = {SIF_NO,PACKED_FOR,DEVICE_ID,PACKED_TIME,CREW_OPEN_TIME,CREW_CLOSE_TIME,DOWNLOADED};
        exportToExcel("SIF Details",arr);
    }
    @Override
    protected void defineStringFields() {
        this.pageHeader = "SIF Details";
        this.reportExcelHeader = "SIF Details";
    }

    @Override
    protected void showFilterData() {

        mainTableLayout.setVisible(true);
        Date dateFrom = Date.from(flightDateFromDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateTo = Date.from(flightDateToDateField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        list = connection.getSifDetails(dateFrom,dateTo);
        String outputStr = "Flight Date From: " + BackOfficeUtils.getDateFromDateTime(dateFrom) +
                " , To: " + BackOfficeUtils.getDateFromDateTime(dateTo);
        filterCriteriaText.setValue(outputStr);
        detailsTable.setItems(list);
        String[] arr = {SIF_NO,PACKED_FOR,DEVICE_ID,PACKED_TIME,CREW_OPEN_TIME,CREW_CLOSE_TIME,DOWNLOADED};
        optionButtonRow.removeComponent(optionButtonRow.getComponent(1));
        File file = exportToExcel("SIF Details",arr);
        downloadExcelBtn = getDownloadExcelBtn("SIF Details",file);
        optionButtonRow.addComponent(downloadExcelBtn);

    }

    private void createShowTableHeader(){
        detailsTable.addColumn(SIFDetails::getSIFNo).setCaption(SIF_NO).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(SIFDetails::getDeviceId).setCaption(DEVICE_ID).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(bean->BackOfficeUtils.getDateStringFromDate(bean.getFlightDate())).setCaption(DEPARTURE_DATE).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(SIFDetails::getStatus).setCaption(STATUS).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(SIFDetails::getPackedFor).setCaption("Flight No").
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
        detailsTable.addColumn(SIFDetails::getPrograms).setCaption(PROGRAM).
                setFilter(getColumnFilterField(), InMemoryFilter.StringComparator.containsIgnoreCase());
    }

    @Override
    public Sheet getWorkbook(Sheet sheet){

        int rowNum = 1;
        for(SIFDetails sifDetails: list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0)
                    .setCellValue(String.valueOf(sifDetails.getSIFNo()));
            row.createCell(1)
                    .setCellValue(sifDetails.getPackedFor());
            row.createCell(2)
                    .setCellValue(sifDetails.getDeviceId());

            Cell packedTime = row.createCell(3);
            Date packTime = sifDetails.getPackedTime();
            if(packTime != null) {
                packedTime.setCellValue(sifDetails.getPackedTime());
                packedTime.setCellStyle(dateCellStyle);
            }
            else packedTime.setCellValue("");

            Cell crewOpenCell = row.createCell(4);
            Date crewOpen = sifDetails.getCrewOpenedTime();
            if(crewOpen != null) {
                crewOpenCell.setCellValue(sifDetails.getCrewOpenedTime());
                crewOpenCell.setCellStyle(dateCellStyle);
            }
            else crewOpenCell.setCellValue("");

            Cell crewClosedCell = row.createCell(5);
            Date crewClose = sifDetails.getCrewClosedTime();
            if(crewClose != null){
                crewClosedCell.setCellValue(sifDetails.getCrewClosedTime());
                crewClosedCell.setCellStyle(dateCellStyle);
            }
            else crewClosedCell.setCellValue("");


            Cell downloaded = row.createCell(6);
            downloaded.setCellValue(sifDetails.getDownloaded());
            downloaded.setCellStyle(dateCellStyle);
        }
        return sheet;
    }
}
