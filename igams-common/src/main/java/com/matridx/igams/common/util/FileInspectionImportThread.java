package com.matridx.igams.common.util;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.impl.FileComInspectionImport;

public class FileInspectionImportThread extends Thread{
	
	private final FileComInspectionImport fileComInspectionImport;
	
	public FileInspectionImportThread(FileComInspectionImport fileComInspectionImport){
		this.fileComInspectionImport = fileComInspectionImport;
	}

	@Override
	public void run() {
		try {
			fileComInspectionImport.FileInspectionImportCheck();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
