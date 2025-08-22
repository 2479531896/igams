package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="QmngshzxxDto")
public class QmngshzxxDto extends QmngshzxxModel{
	//性别s
	private String [] sfkjjjtzls;
	private String [] cyzts;
	private String [] sjfzjgs;
	private String [] xbs;
	private String [] grxgzds;
	private String  zgcysjstart;
	private String  zgcysjend;
	private String  txrqstart;
	private String  txrqend;
	private String  hzrzsjstart;
	private String  hzrzsjend;
	//医疗中心限制
	private List<String> yyxz;

	public List<String> getYyxz() {
		return yyxz;
	}

	public void setYyxz(List<String> yyxz) {
		this.yyxz = yyxz;
	}

	public String[] getSfkjjjtzls() {
		return sfkjjjtzls;
	}

	public void setSfkjjjtzls(String[] sfkjjjtzls) {
		this.sfkjjjtzls = sfkjjjtzls;
	}

	public String[] getCyzts() {
		return cyzts;
	}

	public void setCyzts(String[] cyzts) {
		this.cyzts = cyzts;
	}

	public String[] getSjfzjgs() {
		return sjfzjgs;
	}

	public void setSjfzjgs(String[] sjfzjgs) {
		this.sjfzjgs = sjfzjgs;
	}

	public String[] getXbs() {
		return xbs;
	}

	public void setXbs(String[] xbs) {
		this.xbs = xbs;
	}

	public String[] getGrxgzds() {
		return grxgzds;
	}

	public void setGrxgzds(String[] grxgzds) {
		this.grxgzds = grxgzds;
	}

	public String getZgcysjstart() {
		return zgcysjstart;
	}

	public void setZgcysjstart(String zgcysjstart) {
		this.zgcysjstart = zgcysjstart;
	}

	public String getZgcysjend() {
		return zgcysjend;
	}

	public void setZgcysjend(String zgcysjend) {
		this.zgcysjend = zgcysjend;
	}

	public String getTxrqstart() {
		return txrqstart;
	}

	public void setTxrqstart(String txrqstart) {
		this.txrqstart = txrqstart;
	}

	public String getTxrqend() {
		return txrqend;
	}

	public void setTxrqend(String txrqend) {
		this.txrqend = txrqend;
	}

	public String getHzrzsjstart() {
		return hzrzsjstart;
	}

	public void setHzrzsjstart(String hzrzsjstart) {
		this.hzrzsjstart = hzrzsjstart;
	}

	public String getHzrzsjend() {
		return hzrzsjend;
	}

	public void setHzrzsjend(String hzrzsjend) {
		this.hzrzsjend = hzrzsjend;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
