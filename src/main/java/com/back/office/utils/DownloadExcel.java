package com.back.office.utils;

import com.back.office.db.DBConnection;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DownloadExcel implements Runnable {

    DownloadHelper.DownloadServiceListener excelDownloadServiceListener;
    Workbook workbook;
    protected DBConnection connection;

    public DownloadExcel(DownloadHelper.DownloadServiceListener excelDownloadServiceListener,Workbook workbook) {
        this.excelDownloadServiceListener = excelDownloadServiceListener;
        this.workbook = workbook;
    }

    @Override
    public void run() {
        try {
            File tempFile = File.createTempFile("tmp","xls");
            FileOutputStream fileOut = new FileOutputStream(tempFile,true);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            excelDownloadServiceListener.onComplete(tempFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
