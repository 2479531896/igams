package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DdfbsglDto")
public class DdfbsglDto extends DdfbsglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//业务类型名称
	private String ywlxmc;
	//关联单号
	private String gldh;
	//业务类型s（高级筛选）
	private String[] ywlxs;

	//开始日期
	private String rqstart;

	//结束日期
	private String rqend;
	//miniappid 获取钉钉
	private String miniappid;
	

	public String getMiniappid() {
		return miniappid;
	}

	public void setMiniappid(String miniappid) {
		this.miniappid = miniappid;
	}

	public String getRqstart() {
		return rqstart;
	}

	public void setRqstart(String rqstart) {
		this.rqstart = rqstart;
	}

	public String getRqend() {
		return rqend;
	}

	public void setRqend(String rqend) {
		this.rqend = rqend;
	}

	public String[] getYwlxs() {
		return ywlxs;
	}

	public void setYwlxs(String[] ywlxs) {
		this.ywlxs = ywlxs;
		for(int i=0;i<this.ywlxs.length;i++)
		{
			this.ywlxs[i] = this.ywlxs[i].replace("'", "");
		}
	}

	public String getGldh() {
		return gldh;
	}

	public void setGldh(String gldh) {
		this.gldh = gldh;
	}

	public String getYwlxmc() {
		return ywlxmc;
	}

	public void setYwlxmc(String ywlxmc) {
		this.ywlxmc = ywlxmc;
	}
	
}
