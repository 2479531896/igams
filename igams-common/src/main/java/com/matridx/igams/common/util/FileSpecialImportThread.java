package com.matridx.igams.common.util;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.impl.FileComImport;

public class FileSpecialImportThread extends Thread{
	
	private final FileComImport fileComImport;
	
	public FileSpecialImportThread(FileComImport fileComImport){
		this.fileComImport = fileComImport;
		
	}

	@Override
	public void run() {
		try {
			fileComImport.FileImportSpecialCheck();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
