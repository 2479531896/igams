package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.ShxxDto;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="FksqDto")
public class FksqDto extends FksqModel{

	//申请人名称
	private String sqrmc;
	//申请部门名称
	private String sqbmmc;
	//费用归属名称
	private String fygsmc;
	//付款方名称
	private String fkfmc;
	//付款方式名称
	private String fkfsmc;
	//钉钉审批人用户id
	private String sprid;
	//钉钉审批人用户姓名
	private String sprxm;
	//钉钉审批人钉钉ID
	private String sprddid;
	//钉钉审批人用户名
	private String spryhm;
	//钉钉审批人角色ID
	private String sprjsid;
	//钉钉审批人角色名称
	private String sprjsmc;
	//支付对象名称
	private String zfdxmc;
	//附件IDS
	private List<String> fjids;

	private String lrsjstart;//录入开始时间起
	private String lrsjend;//录入开始时间止
	private String[] zts;//状态
	private String[] fygss;//费用归属
	private String[] manyfkf;//付款方
	private String[] fkfss;//付款方式

	private List<ShxxDto> shxxDtos;//审核数据
	private String bcbj;//保存标记

	public String getBcbj() {
		return bcbj;
	}

	public void setBcbj(String bcbj) {
		this.bcbj = bcbj;
	}

	public List<ShxxDto> getShxxDtos() {
		return shxxDtos;
	}

	public void setShxxDtos(List<ShxxDto> shxxDtos) {
		this.shxxDtos = shxxDtos;
	}

	public String[] getFygss() {
		return fygss;
	}

	public void setFygss(String[] fygss) {
		this.fygss = fygss;
		for(int i=0;i<this.fygss.length;i++)
		{
			this.fygss[i] = this.fygss[i].replace("'", "");
		}
	}

	public String[] getManyfkf() {
		return manyfkf;
	}

	public void setManyfkf(String[] manyfkf) {
		this.manyfkf = manyfkf;
		for(int i=0;i<this.manyfkf.length;i++)
		{
			this.manyfkf[i] = this.manyfkf[i].replace("'", "");
		}
	}

	public String[] getFkfss() {
		return fkfss;
	}

	public void setFkfss(String[] fkfss) {
		this.fkfss = fkfss;
		for(int i=0;i<this.fkfss.length;i++)
		{
			this.fkfss[i] = this.fkfss[i].replace("'", "");
		}
	}

	public String getLrsjstart() {
		return lrsjstart;
	}

	public void setLrsjstart(String lrsjstart) {
		this.lrsjstart = lrsjstart;
	}

	public String getLrsjend() {
		return lrsjend;
	}

	public void setLrsjend(String lrsjend) {
		this.lrsjend = lrsjend;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for(int i=0;i<this.zts.length;i++)
		{
			this.zts[i] = this.zts[i].replace("'", "");
		}
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getZfdxmc() {
		return zfdxmc;
	}

	public void setZfdxmc(String zfdxmc) {
		this.zfdxmc = zfdxmc;
	}

	public String getSprid() {
		return sprid;
	}

	public void setSprid(String sprid) {
		this.sprid = sprid;
	}

	public String getSprxm() {
		return sprxm;
	}

	public void setSprxm(String sprxm) {
		this.sprxm = sprxm;
	}

	public String getSprddid() {
		return sprddid;
	}

	public void setSprddid(String sprddid) {
		this.sprddid = sprddid;
	}

	public String getSpryhm() {
		return spryhm;
	}

	public void setSpryhm(String spryhm) {
		this.spryhm = spryhm;
	}

	public String getSprjsid() {
		return sprjsid;
	}

	public void setSprjsid(String sprjsid) {
		this.sprjsid = sprjsid;
	}

	public String getSprjsmc() {
		return sprjsmc;
	}

	public void setSprjsmc(String sprjsmc) {
		this.sprjsmc = sprjsmc;
	}

	public String getSqrmc() {
		return sqrmc;
	}

	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getFygsmc() {
		return fygsmc;
	}

	public void setFygsmc(String fygsmc) {
		this.fygsmc = fygsmc;
	}

	public String getFkfmc() {
		return fkfmc;
	}

	public void setFkfmc(String fkfmc) {
		this.fkfmc = fkfmc;
	}

	public String getFkfsmc() {
		return fkfsmc;
	}

	public void setFkfsmc(String fkfsmc) {
		this.fkfsmc = fkfsmc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
