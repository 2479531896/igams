package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjwzxxModel")
public class SjwzxxModel extends BaseModel{
	//送检物种ID
	private String sjwzid;
	//送检ID
	private String sjid;
	//物种ID
	private String wzid;
	//物种英文名
	private String wzywm;
	//物种中文名
	private String wzzwm;
	//物种分类
	private String wzfl;
	//结果类型  现分为高关注度和疑似
	private String jglx;
	//序号
	private String xh;
	//读取数
	private String dqs;
	//基因组覆盖度
	private String jyzfgd;
	//芯片信息
	private String xpxx;
	//相对丰度
	private String xdfd;
	//属ID
	private String sid;
	//属名
	private String sm;
	//属中文名
	private String szwm;
	//属的读数
	private String sdds;
	//属丰度
	private String sfd;
	//细菌类型
	private String xjlx;
	//病毒类型
	private String bdlx;
	//检测类型
	private String jclx;
	//检测子类型
	private String jczlx;
	//物种分类类型
	private String wzfllx;
	//文献id
	private String wxid;
	
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	//物种分类类型
	public String getWzfllx() {
		return wzfllx;
	}
	public void setWzfllx(String wzfllx) {
		this.wzfllx = wzfllx;
	}
	public String getJclx() {
		return jclx;
	}
	public void setJclx(String jclx) {
		this.jclx = jclx;
	}
	//送检物种ID
	public String getSjwzid() {
		return sjwzid;
	}
	public void setSjwzid(String sjwzid){
		this.sjwzid = sjwzid;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//物种ID
	public String getWzid() {
		return wzid;
	}
	public void setWzid(String wzid){
		this.wzid = wzid;
	}
	//物种英文名
	public String getWzywm() {
		return wzywm;
	}
	public void setWzywm(String wzywm){
		this.wzywm = wzywm;
	}
	//物种中文名
	public String getWzzwm() {
		return wzzwm;
	}
	public void setWzzwm(String wzzwm){
		this.wzzwm = wzzwm;
	}
	//物种分类
	public String getWzfl() {
		return wzfl;
	}
	public void setWzfl(String wzfl){
		this.wzfl = wzfl;
	}
	//结果类型  现分为高关注度和疑似
	public String getJglx() {
		return jglx;
	}
	public void setJglx(String jglx){
		this.jglx = jglx;
	}
	//读取数
	public String getDqs() {
		return dqs;
	}
	public void setDqs(String dqs){
		this.dqs = dqs;
	}
	//基因组覆盖度
	public String getJyzfgd() {
		return jyzfgd;
	}
	public void setJyzfgd(String jyzfgd){
		this.jyzfgd = jyzfgd;
	}
	//芯片信息
	public String getXpxx() {
		return xpxx;
	}
	public void setXpxx(String xpxx){
		this.xpxx = xpxx;
	}
	//相对丰度
	public String getXdfd() {
		return xdfd;
	}
	public void setXdfd(String xdfd){
		this.xdfd = xdfd;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSm() {
		return sm;
	}
	public void setSm(String sm) {
		this.sm = sm;
	}
	public String getSzwm() {
		return szwm;
	}
	public void setSzwm(String szwm) {
		this.szwm = szwm;
	}
	public String getSdds() {
		return sdds;
	}
	public void setSdds(String sdds) {
		this.sdds = sdds;
	}
	public String getSfd() {
		return sfd;
	}
	public void setSfd(String sfd) {
		this.sfd = sfd;
	}
	public String getXjlx() {
		return xjlx;
	}
	public void setXjlx(String xjlx) {
		this.xjlx = xjlx;
	}
	public String getBdlx() {
		return bdlx;
	}
	public void setBdlx(String bdlx) {
		this.bdlx = bdlx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public String getJczlx() {
        return jczlx;
    }

    public void setJczlx(String jczlx) {
        this.jczlx = jczlx;
    }
}
