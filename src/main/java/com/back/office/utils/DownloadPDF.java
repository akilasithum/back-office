package com.back.office.utils;

import com.back.office.db.DBConnection;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadPDF implements Runnable {

    DownloadHelper.DownloadServiceListener excelDownloadServiceListener;
    PdfPTable pdfPTable;
    protected DBConnection connection;
    protected String headerName;

    public DownloadPDF(DownloadHelper.DownloadServiceListener excelDownloadServiceListener, PdfPTable pdfPTable,String headerName) {
        this.excelDownloadServiceListener = excelDownloadServiceListener;
        this.pdfPTable = pdfPTable;
        this.headerName = headerName;
    }

    @Override
    public void run() {
        com.itextpdf.text.Font yellowFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 14, com.itextpdf.text.Font.BOLD);
        try {
            Document document = new Document();
            File pdfFile = File.createTempFile("tmp","pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.setPageSize(PageSize.A4);
            Paragraph header = new Paragraph(headerName, yellowFont);
            header.setAlignment(1);
            document.add(header);
            document.add(Chunk.NEWLINE);
            document.add(pdfPTable);
            document.add( Chunk.NEWLINE );
            document.close();
            writer.close();
            excelDownloadServiceListener.onComplete(pdfFile.getPath());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
