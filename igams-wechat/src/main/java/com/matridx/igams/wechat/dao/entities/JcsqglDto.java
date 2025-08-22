package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="JcsqglDto")
public class JcsqglDto extends JcsqglModel{
	//申请类型名称
	private String sqlxmc;
	//录入人员名称
	private String lrrymc;
	//状态（多）[]
	private String[] zts;
	//申请类型（多）[]
	private String[] sqlxs;
	//类型（多）[]
	private String[] lxs;
	private String lrsjstart;
	private String lrsjend;
	private String sqmx_json;
	//附件IDS
	private List<String> fjids;
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
	//标记
	private String tab_flag;
	//查询条件(全部)
	private String entire;
	//申请类型代码
	private String sqlxdm;
	//业务类型
	private String ywlx;
	//标本编号list
	private List<String> ybbhs;
	//样本编号
	private String ybbh;
	//文库编码
	private String wkbm;
	//附件保存标记
	private String fjbcbj;

	private String sjid;
	//申请类型csdm
	private String sqlxcsdm;
	//限制标记
	private String xzbj;

	public String getXzbj() {
		return xzbj;
	}

	public void setXzbj(String xzbj) {
		this.xzbj = xzbj;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getWkbm() {
		return wkbm;
	}

	public void setWkbm(String wkbm) {
		this.wkbm = wkbm;
	}

	public List<String> getYbbhs() {
		return ybbhs;
	}

	public void setYbbhs(List<String> ybbhs) {
		this.ybbhs = ybbhs;
	}
	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getSqlxdm() {
		return sqlxdm;
	}

	public void setSqlxdm(String sqlxdm) {
		this.sqlxdm = sqlxdm;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getTab_flag() {
		return tab_flag;
	}

	public void setTab_flag(String tab_flag) {
		this.tab_flag = tab_flag;
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

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getSqmx_json() {
		return sqmx_json;
	}

	public void setSqmx_json(String sqmx_json) {
		this.sqmx_json = sqmx_json;
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

	public String[] getSqlxs() {
		return sqlxs;
	}

	public void setSqlxs(String[] sqlxs) {
		this.sqlxs = sqlxs;
		for(int i=0;i<this.sqlxs.length;i++)
		{
			this.sqlxs[i] = this.sqlxs[i].replace("'", "");
		}
	}

	public String[] getLxs() {
		return lxs;
	}

	public void setLxs(String[] lxs) {
		this.lxs = lxs;
		for(int i=0;i<this.lxs.length;i++)
		{
			this.lxs[i] = this.lxs[i].replace("'", "");
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

	public String getSqlxmc() {
		return sqlxmc;
	}

	public void setSqlxmc(String sqlxmc) {
		this.sqlxmc = sqlxmc;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getFjbcbj() {
		return fjbcbj;
	}

	public void setFjbcbj(String fjbcbj) {
		this.fjbcbj = fjbcbj;
	}

	public String getSjid() {
		return sjid;
	}

	public void setSjid(String sjid) {
		this.sjid = sjid;
	}

	public String getSqlxcsdm() {
		return sqlxcsdm;
	}

	public void setSqlxcsdm(String sqlxcsdm) {
		this.sqlxcsdm = sqlxcsdm;
	}
}
