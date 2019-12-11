package com.back.office.utils;

import com.back.office.entity.SIFDetails;

public class DownloadHelperImpl implements DownloadHelper{

    @Override
    public void createFile(DownloadServiceListener excelDownloadServiceListener, SIFDetails sifDetails) {
        DownloadSIFDocument sifDocument = new DownloadSIFDocument(excelDownloadServiceListener,sifDetails);
        Thread thread = new Thread(sifDocument);
        thread.run();
    }
}
