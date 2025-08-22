package com.matridx.igams.common.util;

import com.matridx.igams.common.service.impl.FileComExport;

public class FileExportThread extends Thread{
	
	private final FileComExport fileComExport;
	
	public FileExportThread(FileComExport fileComExport){
		this.fileComExport = fileComExport;
		
	}

	@Override
	public void run() {
		fileComExport.commExportDeal();
	}
}
