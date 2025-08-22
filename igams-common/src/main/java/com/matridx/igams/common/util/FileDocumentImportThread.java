package com.matridx.igams.common.util;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.impl.FileComImport;

public class FileDocumentImportThread extends Thread{
	
	private final FileComImport fileComImport;
	
	public FileDocumentImportThread(FileComImport fileComImport){
		this.fileComImport = fileComImport;
		
	}

	@Override
	public void run() {
		try {
			fileComImport.FileDocumentImportCheck();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
