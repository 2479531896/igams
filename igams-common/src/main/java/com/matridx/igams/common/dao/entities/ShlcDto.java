package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="ShlcDto")
public class ShlcDto extends ShlcModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//申请时间
	private String sqsj;
	//岗位名称
	private String gwmc;
	//过程ID 用于找出申请时间后确定相应的审核流程
	private String gcid;
	//是否为当前流程
	private boolean current;
	//是否为已审核流程
	private boolean audited = false;
	//审核流程名称
	private String lclbmc;
	//岗位IDlist
	private List<String> gwidlist;
	//审批岗位成员
	private List<SpgwcyDto> spgwcyDtos;
	//xx,xx,xx 审批岗位成员名称字符串
	private String spgwcymcs;
	//外部岗位
	private String wbgw;
	//info
	private String info;
	//绩效模板id
	private String jxmbid;
	//模板设置id
	private String mbszid;
	//权重
	private String qz;
	private String lclbcskz2;//流程类别参数扩展2 是否特殊页面
	private String lclbcskz3;//流程类别参数扩展3 钉钉回调类型参数代码
	private String ddhdlxcskz1;//钉钉审批processCode
	private String lclbcskz1;//特殊审核页面路径

	public String getLclbcskz1() {
		return lclbcskz1;
	}

	public void setLclbcskz1(String lclbcskz1) {
		this.lclbcskz1 = lclbcskz1;
	}

	public String getDdhdlxcskz1() {
		return ddhdlxcskz1;
	}

	public void setDdhdlxcskz1(String ddhdlxcskz1) {
		this.ddhdlxcskz1 = ddhdlxcskz1;
	}

	public String getLclbcskz2() {
		return lclbcskz2;
	}

	public void setLclbcskz2(String lclbcskz2) {
		this.lclbcskz2 = lclbcskz2;
	}

	public String getLclbcskz3() {
		return lclbcskz3;
	}

	public void setLclbcskz3(String lclbcskz3) {
		this.lclbcskz3 = lclbcskz3;
	}

	public String getMbszid() {
		return mbszid;
	}

	public void setMbszid(String mbszid) {
		this.mbszid = mbszid;
	}

	public String getQz() {
		return qz;
	}

	public void setQz(String qz) {
		this.qz = qz;
	}

	public String getJxmbid() {
		return jxmbid;
	}

	public void setJxmbid(String jxmbid) {
		this.jxmbid = jxmbid;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getWbgw() {
		return wbgw;
	}

	public void setWbgw(String wbgw) {
		this.wbgw = wbgw;
	}

	public String getSpgwcymcs() {
		return spgwcymcs;
	}

	public void setSpgwcymcs(String spgwcymcs) {
		this.spgwcymcs = spgwcymcs;
	}

	public List<SpgwcyDto> getSpgwcyDtos() {
		return spgwcyDtos;
	}

	public void setSpgwcyDtos(List<SpgwcyDto> spgwcyDtos) {
		this.spgwcyDtos = spgwcyDtos;
	}

	public List<String> getGwidlist() {
		return gwidlist;
	}
	public void setGwidlist(List<String> gwidlist) {
		this.gwidlist = gwidlist;
	}
	public String getLclbmc() {
		return lclbmc;
	}
	public void setLclbmc(String lclbmc) {
		this.lclbmc = lclbmc;
	}
	public String getSqsj() {
		return sqsj;
	}
	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}
	public String getGwmc() {
		return gwmc;
	}
	public void setGwmc(String gwmc) {
		this.gwmc = gwmc;
	}
	public String getGcid() {
		return gcid;
	}
	public void setGcid(String gcid) {
		this.gcid = gcid;
	}
	public boolean isCurrent() {
		return current;
	}
	public void setCurrent(boolean current) {
		this.current = current;
	}
	public boolean isAudited() {
		return audited;
	}
	public void setAudited(boolean audited) {
		this.audited = audited;
	}

}
