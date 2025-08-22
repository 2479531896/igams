package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjxxjgDto")
public class SjxxjgDto extends SjxxjgModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//相应检测类型下的数据总和
	private int num;
	//用于取详细结果最底层的子数据
	private String np;

	public String getNp() {
		return np;
	}

	public void setNp(String np) {
		this.np = np;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}	
}
