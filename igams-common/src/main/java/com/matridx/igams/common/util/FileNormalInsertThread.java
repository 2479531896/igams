package com.matridx.igams.common.util;

import com.matridx.igams.common.service.impl.FileComImport;

public class FileNormalInsertThread extends Thread{
	
	private final FileComImport fileComImport;
	
	public FileNormalInsertThread(FileComImport fileComImport){
		this.fileComImport = fileComImport;
		
	}

	@Override
	public void run() {
		try {
			fileComImport.persistentNormalFileAfterCheck();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
