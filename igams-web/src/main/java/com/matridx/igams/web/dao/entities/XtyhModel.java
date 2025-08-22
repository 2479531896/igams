package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="XtyhModel")
public class XtyhModel extends BaseModel{
	//用户id
	private String yhid;
	//用户名
	protected String yhm;
	//真实姓名
	private String zsxm;
	//密码
	protected String mm;
	//是否锁定
	protected String sfsd;
	//当前角色
	private String dqjs;
	//登录ip
	private String dlip;
	//登录时间
	private String dlsj;
	//错误次数
	private String cwcs;
	//钉钉ID
	private String ddid;
	//微信ID
	private String wechatid;
	//岗位名称
	private String gwmc;
	//密码强度flg
	private String mm_flg;
	//分布式标记
	private String prefix;
	//钉钉头像路径
	private String ddtxlj;

	//锁定时间
	private String sdtime;
	//解锁时间
	private String jstime;
	//分组
	private String grouping;

	//默认分组
	private String mrfz;
	//密码修改时间
	private String mmxgsj;
	//邮箱
	private String email;
	//用户在当前开发者企业账号范围内的唯一标识
	private String unionid;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getMrfz() {
		return mrfz;
	}

	public void setMrfz(String mrfz) {
		this.mrfz = mrfz;
	}

	public String getGrouping() {
		return grouping;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public String getSdtime() {
		return sdtime;
	}

	public void setSdtime(String sdtime) {
		this.sdtime = sdtime;
	}

	public String getJstime() {
		return jstime;
	}

	public void setJstime(String jstime) {
		this.jstime = jstime;
	}

	public String getDdtxlj() {
		return ddtxlj;
	}
	public void setDdtxlj(String ddtxlj) {
		this.ddtxlj = ddtxlj;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getMm_flg()
	{
		return mm_flg;
	}
	public void setMm_flg(String mm_flg)
	{
		this.mm_flg = mm_flg;
	}
	//用户id
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
	//用户名
	public String getYhm() {
		return yhm;
	}
	public void setYhm(String yhm){
		this.yhm = yhm;
	}
	//真实姓名
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm){
		this.zsxm = zsxm;
	}
	//密码
	public String getMm() {
		return mm;
	}
	public void setMm(String mm){
		this.mm = mm;
	}
	//是否锁定
	public String getSfsd() {
		return sfsd;
	}
	public void setSfsd(String sfsd){
		this.sfsd = sfsd;
	}
	//当前角色
	public String getDqjs() {
		return dqjs;
	}
	public void setDqjs(String dqjs){
		this.dqjs = dqjs;
	}
	//登录ip
	public String getDlip() {
		return dlip;
	}
	public void setDlip(String dlip){
		this.dlip = dlip;
	}
	//登录时间
	public String getDlsj() {
		return dlsj;
	}
	public void setDlsj(String dlsj){
		this.dlsj = dlsj;
	}
	//错误次数
	public String getCwcs() {
		return cwcs;
	}
	public void setCwcs(String cwcs){
		this.cwcs = cwcs;
	}
	//钉钉ID
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}
	//微信ID
	public String getWechatid() {
		return wechatid;
	}
	public void setWechatid(String wechatid) {
		this.wechatid = wechatid;
	}
	//岗位名称
	public String getGwmc() {
		return gwmc;
	}
	public void setGwmc(String gwmc) {
		this.gwmc = gwmc;
	}

	public String getMmxgsj() {
		return mmxgsj;
	}

	public void setMmxgsj(String mmxgsj) {
		this.mmxgsj = mmxgsj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
