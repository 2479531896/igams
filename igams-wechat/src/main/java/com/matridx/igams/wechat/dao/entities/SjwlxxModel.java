package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjwlxxModel")
public class SjwlxxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//物流ID
	private String wlid;
	//送检派单ID
	private String sjpdid;
	//上级物流ID
	private String sjwlid;
	//接单人
	private String jdr;
	//接单时间
	private String jdsj;
	//预估取件时间
	private String ygqjsj;
	//取消接单原因
	private String qxjdyy;
	//取件时间
	private String qjsj;
	//寄送方式
	private String jsfs;
	//关联单号
	private String gldh;
	//班次
	private String bc;
	//到达地点
	private String dddd;
	//预计到达时间
	private String yjddsj;

	//取件备注
	private String qjbz;
	//送达时间
	private String sdsj;
	//物流费用
	private String wlfy;
	//物流状态
	private String wlzt;
	//接单备注
	private String jdbz;
	//送达备注
	private String sdbz;
	//预计送达时间
	private String yjsdsj;
	//送检团单id
	private String sjtdid;

	public String getSjtdid() {
		return sjtdid;
	}

	public void setSjtdid(String sjtdid) {
		this.sjtdid = sjtdid;
	}

	public String getYjsdsj() {
		return yjsdsj;
	}

	public void setYjsdsj(String yjsdsj) {
		this.yjsdsj = yjsdsj;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getSjpdid() {
		return sjpdid;
	}

	public void setSjpdid(String sjpdid) {
		this.sjpdid = sjpdid;
	}

	public String getSjwlid() {
		return sjwlid;
	}

	public void setSjwlid(String sjwlid) {
		this.sjwlid = sjwlid;
	}

	public String getJdr() {
		return jdr;
	}

	public void setJdr(String jdr) {
		this.jdr = jdr;
	}

	public String getJdsj() {
		return jdsj;
	}

	public void setJdsj(String jdsj) {
		this.jdsj = jdsj;
	}

	public String getYgqjsj() {
		return ygqjsj;
	}

	public void setYgqjsj(String ygqjsj) {
		this.ygqjsj = ygqjsj;
	}

	public String getQxjdyy() {
		return qxjdyy;
	}

	public void setQxjdyy(String qxjdyy) {
		this.qxjdyy = qxjdyy;
	}

	public String getQjsj() {
		return qjsj;
	}

	public void setQjsj(String qjsj) {
		this.qjsj = qjsj;
	}

	public String getJsfs() {
		return jsfs;
	}

	public void setJsfs(String jsfs) {
		this.jsfs = jsfs;
	}

	public String getGldh() {
		return gldh;
	}

	public void setGldh(String gldh) {
		this.gldh = gldh;
	}

	public String getBc() {
		return bc;
	}

	public void setBc(String bc) {
		this.bc = bc;
	}

	public String getDddd() {
		return dddd;
	}

	public void setDddd(String dddd) {
		this.dddd = dddd;
	}

	public String getYjddsj() {
		return yjddsj;
	}

	public void setYjddsj(String yjddsj) {
		this.yjddsj = yjddsj;
	}
	public String getQjbz() {
		return qjbz;
	}

	public void setQjbz(String qjbz) {
		this.qjbz = qjbz;
	}

	public String getSdsj() {
		return sdsj;
	}

	public void setSdsj(String sdsj) {
		this.sdsj = sdsj;
	}

	public String getWlfy() {
		return wlfy;
	}

	public void setWlfy(String wlfy) {
		this.wlfy = wlfy;
	}

	public String getWlzt() {
		return wlzt;
	}

	public void setWlzt(String wlzt) {
		this.wlzt = wlzt;
	}

	public String getJdbz() {
		return jdbz;
	}

	public void setJdbz(String jdbz) {
		this.jdbz = jdbz;
	}

	public String getSdbz() {
		return sdbz;
	}

	public void setSdbz(String sdbz) {
		this.sdbz = sdbz;
	}
}
