package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjjcxmModel")
public class SjjcxmModel extends BaseModel{
	//送检ID
	private String sjid;
	//序号
	private String xh;
	//检测项目  关联基础数据
	private String jcxmid;
	//检测子项目  关联基础数据
	private String jczxmid;
	private String yszkid;//应收账款id
	private String sfsf;
	private String dzje; //对账金额
	private String yfje; //应付金额
	private String sfdz; //是否对账
	private String xmglid; //项目管理id
	private String sfje; //实付金额
	private String fkrq; //付款日期
	private String tkje; //退款金额
	private String tkrq; //退款日期
	private String tkfs; //退款方式
	private String dcbj; //导出标记
  	private String yyfje;//原应付金额

	public String getYyfje() {
		return yyfje;
	}

	public void setYyfje(String yyfje) {
		this.yyfje = yyfje;
	}

	public String getDcbj() {
		return dcbj;
	}

	public void setDcbj(String dcbj) {
		this.dcbj = dcbj;
	}

	public String getXmglid() {
		return xmglid;
	}

	public void setXmglid(String xmglid) {
		this.xmglid = xmglid;
	}

	public String getSfje() {
		return sfje;
	}

	public void setSfje(String sfje) {
		this.sfje = sfje;
	}

	public String getFkrq() {
		return fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}

	public String getTkje() {
		return tkje;
	}

	public void setTkje(String tkje) {
		this.tkje = tkje;
	}

	public String getTkrq() {
		return tkrq;
	}

	public void setTkrq(String tkrq) {
		this.tkrq = tkrq;
	}

	public String getTkfs() {
		return tkfs;
	}

	public void setTkfs(String tkfs) {
		this.tkfs = tkfs;
	}



	public String getYszkid() {
		return yszkid;
	}

	public void setYszkid(String yszkid) {
		this.yszkid = yszkid;
	}

	public String getDzje() {
		return dzje;
	}

	public void setDzje(String dzje) {
		this.dzje = dzje;
	}

	public String getYfje() {
		return yfje;
	}

	public void setYfje(String yfje) {
		this.yfje = yfje;
	}

	public String getSfdz() {
		return sfdz;
	}

	public void setSfdz(String sfdz) {
		this.sfdz = sfdz;
	}

	public String getSfsf() {
		return sfsf;
	}

	public void setSfsf(String sfsf) {
		this.sfsf = sfsf;
	}

	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//检测项目  关联基础数据
	public String getJcxmid() {
		return jcxmid;
	}
	public void setJcxmid(String jcxmid){
		this.jcxmid = jcxmid;
	}

	public String getJczxmid() {
		return jczxmid;
	}

	public void setJczxmid(String jczxmid) {
		this.jczxmid = jczxmid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
