package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="QmngsxcgModel")
public class QmngsxcgModel extends BaseModel{
	//Q-mNGS脓毒症记录id
	private String qmngsndzjlid;
	//检验项目关联基础数据
	private String jyxm;
	//数据值
	private String sjz;
	//Q-mNGS脓毒症记录id
	public String getQmngsndzjlid() {
		return qmngsndzjlid;
	}
	public void setQmngsndzjlid(String qmngsndzjlid){
		this.qmngsndzjlid = qmngsndzjlid;
	}
	//检验项目关联基础数据
	public String getJyxm() {
		return jyxm;
	}
	public void setJyxm(String jyxm){
		this.jyxm = jyxm;
	}
	//数据值
	public String getSjz() {
		return sjz;
	}
	public void setSjz(String sjz){
		this.sjz = sjz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
