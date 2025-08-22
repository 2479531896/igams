package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.JcsjDto;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="HbsfbzDto")
public class HbsfbzDto extends HbsfbzModel{
	//项目名称
	private String csmc;
	//项目id
	private String csid;
	//合作伙伴
	private String hbmc;
	private String xmmc;
	private String zxmmc;
	private List<String> ids;

	List<JcsjDto> jczxms;
	private String xmcskz1;
	private String xmcskz3;
	//合同编号
	private String htbh;
	//同步标记，判断新增还是修改,1代表新增
	private String insertFlag;

	private String jcxmid;

	private String jczxmid;

	public String getInsertFlag() {
		return insertFlag;
	}

	public void setInsertFlag(String insertFlag) {
		this.insertFlag = insertFlag;
	}

	public String getHtbh() {
		return htbh;
	}

	public void setHtbh(String htbh) {
		this.htbh = htbh;
	}

	public String getXmcskz1() {
		return xmcskz1;
	}

	public void setXmcskz1(String xmcskz1) {
		this.xmcskz1 = xmcskz1;
	}

	public String getXmcskz3() {
		return xmcskz3;
	}

	public void setXmcskz3(String xmcskz3) {
		this.xmcskz3 = xmcskz3;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getZxmmc() {
		return zxmmc;
	}

	public void setZxmmc(String zxmmc) {
		this.zxmmc = zxmmc;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public List<JcsjDto> getJczxms() {
		return jczxms;
	}

	public void setJczxms(List<JcsjDto> jczxms) {
		this.jczxms = jczxms;
	}

	public String getCsmc()
	{
		return csmc;
	}

	public void setCsmc(String csmc)
	{
		this.csmc = csmc;
	}

	public String getCsid()
	{
		return csid;
	}

	public void setCsid(String csid)
	{
		this.csid = csid;
	}

	public String getHbmc() {
		return hbmc;
	}

	public void setHbmc(String hbmc) {
		this.hbmc = hbmc;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getJczxmid() {
		return jczxmid;
	}

	public void setJczxmid(String jczxmid) {
		this.jczxmid = jczxmid;
	}

	public String getJcxmid() {
		return jcxmid;
	}

	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}
}
