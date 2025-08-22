package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YhsjxxjgModel")
public class YhsjxxjgModel extends BaseModel{
	//详细结果ID
	private String xxjgid;
	//序号
	private String xh;
	//送检ID
	private String sjid;
	//系统用户ID
	private String yhid;
	//检测类型   分D  和R ，C
	private String jclx;
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
	//相对丰度
	private String xdfd;
	//该物种的属名
	private String sm;
	//属的读数
	private String sdds;
	//属丰度
	private String sfd;
	//细菌类型 gram_stain
	private String xjlx;
	//病毒类型 virus_type
	private String bdlx;
	//该物种在整张芯片中的检出频率
	private String xpjcl;
	//可能为：低关注，高关注，默认关注
	private String gzd;
	//物种分类类型  用于合并 细菌类型 gram_stain 病毒类型 virus_type
	private String wzfllx;
	//关联类型
	private String gllx;
	//状态
	private String zt;
	
	public String getGllx() {
		return gllx;
	}
	public void setGllx(String gllx) {
		this.gllx = gllx;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	//详细结果ID
	public String getXxjgid() {
		return xxjgid;
	}
	public void setXxjgid(String xxjgid){
		this.xxjgid = xxjgid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//系统用户ID
	public String getYhid() {
		return yhid;
	}
	public void setYhid(String yhid){
		this.yhid = yhid;
	}
	//检测类型   分D  和R ，C
	public String getJclx() {
		return jclx;
	}
	public void setJclx(String jclx){
		this.jclx = jclx;
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
	public void setFldj(String fldj){
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
	//相对丰度
	public String getXdfd() {
		return xdfd;
	}
	public void setXdfd(String xdfd){
		this.xdfd = xdfd;
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
	//属丰度
	public String getSfd() {
		return sfd;
	}
	public void setSfd(String sfd){
		this.sfd = sfd;
	}
	//细菌类型 gram_stain
	public String getXjlx() {
		return xjlx;
	}
	public void setXjlx(String xjlx){
		this.xjlx = xjlx;
	}
	//病毒类型 virus_type
	public String getBdlx() {
		return bdlx;
	}
	public void setBdlx(String bdlx){
		this.bdlx = bdlx;
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
	//物种分类类型  用于合并 细菌类型 gram_stain 病毒类型 virus_type
	public String getWzfllx() {
		return wzfllx;
	}
	public void setWzfllx(String wzfllx){
		this.wzfllx = wzfllx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
