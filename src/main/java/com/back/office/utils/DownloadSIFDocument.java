package com.back.office.utils;

import com.back.office.db.DBConnection;
import com.back.office.entity.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

public class DownloadSIFDocument implements Runnable {

    DownloadHelper.DownloadServiceListener excelDownloadServiceListener;
    SIFDetails sifDetails;
    protected DBConnection connection;

    public DownloadSIFDocument(DownloadHelper.DownloadServiceListener excelDownloadServiceListener,SIFDetails sifDetails) {
        this.excelDownloadServiceListener = excelDownloadServiceListener;
        this.sifDetails = sifDetails;
    }

    @Override
    public void run() {
        connection = DBConnection.getInstance();
        if(sifDetails.getDownloadType().equals("Galley Weight")){
            downloadGalleyWeightReport();
        }
        else{
            downloadPDF();
        }

    }

    private void downloadPDF(){
        Document document = new Document();
        Date date = new Date();


        try {
            File pdfFile = File.createTempFile("tmp","pdf");

            Font redFont = FontFactory.getFont(FontFactory.TIMES, 10);
            Font yellowFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD);

            Font tableHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 8, Font.BOLD);
            Font tableContent = FontFactory.getFont(FontFactory.TIMES, 8, Font.BOLD);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.setPageSize(PageSize.A4);
            Paragraph header = new Paragraph("SIF Details - " + sifDetails.getDownloadType(),yellowFont);
            header.setAlignment(1);
            document.add(header);
            document.add( Chunk.NEWLINE );
            String serviceType = "";
            if("Buy on Board".equalsIgnoreCase(sifDetails.getDownloadType())) serviceType = "BOB";
            else if("Buy on Board".equalsIgnoreCase(sifDetails.getDownloadType()))serviceType = "DTF";
            else if("Buy on Board".equalsIgnoreCase(sifDetails.getDownloadType()))serviceType = "DTP";
            else serviceType = "VRT";

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

            PdfPCell cell4 = new PdfPCell(new Paragraph("KIT CODE : " + sifDetails.getKitCodes(),redFont));
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

            List<Cart> cartNos = connection.getCartNoFromSifNo(String.valueOf(sifDetails.getSIFNo()));
            String cartNoStr = "Cart # \n";
            int i = 1;
            for(Cart cart : cartNos){
                if(serviceType.equalsIgnoreCase(cart.getServiceType())){
                    cartNoStr += (i + ". " +cart.getCartNumber() + "\n");
                }
            }
            PdfPCell cartNoCell = new PdfPCell(new Paragraph(cartNoStr ,redFont));
            cartNoCell.setVerticalAlignment(0);
            cartNoCell.setBorder(Rectangle.NO_BORDER);
            cartNoTable.addCell(cartNoCell);

            PdfPCell stationCell = new PdfPCell(new Paragraph("Station : YYZ  Packed by : " + sifDetails.getPackedUser()
                    + " Date : " +BackOfficeUtils.getDateStringFromDate(sifDetails.getPackedTime()),redFont));
            stationCell.setBorder(Rectangle.NO_BORDER);
            cartNoTable.addCell(stationCell);

            document.add(cartNoTable);
            document.add( Chunk.NEWLINE );

            PdfPTable headerTable = new PdfPTable(new float[] {6,3,3,2});
            headerTable.setWidthPercentage(100);
            headerTable.addCell(new Paragraph(" ",tableHeader));
            headerTable.addCell(new Paragraph("Outbound",tableHeader));
            headerTable.addCell(new Paragraph("Inbound",tableHeader));
            headerTable.addCell(new Paragraph(" ",tableHeader));

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


