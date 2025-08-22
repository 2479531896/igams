package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;


@Alias(value="MxxxDto")
public class MxxxDto extends MxxxModel {

	private String yysjstart;
	private String yysjend;
	private String cbgsjstart;
	private String cbgsjend;
	private String[] statuses;
	private String[] types;
	private String sjid;

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getYysjstart() {
		return yysjstart;
	}

	public void setYysjstart(String yysjstart) {
		this.yysjstart = yysjstart;
	}

	public String getYysjend() {
		return yysjend;
	}

	public void setYysjend(String yysjend) {
		this.yysjend = yysjend;
	}

	public String getCbgsjstart() {
		return cbgsjstart;
	}

	public void setCbgsjstart(String cbgsjstart) {
		this.cbgsjstart = cbgsjstart;
	}

	public String getCbgsjend() {
		return cbgsjend;
	}

	public void setCbgsjend(String cbgsjend) {
		this.cbgsjend = cbgsjend;
	}

	public String[] getStatuses() {
		return statuses;
	}

	public void setStatuses(String[] statuses) {
		this.statuses = statuses;
		for(int i=0;i<this.statuses.length;i++)
		{
			this.statuses[i] = this.statuses[i].replace("'", "");
		}
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
		for(int i=0;i<this.types.length;i++)
		{
			this.types[i] = this.types[i].replace("'", "");
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
