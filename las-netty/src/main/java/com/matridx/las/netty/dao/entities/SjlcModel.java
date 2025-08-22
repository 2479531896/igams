package com.matridx.las.netty.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SjlcModel")
public class SjlcModel {
	//流程id
	private String lcid;
	//序号
	private String xh;
	//是否占用机器人
	private String sfzyjqr;
	//是否占用仪器
	private String sfzyyq;
	//是否归还仪器
	private String sfghjqr;
	//是否归还机器人
	private String sfghyq;
	//是否循环
	private String sfxh;
	//事件id
	private String sjid;
	//占用的一起类型
	private String zyyqlx;
	//归还的一起类型
	private String ghyqlx;
	//流程类型
	private String lclx;
	//是否最后一个流程
	private String sfzhyg;
	//循环判断类型
	private String xhpdlx;
	//流程回调函数
	private String sjhd;
	//循环判断类型名
	private String pdzxcs;
	//参数类名
	private String classname;
	//事件是否执行类型
	private String checklx;
	//参数类型
	private String paramlx;
	//事件回调处理类型
	private String sjhtcllx;
	//循环判断参数
	private String xhpdcs;
	//参数
	private String param;
	//跟换EP管方法时机器人身上EP管载具位置的名称
	private String agvparam;
	//是否更换ep管
	private String sfghep;
	//是否一定占用新仪器
	private String sfxyq;
	//是否一定占用新机器人
	private String sfxjqr;
	//流程id
	public String getLcid() {
		return lcid;
	}
	public void setLcid(String lcid){
		this.lcid = lcid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//是否占用机器人
	public String getSfzyjqr() {
		return sfzyjqr;
	}
	public void setSfzyjqr(String sfzyjqr){
		this.sfzyjqr = sfzyjqr;
	}
	//是否占用仪器
	public String getSfzyyq() {
		return sfzyyq;
	}
	public void setSfzyyq(String sfzyyq){
		this.sfzyyq = sfzyyq;
	}
	//是否归还仪器
	public String getSfghjqr() {
		return sfghjqr;
	}
	public void setSfghjqr(String sfghjqr){
		this.sfghjqr = sfghjqr;
	}
	//是否归还机器人
	public String getSfghyq() {
		return sfghyq;
	}
	public void setSfghyq(String sfghyq){
		this.sfghyq = sfghyq;
	}
	//是否循环
	public String getSfxh() {
		return sfxh;
	}
	public void setSfxh(String sfxh){
		this.sfxh = sfxh;
	}
	//事件id
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//占用的一起类型
	public String getZyyqlx() {
		return zyyqlx;
	}
	public void setZyyqlx(String zyyqlx){
		this.zyyqlx = zyyqlx;
	}
	//归还的一起类型
	public String getGhyqlx() {
		return ghyqlx;
	}
	public void setGhyqlx(String ghyqlx){
		this.ghyqlx = ghyqlx;
	}
	//流程类型
	public String getLclx() {
		return lclx;
	}
	public void setLclx(String lclx){
		this.lclx = lclx;
	}

	public String getSfzhyg() {
		return sfzhyg;
	}

	public void setSfzhyg(String sfzhyg) {
		this.sfzhyg = sfzhyg;
	}

	public String getXhpdlx() {
		return xhpdlx;
	}

	public void setXhpdlx(String xhpdlx) {
		this.xhpdlx = xhpdlx;
	}

	public String getSjhd() {
		return sjhd;
	}

	public String getPdzxcs() {
		return pdzxcs;
	}

	public void setPdzxcs(String pdzxcs) {
		this.pdzxcs = pdzxcs;
	}

	public void setSjhd(String sjhd) {
		this.sjhd = sjhd;
	}
	
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	
	public String getChecklx() {
		return checklx;
	}
	public void setChecklx(String checklx) {
		this.checklx = checklx;
	}
	public String getParamlx() {
		return paramlx;
	}
	public void setParamlx(String paramlx) {
		this.paramlx = paramlx;
	}
	
	public String getSjhtcllx() {
		return sjhtcllx;
	}
	public void setSjhtcllx(String sjhtcllx) {
		this.sjhtcllx = sjhtcllx;
	}

	public String getXhpdcs() {
		return xhpdcs;
	}

	public void setXhpdcs(String xhpdcs) {
		this.xhpdcs = xhpdcs;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getAgvparam() {
		return agvparam;
	}

	public void setAgvparam(String agvparam) {
		this.agvparam = agvparam;
	}

	public String getSfghep() {
		return sfghep;
	}

	public void setSfghep(String sfghep) {
		this.sfghep = sfghep;
	}

	public String getSfxyq() {
		return sfxyq;
	}

	public void setSfxyq(String sfxyq) {
		this.sfxyq = sfxyq;
	}

	public String getSfxjqr() {
		return sfxjqr;
	}

	public void setSfxjqr(String sfxjqr) {
		this.sfxjqr = sfxjqr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
