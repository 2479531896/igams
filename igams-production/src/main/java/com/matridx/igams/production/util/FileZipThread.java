package com.matridx.igams.production.util;

import com.matridx.igams.production.service.impl.FileZipExport;

public class FileZipThread extends Thread{
private final FileZipExport fileZipExport;
private String bj;//用于区分请购和供应商下载
	
	public FileZipThread(FileZipExport fileZipExport){
		this.fileZipExport = fileZipExport;
		
	}
	public FileZipThread(FileZipExport fileZipExport,String bj){
		this.fileZipExport = fileZipExport;
		this.bj = bj;
	}

	@Override
	public void run() {
		//如果bj为Y执行供应商压缩包下载
		if ("Y".equals(bj)){
			fileZipExport.fileZipExportDealGys();
		}else {
			fileZipExport.fileZipExportDeal();
		}
	}
}
