package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="ShtxModel")
public class ShtxModel extends BaseBasicModel{
	//提醒ID
	private String txid;
	//提醒类别 采用审批类别的数据库
	private String txlb;
	//提醒延期日数
	private String txyqrs;
	//提醒ID
	public String getTxid() {
		return txid;
	}
	public void setTxid(String txid){
		this.txid = txid;
	}
	//提醒类别 采用审批类别的数据库
	public String getTxlb() {
		return txlb;
	}
	public void setTxlb(String txlb){
		this.txlb = txlb;
	}
	//提醒延期日数
	public String getTxyqrs() {
		return txyqrs;
	}
	public void setTxyqrs(String txyqrs){
		this.txyqrs = txyqrs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
