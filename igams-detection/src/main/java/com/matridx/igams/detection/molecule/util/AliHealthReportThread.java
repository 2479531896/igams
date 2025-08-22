package com.matridx.igams.detection.molecule.util;

import com.matridx.igams.detection.molecule.service.impl.AliHealthReturnReport;

public class AliHealthReportThread extends Thread{
private final AliHealthReturnReport aliHealthReturnReport;

	public AliHealthReportThread(AliHealthReturnReport aliHealthReturnReport){
		this.aliHealthReturnReport = aliHealthReturnReport;
		
	}

	@Override
	public void run() {
		aliHealthReturnReport.returnReport();
	}
}
