package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjdwxxDto")
public class SjdwxxDto extends SjdwxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String depth;//菜单等级
	private String checked;//是否选中(助手小程序)
	
	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}
}
