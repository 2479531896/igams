package com.matridx.igams.wechat.util;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.wechat.service.impl.ResendReport;

public class ResendReportThread extends Thread{
	
	private ResendReport resendReport;
	
	public ResendReportThread(ResendReport resendReport){
		this.resendReport = resendReport;
		
	}

	@Override
	public void run() {
		try {
			resendReport.resendNewReportDeal();
		} catch (BusinessException e) {
			throw new RuntimeException(e);
		}
	}
}
