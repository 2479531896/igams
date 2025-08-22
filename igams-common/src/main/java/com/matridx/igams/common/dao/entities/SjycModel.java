package com.matridx.igams.common.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="SjycModel")
public class SjycModel extends BaseModel{
	//异常ID
	private String ycid;
	//业务ID
	private String ywid;
	//类型 基础数据 主要为收样阶段，检测阶段
	private String lx;
	//子类型 基础数据 为二级类型
	private String zlx;
	//提出人
	private String twr;
	//异常信息
	private String ycxx;
	//确认类型
	private String qrlx;
	//确认人
	private String qrr;
	//是否结束  0:否 1：是
	private String sfjs;
	//标题
	private String ycbt;
	//最后回复时间
	private String zhhfsj;//最后回复时间
	//最后回复内容
	private String zhhfnr;//最后回复内容
	private String zfjl;//转发记录
		//转发时间
	private String zfsj;
	//异常区分
	private String ycqf;
	//提问人名称
	private String twrmc;
	//异常子区分
	private String yczqf;
	//检测单位
	private String jcdw;
	//结束人员
	private String jsry;
	//结束时间
	private String jssj;
	//提问人类型
	private String twrlx;
	//客户名称
	private String khmc;
	//投诉标记
	private String tsbj;
	//日期
	private String rq;

	public String getRq() {
		return rq;
	}

	public void setRq(String rq) {
		this.rq = rq;
	}

	public String getTsbj() {
		return tsbj;
	}

	public void setTsbj(String tsbj) {
		this.tsbj = tsbj;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}


	public String getJsry() {
		return jsry;
	}

	public void setJsry(String jsry) {
		this.jsry = jsry;
	}

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getYczqf() {
		return yczqf;
	}

	public void setYczqf(String yczqf) {
		this.yczqf = yczqf;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public String getTwrmc() {
		return twrmc;
	}

	public void setTwrmc(String twrmc) {
		this.twrmc = twrmc;
	}

	public String getZfsj() {
		return zfsj;
	}

	public void setZfsj(String zfsj) {
		this.zfsj = zfsj;
	}

	public String getZfjl() {
		return zfjl;
	}

	public void setZfjl(String zfjl) {
		this.zfjl = zfjl;
	}


	public void setZhhfsj(String zhhfsj) {
		this.zhhfsj = zhhfsj;
	}

	public String getZhhfnr() {
		return zhhfnr;
	}

	public void setZhhfnr(String zhhfnr) {
		this.zhhfnr = zhhfnr;
	}


	public String getZhhfsj() {
		return zhhfsj;
	}


	//标题
	public String getYcbt() {
		return ycbt;
	}
	public void setYcbt(String ycbt) {
		this.ycbt = ycbt;
	}
	//异常ID
	public String getYcid() {
		return ycid;
	}
	public void setYcid(String ycid){
		this.ycid = ycid;
	}
	//类型 基础数据 主要为收样阶段，检测阶段
	public String getLx() {
		return lx;
	}
	public void setLx(String lx){
		this.lx = lx;
	}
	//子类型 基础数据 为二级类型
	public String getZlx() {
		return zlx;
	}
	public void setZlx(String zlx){
		this.zlx = zlx;
	}
	//提出人
	public String getTwr() {
		return twr;
	}
	public void setTwr(String twr){
		this.twr = twr;
	}
	//异常信息
	public String getYcxx() {
		return ycxx;
	}
	public void setYcxx(String ycxx){
		this.ycxx = ycxx;
	}
	//确认类型
	public String getQrlx() {
		return qrlx;
	}
	public void setQrlx(String qrlx) {
		this.qrlx = qrlx;
	}
	//确认人
	public String getQrr() {
		return qrr;
	}
	public void setQrr(String qrr){
		this.qrr = qrr;
	}
	//是否结束  0:否 1：是
	public String getSfjs() {
		return sfjs;
	}
	public void setSfjs(String sfjs){
		this.sfjs = sfjs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getYcqf() {
		return ycqf;
	}

	public void setYcqf(String ycqf) {
		this.ycqf = ycqf;
	}

	public String getTwrlx() {
		return twrlx;
	}

	public void setTwrlx(String twrlx) {
		this.twrlx = twrlx;
	}
}
