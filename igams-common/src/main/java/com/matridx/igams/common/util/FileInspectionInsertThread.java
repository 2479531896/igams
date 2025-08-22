package com.matridx.igams.common.util;

import com.matridx.igams.common.service.impl.FileComInspectionImport;

public class FileInspectionInsertThread extends Thread{
	
	private final FileComInspectionImport fileComInspectionImport;
	
	public FileInspectionInsertThread(FileComInspectionImport fileComInspectionImport){
		this.fileComInspectionImport = fileComInspectionImport;
		
	}

	@Override
	public void run() {
		try {
			fileComInspectionImport.persistentFileInspectionAfterCheck();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