            List<SIFSheet> sifSheetList = connection.getSigSheetList(sifDetails.getSIFNo(),serviceType);
            if(sifSheetList != null && !sifSheetList.isEmpty()){
                for(SIFSheet inventory : sifSheetList){
                    int variance = inventory.getObOpenQty() - (inventory.getObClosingQty()+inventory.getObSoldQty()) +
                            inventory.getIbOpenQty() - (inventory.getIbClosingQty()+inventory.getIbSoldQty());
                    itemTable.addCell(new Paragraph(inventory.getItemNo(),tableContent));
                    itemTable.addCell(new Paragraph(inventory.getItemDesc(),tableContent));
                    itemTable.addCell(new Paragraph(inventory.getCart(),tableContent));
                    itemTable.addCell(new Paragraph(inventory.getDrawer(),tableContent));
                    itemTable.addCell(new Paragraph(String.valueOf(inventory.getPrice()),tableContent));
                    itemTable.addCell(new Paragraph(String.valueOf(inventory.getObOpenQty()),tableContent));
                    itemTable.addCell(new Paragraph(String.valueOf(inventory.getObSoldQty()),tableContent));
                    itemTable.addCell(new Paragraph(String.valueOf(inventory.getObClosingQty()),tableContent));
                    itemTable.addCell(new Paragraph(String.valueOf(inventory.getIbOpenQty()),tableContent));
                    itemTable.addCell(new Paragraph(String.valueOf(inventory.getIbSoldQty()),tableContent));
                    itemTable.addCell(new Paragraph(String.valueOf(inventory.getIbClosingQty()),tableContent));
                    itemTable.addCell(new Paragraph(" ",tableContent));
                    itemTable.addCell(new Paragraph(String.valueOf(variance),tableContent));
                }
            }
            else{
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
            }
            document.add(headerTable);
            document.add(itemTable);
            document.add( Chunk.NEWLINE );
            document.add(new Paragraph("Tender Summary"));
            document.add( Chunk.NEWLINE );
            PdfPTable tenderSummaryTable = new PdfPTable(new float[] {1,2,1,1,1,1,1,1,1,1,1,1 });
            tenderSummaryTable.setWidthPercentage(100);
            tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
            tenderSummaryTable.addCell(new Paragraph("FA Name",tableContent));
            tenderSummaryTable.addCell(new Paragraph("Flt #",tableContent));
            tenderSummaryTable.addCell(new Paragraph("Date",tableContent));
            tenderSummaryTable.addCell(new Paragraph("Total Sales",tableContent));
            tenderSummaryTable.addCell(new Paragraph("Cash",tableContent));
            tenderSummaryTable.addCell(new Paragraph("CC",tableContent));
            tenderSummaryTable.addCell(new Paragraph("Vouchers",tableContent));
            tenderSummaryTable.addCell(new Paragraph("Discounts",tableContent));
            tenderSummaryTable.addCell(new Paragraph("Other",tableContent));
            tenderSummaryTable.addCell(new Paragraph("Variance",tableContent));
            tenderSummaryTable.addCell(new Paragraph("Signature",tableContent));
            List<String> inOutStr = Arrays.asList("Outbound","Inbound");
            for(String str : inOutStr){
                tenderSummaryTable.addCell(new Paragraph(str,tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
                tenderSummaryTable.addCell(new Paragraph(" ",tableContent));
            }
            document.add(tenderSummaryTable);
            document.close();
            writer.close();
            excelDownloadServiceListener.onComplete(pdfFile.getPath());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void downloadGalleyWeightReport(){

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


        Font redFont = FontFactory.getFont(FontFactory.TIMES, 10);
        Font greenFont = FontFactory.getFont(FontFactory.TIMES, 12);
        Font yellowFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD);

        Font serviceTypeHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD);
        Font cartNoHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 10, Font.BOLD);
        try {
            Document document = new Document();
            File pdfFile = File.createTempFile("tmp","pdf");
            //File pdfFile = new File(sifDetails.getSIFNo()+"_galley_weight.pdf");
            FileResource fileResoce = new FileResource(pdfFile);
            FileDownloader pdfFileDownloader = new FileDownloader(fileResoce);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.setPageSize(PageSize.A4);
            Paragraph header = new Paragraph("Galley weight (SIF) - " + sifDetails.getSIFNo(), yellowFont);
            header.setAlignment(1);
            document.add(header);
            document.add(Chunk.NEWLINE);

            String depDateFromToStr = "Flight No : " +sifDetails.getPackedFor();
            String flightDateStr = "flight Date : " + BackOfficeUtils.getDateStringFromDate(sifDetails.getFlightDate());
            //String posDeviceId = "POS ID : " + sifDetails.getDeviceId();
            Paragraph depDateFromToParagraph = new Paragraph(depDateFromToStr,redFont);
            document.add(depDateFromToParagraph);
            Paragraph flightDateParagraph = new Paragraph(flightDateStr,redFont);
            document.add(flightDateParagraph);

            float toalSifWeight = 0;
            for(Map.Entry<String,List<String>> serviceType : serviceTypeCartNoMap.entrySet()){
                document.add( Chunk.NEWLINE );
                String para = "Service Type  : " +serviceType.getKey();
                Paragraph serviceTypePara = new Paragraph(para,serviceTypeHeader);
                document.add(serviceTypePara);

                for(String cartNo : serviceType.getValue()){
                    float totalWeight;
                    document.add( Chunk.NEWLINE );
                    Paragraph cartoNoPara = new Paragraph("Cart No : "+cartNo,cartNoHeader);
                    document.add(cartoNoPara);
                    EquipmentDetails eq = equipmentDetailsMap.get(cartNoTypeMap.get(cartNo));
                    document.add(new Paragraph("Pack Type  : "+eq.getPackType()));
                    //document.add(new Paragraph("Cart Weight : " + eq.getCartWeight() + "g",greenFont));
                    //document.add(new Paragraph("Drawer Weight : " + eq.getDraweWeight() * eq.getNoOfDrawers() + "g",greenFont));
                    totalWeight = eq.getCartWeight() + eq.getNoOfDrawers()*eq.getDraweWeight();

                    document.add( Chunk.NEWLINE );
                    List<OpeningInventory> cartInventory = connection.getOpeningInventoryFromSIF(sifDetails.getSIFNo(),cartNo);
                    Map<String,List<OpeningInventory>> drawerInventoryMap = new HashMap<>();
                    for(OpeningInventory inventory : cartInventory){
                        if(drawerInventoryMap.containsKey(inventory.getDrawer())){
                            List<OpeningInventory> cartNumberList = drawerInventoryMap.get(inventory.getDrawer());
                            cartNumberList.add(inventory);
                            drawerInventoryMap.put(inventory.getDrawer(),cartNumberList);
                        }
                        else{
                            List<OpeningInventory> cartNumberList = new ArrayList<>();
                            cartNumberList.add(inventory);
                            drawerInventoryMap.put(inventory.getDrawer(),cartNumberList);
                        }
                    }

                    Map<String, List<OpeningInventory>> sorted = drawerInventoryMap.entrySet().stream().sorted(comparingByKey())
                            .collect( toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));

                    for(Map.Entry<String,List<OpeningInventory>> drawerMap : sorted.entrySet()){
                        document.add(new Paragraph(drawerMap.getKey()));
                        PdfPTable itemTable = new PdfPTable(new float[] {1,2,1,1,1 });
                        itemTable.setWidthPercentage(90);
                        itemTable.addCell(new Paragraph("Item #",greenFont));
                        itemTable.addCell(new Paragraph("Description",greenFont));
                        itemTable.addCell(new Paragraph("Qty",greenFont));
                        itemTable.addCell(new Paragraph("Unit Weight(g)",greenFont));
                        itemTable.addCell(new Paragraph("Weight (g) ",greenFont));

                        for(OpeningInventory inventory : drawerMap.getValue()){
                            ItemDetails item = itemDetailsMap.get(inventory.getItemCode());
                            totalWeight += item.getWeight() * inventory.getQuantity();
                            itemTable.addCell(new Paragraph(item.getItemCode(),redFont));
                            itemTable.addCell(new Paragraph(item.getItemName(),redFont));
                            itemTable.addCell(new Paragraph(String.valueOf(inventory.getQuantity()),redFont));
                            itemTable.addCell(new Paragraph(String.valueOf(item.getWeight()),redFont));
                            itemTable.addCell(new Paragraph(item.getWeight() * inventory.getQuantity()+"",redFont));
                        }
                        document.add(itemTable);
                    }

                    document.add(new Paragraph("Total Cart Weight : " + totalWeight/1000 + "Kg",redFont));
                    toalSifWeight += totalWeight;
                }
            }
            document.add( Chunk.NEWLINE );
            document.add(new Paragraph("Total SIF Weight : " + toalSifWeight/1000 + "Kg",yellowFont));
            document.close();
            writer.close();
            excelDownloadServiceListener.onComplete(pdfFile.getPath());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
