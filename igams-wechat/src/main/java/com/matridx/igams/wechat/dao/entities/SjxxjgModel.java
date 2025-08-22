package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjxxjgModel")
public class SjxxjgModel extends BaseModel{
	//详细结果ID
	private String xxjgid;
	//检测类型
	private String jclx;
	//序号
	private String xh;
	//送检ID
	private String sjid;
	//当前节点ID
	private String jdid;
	//父节点ID
	private String fjdid;
	//物种英文名
	private String wzywm;
	//物种中文名
	private String wzzwm;
	//物种分类
	private String wzfl;
	//物种分类等级代码
	private String fldj;
	//该物种的读取数
	private String dqs;
	//属于该物种及其子节点的读取数
	private String zdqs;
	//每百万读数中该物种的读数
	private String dwds;
	//该物种读数占标本总读数的百分比
	private String dsbfb;
	//基因组覆盖度百分比
	private String jyzfgd;
	//该物种的属名
	private String sm;
	//属的读数
	private String sdds;
	//该物种在整张芯片中的检出频率
	private String xpjcl;
	//可能为：低关注，高关注，默认关注
	private String gzd;
	//相对丰度
	private String xdfd;
	//病毒类型
	private String bdlx;
	//细菌类型
	private String xjlx;
	//物种分类类型
	private String wzfllx;
	//属丰度
	private String sfd;
	//项目代码
	private String xmdm;
	//文库编号
	private String wkbh;
	//芯片名称
	private String xpmc;

	public String getXmdm() {
		return xmdm;
	}

	public void setXmdm(String xmdm) {
		this.xmdm = xmdm;
	}

	public String getSfd()
	{
		return sfd;
	}
	public void setSfd(String sfd)
	{
		this.sfd = sfd;
	}
	//物种分类类型
	public String getWzfllx() {
		return wzfllx;
	}
	public void setWzfllx(String wzfllx) {
		this.wzfllx = wzfllx;
	}
	//详细结果ID
	public String getXxjgid() {
		return xxjgid;
	}
	public void setXxjgid(String xxjgid){
		this.xxjgid = xxjgid;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//当前节点ID
	public String getJdid() {
		return jdid;
	}
	public void setJdid(String jdid){
		this.jdid = jdid;
	}
	//父节点ID
	public String getFjdid() {
		return fjdid;
	}
	public void setFjdid(String fjdid){
		this.fjdid = fjdid;
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
	//物种分类等级代码
	public String getFldj() {
		return fldj;
	}
	public void setFldj(String fldj) {
		this.fldj = fldj;
	}
	//该物种的读取数
	public String getDqs() {
		return dqs;
	}
	public void setDqs(String dqs){
		this.dqs = dqs;
	}
	//属于该物种及其子节点的读取数
	public String getZdqs() {
		return zdqs;
	}
	public void setZdqs(String zdqs){
		this.zdqs = zdqs;
	}
	//每百万读数中该物种的读数
	public String getDwds() {
		return dwds;
	}
	public void setDwds(String dwds){
		this.dwds = dwds;
	}
	//该物种读数占标本总读数的百分比
	public String getDsbfb() {
		return dsbfb;
	}
	public void setDsbfb(String dsbfb){
		this.dsbfb = dsbfb;
	}
	//基因组覆盖度百分比
	public String getJyzfgd() {
		return jyzfgd;
	}
	public void setJyzfgd(String jyzfgd){
		this.jyzfgd = jyzfgd;
	}
	//该物种的属名
	public String getSm() {
		return sm;
	}
	public void setSm(String sm){
		this.sm = sm;
	}
	//属的读数
	public String getSdds() {
		return sdds;
	}
	public void setSdds(String sdds){
		this.sdds = sdds;
	}
	//该物种在整张芯片中的检出频率
	public String getXpjcl() {
		return xpjcl;
	}
	public void setXpjcl(String xpjcl){
		this.xpjcl = xpjcl;
	}
	//可能为：低关注，高关注，默认关注
	public String getGzd() {
		return gzd;
	}
	public void setGzd(String gzd){
		this.gzd = gzd;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	//相对丰度
	public String getXdfd() {
		return xdfd;
	}
	public void setXdfd(String xdfd) {
		this.xdfd = xdfd;
	}
	//病毒类型
	public String getBdlx() {
		return bdlx;
	}
	public void setBdlx(String bdlx) {
		this.bdlx = bdlx;
	}
	//细菌类型
	public String getXjlx() {
		return xjlx;
	}
	public void setXjlx(String xjlx) {
		this.xjlx = xjlx;
	}
	public String getJclx() {
		return jclx;
	}
	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getXpmc() {
		return xpmc;
	}

	public void setXpmc(String xpmc) {
		this.xpmc = xpmc;
	}
}
