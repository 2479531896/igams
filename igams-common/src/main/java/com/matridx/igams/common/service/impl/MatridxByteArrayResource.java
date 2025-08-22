package com.matridx.igams.common.service.impl;

import org.springframework.core.io.ByteArrayResource;

public class MatridxByteArrayResource extends ByteArrayResource{
	private String fileName;
	
	public MatridxByteArrayResource(byte[] byteArray) {
		super(byteArray);
		// TODO Auto-generated constructor stub
	}
	
	public void setFilename(String s_fileName) {
		fileName = s_fileName;
	}

	@Override
    public String getFilename() {
        return fileName;
    }
}
