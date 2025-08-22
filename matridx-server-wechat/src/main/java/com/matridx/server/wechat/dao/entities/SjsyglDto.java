package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;


@Alias(value="SjsyglDto")
public class SjsyglDto extends SjsyglModel{

	//检测项目名称
	private String jcxmmc;
	//检测类型名称
	private String jclxmc;

	private String xmsyglid;

	//检测类型代码
	private String jclxdm;
	//检测单位名称
	private String jcdwmc;
	//代表
	private String db;
	private String yjcdw;
	//患者姓名
	private String hzxm;
	//样本编号
	private String ybbh;
	//样本类型代码
	private String yblxdm;
	//扩展参数5
	private String kzcs5;
	//扩展参数6
	private String kzcs6;
	//伙伴ID
	private String hbid;
	//检测子项目名称
	private String jczxmmc;
	private String json;
	//排序
	private String px;
	//项目名称别名
	private String jcxmbm;
	//业务类型，保存xmsygl的送检 复检信息
	private String ywlx;

	private String jcxmcskz3;

	private String xzlx;

	public void setJcxmbm(String jcxmbm) {
		this.jcxmbm = jcxmbm;
	}

	public String getJcxmcskz3() {
		return jcxmcskz3;
	}

	public void setJcxmcskz3(String jcxmcskz3) {
		this.jcxmcskz3 = jcxmcskz3;
	}

	public String getXzlx() {
		return xzlx;
	}

	public void setXzlx(String xzlx) {
		this.xzlx = xzlx;
	}

	public String getJcxmbm() {
		return jcxmbm;
	}

	public void getJcxmbm(String jcxmbm) {
		this.jcxmbm = jcxmbm;
	}

	public String getPx() {
		return px;
	}

	public void setPx(String px) {
		this.px = px;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getXmsyglid() {
		return xmsyglid;
	}

	public void setXmsyglid(String xmsyglid) {
		this.xmsyglid = xmsyglid;
	}

	public String getJczxmmc() {
		return jczxmmc;
	}

	public void setJczxmmc(String jczxmmc) {
		this.jczxmmc = jczxmmc;
	}

	public String getKzcs6() {
		return kzcs6;
	}

	public void setKzcs6(String kzcs6) {
		this.kzcs6 = kzcs6;
	}

	public String getHbid() {
		return hbid;
	}

	public void setHbid(String hbid) {
		this.hbid = hbid;
	}

	public String getYblxdm() {
		return yblxdm;
	}

	public void setYblxdm(String yblxdm) {
		this.yblxdm = yblxdm;
	}

	public String getKzcs5() {
		return kzcs5;
	}

	public void setKzcs5(String kzcs5) {
		this.kzcs5 = kzcs5;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	//json
	private String sy_json;

	public String getSy_json() {
		return sy_json;
	}

	public void setSy_json(String sy_json) {
		this.sy_json = sy_json;
	}

	public String getYjcdw() {
		return yjcdw;
	}

	public void setYjcdw(String yjcdw) {
		this.yjcdw = yjcdw;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJclxmc() {
		return jclxmc;
	}

	public void setJclxmc(String jclxmc) {
		this.jclxmc = jclxmc;
	}

	public String getJclxdm() {
		return jclxdm;
	}

	public void setJclxdm(String jclxdm) {
		this.jclxdm = jclxdm;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
