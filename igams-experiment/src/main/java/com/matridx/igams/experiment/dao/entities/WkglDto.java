package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="WkglDto")
public class WkglDto extends WkglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//录入人员名称
	private String lrrymc;
	//修改人员名称
	private String xgrymc;
	//ids
	private List<String> ids;
	//检测单位名称
	private String jcdwmc;
	//检测单位代码
	private String jcdwdm;
	//下载标记
	private String exprot_flg;
	//格式化的录入时间（yyyy-MM-dd）
	private String format_lrsj;
	//检测单位限制
	private List<String> jcdwxz;
	//当天建立文库数量
	private String count;
	//创建时间开始时间
	private String cjsjstart;
	//创建时间结束时间
	private String cjsjend;
	//芯片名称
	private String xpmc;
	//上机时间
	private String sjsj;
	//下机时间
	private String xjsj;
	//测序仪名
	private String cxy;
	//服务器
	private String fwq;
	//实验室
	private String sys;
	//预计下机时间
	private String yjxjsj;
	//文库上机表上传时间
	private String sjscsj;
	//内部编码
	private String nbbh;
	//pooling模板id
	private String mbid;
	//数据总量
	private String sjzl;
	private String timezone;
	private List<String> fjids;

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getSjzl() {
		return sjzl;
	}

	public void setSjzl(String sjzl) {
		this.sjzl = sjzl;
	}

	public String getMbid() {
		return mbid;
	}

	public void setMbid(String mbid) {
		this.mbid = mbid;
	}

	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	public String getSjscsj() {
		return sjscsj;
	}

	public void setSjscsj(String sjscsj) {
		this.sjscsj = sjscsj;
	}

	public String getXpmc() {
		return xpmc;
	}

	public void setXpmc(String xpmc) {
		this.xpmc = xpmc;
	}

	public String getSjsj() {
		return sjsj;
	}

	public void setSjsj(String sjsj) {
		this.sjsj = sjsj;
	}

	public String getXjsj() {
		return xjsj;
	}

	public void setXjsj(String xjsj) {
		this.xjsj = xjsj;
	}

	public String getCxy() {
		return cxy;
	}

	public void setCxy(String cxy) {
		this.cxy = cxy;
	}

	public String getFwq() {
		return fwq;
	}

	public void setFwq(String fwq) {
		this.fwq = fwq;
	}

	public String getSys() {
		return sys;
	}

	public void setSys(String sys) {
		this.sys = sys;
	}

	public String getYjxjsj() {
		return yjxjsj;
	}

	public void setYjxjsj(String yjxjsj) {
		this.yjxjsj = yjxjsj;
	}

	public String getCjsjstart() {
		return cjsjstart;
	}
	public void setCjsjstart(String cjsjstart) {
		this.cjsjstart = cjsjstart;
	}
	public String getCjsjend() {
		return cjsjend;
	}
	public void setCjsjend(String cjsjend) {
		this.cjsjend = cjsjend;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public List<String> getJcdwxz() {
		return jcdwxz;
	}
	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
	}
	public String getFormat_lrsj()
	{
		return format_lrsj;
	}
	public void setFormat_lrsj(String format_lrsj)
	{
		this.format_lrsj = format_lrsj;
	}
	public String getExprot_flg(){
		return exprot_flg;
	}
	public void setExprot_flg(String exprot_flg)
	{
		this.exprot_flg = exprot_flg;
	}
	public String getJcdwdm() {
		return jcdwdm;
	}
	public void setJcdwdm(String jcdwdm) {
		this.jcdwdm = jcdwdm;
	}
	public String getJcdwmc() {
		return jcdwmc;
	}
	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public String getLrrymc() {
		return lrrymc;
	}
	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}
	public String getXgrymc() {
		return xgrymc;
	}
	public void setXgrymc(String xgrymc) {
		this.xgrymc = xgrymc;
	}
	
	
}
