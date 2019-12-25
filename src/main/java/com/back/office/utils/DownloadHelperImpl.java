package com.back.office.utils;

import com.back.office.entity.SIFDetails;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import org.apache.poi.ss.usermodel.Workbook;


public class DownloadHelperImpl implements DownloadHelper{

    @Override
    public void createFile(DownloadServiceListener excelDownloadServiceListener, SIFDetails sifDetails) {
        DownloadSIFDocument sifDocument = new DownloadSIFDocument(excelDownloadServiceListener,sifDetails);
        Thread thread = new Thread(sifDocument);
        thread.run();
    }

    @Override
    public void createExcelFile(DownloadServiceListener excelDownloadServiceListener, Workbook workbook) {
        DownloadExcel downloadExcel = new DownloadExcel(excelDownloadServiceListener,workbook);
        Thread thread = new Thread(downloadExcel);
        thread.run();
    }

    @Override
    public void createPDFFile(DownloadServiceListener excelDownloadServiceListener, PdfPTable table,String headerName) {
        DownloadPDF downloadPDF = new DownloadPDF(excelDownloadServiceListener,table,headerName);
        Thread thread = new Thread(downloadPDF);
        thread.run();
    }
}
