package com.matridx.igams.detection.molecule.util;


import com.matridx.igams.detection.molecule.service.impl.CovidReportZipExport;

public class CovidReportZipThread extends Thread{
	
	private final CovidReportZipExport covidReportZipExport;
	
	public CovidReportZipThread(CovidReportZipExport covidReportZipExport){
		this.covidReportZipExport = covidReportZipExport;
		
	}

	@Override
	public void run() {
		covidReportZipExport.reportZipExportDeal();
	}
}
