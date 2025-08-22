package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="InvPositionSumDto")
public class InvPositionSumDto extends InvPositionSumModel{
	//计算标记 0 + ,1 -
	private String jsbj;

	public String getJsbj() {
		return jsbj;
	}

	public void setJsbj(String jsbj) {
		this.jsbj = jsbj;
	}
}
