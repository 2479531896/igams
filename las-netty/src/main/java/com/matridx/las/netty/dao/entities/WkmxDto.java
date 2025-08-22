package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WkmxDto")
public class WkmxDto extends WkmxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//文库名称
	private String libraryName;

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}
	
	

}
