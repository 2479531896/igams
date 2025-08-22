package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="CkwxglDto")
public class CkwxglDto extends CkwxglModel{
	//参考文献[报告用]
	private String refs;
	
	public String getRefs() {
		return refs;
	}


	public void setRefs(String refs) {
		this.refs = refs;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
