package com.matridx.igams.common.util;

import com.matridx.igams.common.service.impl.FileComImport;

public class FileInsertThread extends Thread{
	
	private final FileComImport fileComImport;
	
	public FileInsertThread(FileComImport fileComImport){
		this.fileComImport = fileComImport;
		
	}

	@Override
	public void run() {
		try {
			fileComImport.persistentFileAfterCheck();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
