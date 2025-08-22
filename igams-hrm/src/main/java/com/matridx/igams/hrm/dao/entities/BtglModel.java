package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="BtglModel")
public class BtglModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String btglid;//补贴管理id
	private String btmc;//补贴名称
	private String bzsc;//标准时长
	private String bzsj;//标准时间
	private String bzje;//标准金额
	private String dzje;//递增金额
	private String dzjg;//递增金额
	private String dzkssj;//递增开始时间
	private String fdje;//封顶金额
	private String bzjrcr;//标准时间 1：今日 0：次日
	private String dzjrcr;//递增时间 1：今日 0：次日

	public String getBzjrcr() {
		return bzjrcr;
	}

	public void setBzjrcr(String bzjrcr) {
		this.bzjrcr = bzjrcr;
	}

	public String getDzjrcr() {
		return dzjrcr;
	}

	public void setDzjrcr(String dzjrcr) {
		this.dzjrcr = dzjrcr;
	}

	public String getBtglid() {
		return btglid;
	}

	public void setBtglid(String btglid) {
		this.btglid = btglid;
	}

	public String getBtmc() {
		return btmc;
	}

	public void setBtmc(String btmc) {
		this.btmc = btmc;
	}

	public String getBzsc() {
		return bzsc;
	}

	public void setBzsc(String bzsc) {
		this.bzsc = bzsc;
	}

	public String getBzsj() {
		return bzsj;
	}

	public void setBzsj(String bzsj) {
		this.bzsj = bzsj;
	}

	public String getBzje() {
		return bzje;
	}

	public void setBzje(String bzje) {
		this.bzje = bzje;
	}

	public String getDzje() {
		return dzje;
	}

	public void setDzje(String dzje) {
		this.dzje = dzje;
	}

	public String getDzjg() {
		return dzjg;
	}

	public void setDzjg(String dzjg) {
		this.dzjg = dzjg;
	}

	public String getDzkssj() {
		return dzkssj;
	}

	public void setDzkssj(String dzkssj) {
		this.dzkssj = dzkssj;
	}

	public String getFdje() {
		return fdje;
	}

	public void setFdje(String fdje) {
		this.fdje = fdje;
	}
}
