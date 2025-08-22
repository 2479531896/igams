package com.matridx.igams.wechat.dao.entities;

public class CxfFileWrapper {
    // 文件名
    private String fileName;
    // 文件Base64编码
    private String file;

	public final String getFileName() {
        return fileName;
    }

    public final void setFileName(String fileName) {
        this.fileName = fileName;
    }

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
