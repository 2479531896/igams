package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XmglModel")
public class XmglModel extends BaseModel{
	//项目ID
	private String xmid;
	//父项目ID
	private String fxmid;
	//调查名称 用于调查阶段
	private String dcmc;
	//项目代码
	private String xmdm;
	//模板ID
	private String mbid;
	//项目图标
	private String xmtb;
	//项目名称 正式立项后的名称
	private String xmmc;
	//项目类别   普通项目，文件夹
	private String xmlb;
	//项目类型   试剂
	private String xmlx;
	//开始时间 立项时间
	private String kssj;
	//当前进度  研发阶段表的jdid
	private String dqjd;
	//项目分组 基础数据
	private String xmfz;
	//项目公开性 基础数据
	private String xmgkx;
	//任务创建模式  1：卡片模式  2：弹窗模式
	private String cjms;
	//期望完成时间
	private String qwwcsj;
	//实际完成时间
	private String sjwcsj;
	//状态
	private String zt;
	//备注
	private String bz;
	//项目颜色
	private String xmys;
	//项目ID
	public String getXmid() {
		return xmid;
	}
	public void setXmid(String xmid){
		this.xmid = xmid;
	}
	//父项目ID
	public String getFxmid() {
		return fxmid;
	}
	public void setFxmid(String fxmid){
		this.fxmid = fxmid;
	}
	//调查名称 用于调查阶段
	public String getDcmc() {
		return dcmc;
	}
	public void setDcmc(String dcmc){
		this.dcmc = dcmc;
	}
	//项目代码
	public String getXmdm() {
		return xmdm;
	}
	public void setXmdm(String xmdm){
		this.xmdm = xmdm;
	}
	//模板ID
	public String getMbid() {
		return mbid;
	}
	public void setMbid(String mbid){
		this.mbid = mbid;
	}
	//项目图标
	public String getXmtb() {
		return xmtb;
	}
	public void setXmtb(String xmtb){
		this.xmtb = xmtb;
	}
	//项目名称 正式立项后的名称
	public String getXmmc() {
		return xmmc;
	}
	public void setXmmc(String xmmc){
		this.xmmc = xmmc;
	}
	//项目类别   普通项目，文件夹
	public String getXmlb() {
		return xmlb;
	}
	public void setXmlb(String xmlb){
		this.xmlb = xmlb;
	}
	//项目类型   试剂
	public String getXmlx() {
		return xmlx;
	}
	public void setXmlx(String xmlx){
		this.xmlx = xmlx;
	}
	//开始时间 立项时间
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj){
		this.kssj = kssj;
	}
	//当前进度  研发阶段表的jdid
	public String getDqjd() {
		return dqjd;
	}
	public void setDqjd(String dqjd){
		this.dqjd = dqjd;
	}
	//项目分组 基础数据
	public String getXmfz() {
		return xmfz;
	}
	public void setXmfz(String xmfz){
		this.xmfz = xmfz;
	}
	//项目公开性 基础数据
	public String getXmgkx() {
		return xmgkx;
	}
	public void setXmgkx(String xmgkx){
		this.xmgkx = xmgkx;
	}
	//任务创建模式  1：卡片模式  2：弹窗模式
	public String getCjms() {
		return cjms;
	}
	public void setCjms(String cjms){
		this.cjms = cjms;
	}
	//期望完成时间
	public String getQwwcsj() {
		return qwwcsj;
	}
	public void setQwwcsj(String qwwcsj){
		this.qwwcsj = qwwcsj;
	}
	//实际完成时间
	public String getSjwcsj() {
		return sjwcsj;
	}
	public void setSjwcsj(String sjwcsj){
		this.sjwcsj = sjwcsj;
	}
	//状态
	public String getZt() {
		return zt;
	}
	public void setZt(String zt){
		this.zt = zt;
	}
	//备注
	public String getBz() {
		return bz;
	}
	public void setBz(String bz){
		this.bz = bz;
	}
	public String getXmys() {
		return xmys;
	}
	public void setXmys(String xmys) {
		this.xmys = xmys;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
