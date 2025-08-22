package com.matridx.igams.detection.molecule.util;

import com.matridx.igams.detection.molecule.service.impl.UploadReportToWjw;

public class UploadReportToWjwThread extends Thread{
    private final UploadReportToWjw uploadReportToWjw;

    public UploadReportToWjwThread(UploadReportToWjw uploadReportToWjw){
        this.uploadReportToWjw=uploadReportToWjw;
    }

    @Override
    public void run() {
        uploadReportToWjw.dockHealthInterface();
    }
}
