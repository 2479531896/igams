package com.matridx.igams.wechat.util;

import com.matridx.igams.wechat.service.impl.ReportZipExport;

public class ReportZipThread extends Thread{
	
	private ReportZipExport reportZipExport;
	
	public ReportZipThread(ReportZipExport reportZipExport){
		this.reportZipExport = reportZipExport;
		
	}

	@Override
	public void run() {
		reportZipExport.reportZipExportDeal();
	}
}
