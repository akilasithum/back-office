package com.back.office.utils;

import com.back.office.entity.SIFDetails;

public interface DownloadHelper {

    public void createFile(DownloadServiceListener excelDownloadServiceListener, SIFDetails sifDetails);

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
