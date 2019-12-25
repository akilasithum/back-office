package com.back.office.utils;

import com.back.office.entity.SIFDetails;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import org.apache.poi.ss.usermodel.Workbook;

public interface DownloadHelper {

    public void createFile(DownloadServiceListener excelDownloadServiceListener, SIFDetails sifDetails);

    public void createExcelFile(DownloadServiceListener excelDownloadServiceListener, Workbook workbook);

    public void createPDFFile(DownloadServiceListener excelDownloadServiceListener, PdfPTable table, String headerName);

    public interface DownloadServiceListener {
        /**
         * On complete.
         *
         * @param filePath the response
         */
        public void onComplete(String filePath);

        /**
         * On file download fail.
         */
        default public void  onFail(){

        }
    }
}
