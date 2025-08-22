package com.matridx.igams.experiment.dao.entities;


import org.apache.ibatis.type.Alias;

@Alias(value="NgsglDto")
public class NgsglDto extends NgsglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//全部(查询条件)
	private String entire;
	private String[] zbbms;	//检索用制备编码（多）
	private String[] jcdws;	//检索用检查单位（多）
	private String[] sfcgs;	//检索用是否成功（多）
	private String sxrqstart;	//生效开始时间
	private String sxrqend;		//生效结束时间	
	private String SqlParam; 	//导出关联标记位//所选择的字段
	public String getEntire() {
		return entire;
	}
	public String[] getZbbms() {
		return zbbms;
	}
	public void setZbbms(String[] zbbms) {
		this.zbbms = zbbms;
		for (int i = 0; i < zbbms.length; i++){
			this.zbbms[i]=this.zbbms[i].replace("'","");
		}
	}
	public String[] getJcdws() {
		return jcdws;
	}
	public void setJcdws(String[] jcdws) {
		this.jcdws = jcdws;
		for (int i = 0; i < jcdws.length; i++){
			this.jcdws[i]=this.jcdws[i].replace("'","");
		}
	}
	public String[] getSfcgs() {
		return sfcgs;
	}
	public void setSfcgs(String[] sfcgs) {
		this.sfcgs = sfcgs;
		for (int i = 0; i < sfcgs.length; i++){
			this.sfcgs[i]=this.sfcgs[i].replace("'","");
		}
	}
	public void setEntire(String entire) {
		this.entire = entire;
	}
	public String getSxrqstart() {
		return sxrqstart;
	}
	public void setSxrqstart(String sxrqstart) {
		this.sxrqstart = sxrqstart;
	}
	public String getSxrqend() {
		return sxrqend;
	}
	public void setSxrqend(String sxrqend) {
		this.sxrqend = sxrqend;
	}
	public String getSqlParam() {
		return SqlParam;
	}
	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}
	

}
