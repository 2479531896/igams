package com.matridx.server.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;


@Alias(value="QgglDto")
public class QgglDto extends QgglModel{

	//申请人名称
	private String sqrmc;
	//申请部门名称
	private String sqbmmc;
	//支付方式名称
	private String zffsmc;
	//付款方名称
	private String fkfmc;
	//录入人员名称
	private String lrrymc;
	//支付方式[多]
	private String[] zffss;
	//付款方[多]
	private String[] fkfs;
	//状态[多]
	private String[] zts;
	//申请日期开始时间
	private String sqrqstart;
	//申请日期结束时间
	private String sqrqend;
	//部门代码(U8数据库保存此字段)
	private String sqbmdm;
	//项目编码名称
	private String xmbmmc;
	//项目大类名称
	private String xmdlmc;
	//项目编码[多]
	private String[] xmbms;
	//项目大类[多]
	private String[] xmdls;
	//采购明细list
	private List<QgmxDto> qgmxlist;
	//采购明细JSON数据
	private String qgmx_json;
	//项目名称
	private String xmmc;
	//项目编码代码
	private String xmbmdm;
	//项目大类代码
	private String xmdldm;
	//申请人钉钉ID
	private String ddid;
	//访问标记(判断是否为外部接口)
	private String fwbj;
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
	//附件IDS
	private List<String> fjids;
	//业务ID
	private String ywlx;
	//物料ID，用于页面传递
	private String wlid;
	//保存标记,用于跳过后台验证
	private String bcbj;
	//请购明细ID
	private String qgmxid;
	//查看标记,用于附件查看页面控制显示内容
	private String ckbj;
	//审核人标记，用于判断是否更新U8审核人信息
	private String shrgxbj;
	//更新标记，判断审核回调是否需要更新本地以及U8数据(请购审核调用审核查看页面不需要更新)
	private String gxbj;
	//用于判断是修改还是复制操作
	private String flg;
	//请购类别代码
	private String qglbdm;
	//请购类别名称
	private String qglbmc;
	//是否报销s
	private String[] sfbxs;

	public String[] getSfbxs() {
		return sfbxs;
	}

	public void setSfbxs(String[] sfbxs) {
		this.sfbxs = sfbxs;
		for(int i=0;i<this.sfbxs.length;i++)
		{
			this.sfbxs[i] = this.sfbxs[i].replace("'", "");
		}
	}

	public String getQglbdm() {
		return qglbdm;
	}

	public void setQglbdm(String qglbdm) {
		this.qglbdm = qglbdm;
	}

	public String getQglbmc() {
		return qglbmc;
	}

	public void setQglbmc(String qglbmc) {
		this.qglbmc = qglbmc;
	}

	public String getGxbj() {
		return gxbj;
	}

	public void setGxbj(String gxbj) {
		this.gxbj = gxbj;
	}

	public String getShrgxbj() {
		return shrgxbj;
	}

	public void setShrgxbj(String shrgxbj) {
		this.shrgxbj = shrgxbj;
	}

	public String getCkbj() {
		return ckbj;
	}

	public void setCkbj(String ckbj) {
		this.ckbj = ckbj;
	}

	public String getQgmxid() {
		return qgmxid;
	}

	public void setQgmxid(String qgmxid) {
		this.qgmxid = qgmxid;
	}

	public String getBcbj() {
		return bcbj;
	}

	public void setBcbj(String bcbj) {
		this.bcbj = bcbj;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
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

	public String getSpryhm() {
		return spryhm;
	}

	public void setSpryhm(String spryhm) {
		this.spryhm = spryhm;
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

	public String getFwbj() {
		return fwbj;
	}

	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}

	public String getDdid() {
		return ddid;
	}

	public void setDdid(String ddid) {
		this.ddid = ddid;
	}

	public String getXmbmdm() {
		return xmbmdm;
	}

	public void setXmbmdm(String xmbmdm) {
		this.xmbmdm = xmbmdm;
	}

	public String getXmdldm() {
		return xmdldm;
	}

	public void setXmdldm(String xmdldm) {
		this.xmdldm = xmdldm;
	}

	public String getSqbmdm() {
		return sqbmdm;
	}

	public void setSqbmdm(String sqbmdm) {
		this.sqbmdm = sqbmdm;
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

	public String getZffsmc() {
		return zffsmc;
	}

	public void setZffsmc(String zffsmc) {
		this.zffsmc = zffsmc;
	}

	public String getFkfmc() {
		return fkfmc;
	}

	public void setFkfmc(String fkfmc) {
		this.fkfmc = fkfmc;
	}
	
	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String[] getZffss() {
		return zffss;
	}

	public void setZffss(String[] zffss) {
		this.zffss = zffss;
		for(int i=0;i<this.zffss.length;i++)
		{
			this.zffss[i] = this.zffss[i].replace("'", "");
		}
	}

	public String[] getFkfs() {
		return fkfs;
	}

	public void setFkfs(String[] fkfs) {
		this.fkfs = fkfs;
		for(int i=0;i<this.fkfs.length;i++)
		{
			this.fkfs[i] = this.fkfs[i].replace("'", "");
		}
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

	public String getSqrqstart() {
		return sqrqstart;
	}

	public void setSqrqstart(String sqrqstart) {
		this.sqrqstart = sqrqstart;
	}

	public String getSqrqend() {
		return sqrqend;
	}

	public void setSqrqend(String sqrqend) {
		this.sqrqend = sqrqend;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}


	public String getXmbmmc() {
		return xmbmmc;
	}

	public void setXmbmmc(String xmbmmc) {
		this.xmbmmc = xmbmmc;
	}

	public String getXmdlmc() {
		return xmdlmc;
	}

	public void setXmdlmc(String xmdlmc) {
		this.xmdlmc = xmdlmc;
	}

	public String[] getXmbms() {
		return xmbms;
	}

	public void setXmbms(String[] xmbms) {
		this.xmbms = xmbms;
		for(int i=0;i<this.xmbms.length;i++)
		{
			this.xmbms[i] = this.xmbms[i].replace("'", "");
		}
	}

	public String[] getXmdls() {
		return xmdls;
	}

	public void setXmdls(String[] xmdls) {
		this.xmdls = xmdls;
		for(int i=0;i<this.xmdls.length;i++)
		{
			this.xmdls[i] = this.xmdls[i].replace("'", "");
		}
	}


	public List<QgmxDto> getQgmxlist() {
		return qgmxlist;
	}

	public void setQgmxlist(List<QgmxDto> qgmxlist) {
		this.qgmxlist = qgmxlist;
	}

	public String getQgmx_json() {
		return qgmx_json;
	}

	public void setQgmx_json(String qgmx_json) {
		this.qgmx_json = qgmx_json;
	}

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
