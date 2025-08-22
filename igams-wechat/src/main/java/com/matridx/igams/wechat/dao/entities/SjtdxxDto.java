package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="SjtdxxDto")
public class SjtdxxDto extends SjtdxxModel{
	//寄送方式代码
	private String jsfsdm;
	//附件ID复数
	private List<String> fjids;
	//通知人员字符串
	private String tzry_str;
	//录入time
	private String lrtime;
	//录入time
	private String qjtime;
	//录入time
	private String sdtime;
	//录入time
	private String jdtime;
	//显示名称
	private String xsmc;
	//业务类型
	private String ywlx;
	//派单人名称
	private String pdrmc;
	//标本类型名称
	private String bblxmc;
	//取样联系人
	private String qylxr;
	//取样联系电话
	private String qylxdh;
	//取样地址
	private String qydz;
	//取样时间
	private String qysj;
	//预计时间
	private String yjsj;
	//标本编号
	private String bbbh;
	//送检派单状态
	private String sjpdzt;
	//接单人名称
	private String jdrmc;
	//接单人钉钉ID
	private String jdrddid;
	//取件标记
	private String qjbj;
	//是否收费
	private String sfsf;
	//收费金额
	private String sfje;
	//检测单位名称
	private String jcdwmc;
	//寄送方式名称
	private String jsfsmc;
	//接单标记
	private String jdbj;
	//派单人
	private String pdrddid;
	//删除时间
	private String sctime;
	//取样联系人
	private String sdrymc;
	//送达取样地址
	private String sjqydz;

	//录入人员名称
	private String lrrymc;
	//派单方式名称
	private String pdjsfsmc;
	//取消人员名称
	private String qxrymc;
	//派单备注
	private String pdbz;

	//取消派单原因
	private String qxpdyy;

	//患者姓名
	private String hzxm;
	//通知标记
	private String tzbj;
	//物流寄送方式
	private String wljsfs;
	//团单信息
	private String tdxx_str;
	//是否隐藏
	private String sfyc;

	public String getSfyc() {
		return sfyc;
	}

	public void setSfyc(String sfyc) {
		this.sfyc = sfyc;
	}

	public String getWljsfs() {
		return wljsfs;
	}

	public String getTdxx_str() {
		return tdxx_str;
	}

	public void setTdxx_str(String tdxx_str) {
		this.tdxx_str = tdxx_str;
	}

	public void setWljsfs(String wljsfs) {
		this.wljsfs = wljsfs;
	}

	public String getTzbj() {
		return tzbj;
	}

	public void setTzbj(String tzbj) {
		this.tzbj = tzbj;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getQxpdyy() {
		return qxpdyy;
	}

	public void setQxpdyy(String qxpdyy) {
		this.qxpdyy = qxpdyy;
	}

	public String getPdbz() {
		return pdbz;
	}

	public void setPdbz(String pdbz) {
		this.pdbz = pdbz;
	}

	public String getQxrymc() {
		return qxrymc;
	}

	public void setQxrymc(String qxrymc) {
		this.qxrymc = qxrymc;
	}

	public String getPdjsfsmc() {
		return pdjsfsmc;
	}

	public void setPdjsfsmc(String pdjsfsmc) {
		this.pdjsfsmc = pdjsfsmc;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getSdrymc() {
		return sdrymc;
	}

	public void setSdrymc(String sdrymc) {
		this.sdrymc = sdrymc;
	}

	public String getSjqydz() {
		return sjqydz;
	}

	public void setSjqydz(String sjqydz) {
		this.sjqydz = sjqydz;
	}

	public String getSctime() {
		return sctime;
	}

	public void setSctime(String sctime) {
		this.sctime = sctime;
	}


	public String getPdrddid() {
		return pdrddid;
	}

	public void setPdrddid(String pdrddid) {
		this.pdrddid = pdrddid;
	}

	public String getJdbj() {
		return jdbj;
	}

	public void setJdbj(String jdbj) {
		this.jdbj = jdbj;
	}

	public String getJsfsmc() {
		return jsfsmc;
	}

	public void setJsfsmc(String jsfsmc) {
		this.jsfsmc = jsfsmc;
	}

	public String getJdrddid() {
		return jdrddid;
	}

	public void setJdrddid(String jdrddid) {
		this.jdrddid = jdrddid;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getSfsf() {
		return sfsf;
	}

	public void setSfsf(String sfsf) {
		this.sfsf = sfsf;
	}

	public String getSfje() {
		return sfje;
	}

	public void setSfje(String sfje) {
		this.sfje = sfje;
	}

	public String getLrtime() {
		return lrtime;
	}

	public void setLrtime(String lrtime) {
		this.lrtime = lrtime;
	}

	public String getQjtime() {
		return qjtime;
	}

	public void setQjtime(String qjtime) {
		this.qjtime = qjtime;
	}

	public String getSdtime() {
		return sdtime;
	}

	public void setSdtime(String sdtime) {
		this.sdtime = sdtime;
	}

	public String getJdtime() {
		return jdtime;
	}

	public void setJdtime(String jdtime) {
		this.jdtime = jdtime;
	}

	public String getQjbj() {
		return qjbj;
	}

	public void setQjbj(String qjbj) {
		this.qjbj = qjbj;
	}

	public String getJdrmc() {
		return jdrmc;
	}

	public void setJdrmc(String jdrmc) {
		this.jdrmc = jdrmc;
	}

	public String getSjpdzt() {
		return sjpdzt;
	}

	public void setSjpdzt(String sjpdzt) {
		this.sjpdzt = sjpdzt;
	}

	public String getBbbh() {
		return bbbh;
	}

	public void setBbbh(String bbbh) {
		this.bbbh = bbbh;
	}

	public String getPdrmc() {
		return pdrmc;
	}

	public void setPdrmc(String pdrmc) {
		this.pdrmc = pdrmc;
	}

	public String getBblxmc() {
		return bblxmc;
	}

	public void setBblxmc(String bblxmc) {
		this.bblxmc = bblxmc;
	}

	public String getQylxr() {
		return qylxr;
	}

	public void setQylxr(String qylxr) {
		this.qylxr = qylxr;
	}

	public String getQylxdh() {
		return qylxdh;
	}

	public void setQylxdh(String qylxdh) {
		this.qylxdh = qylxdh;
	}

	public String getQydz() {
		return qydz;
	}

	public void setQydz(String qydz) {
		this.qydz = qydz;
	}

	public String getYjsj() {
		return yjsj;
	}

	public void setYjsj(String yjsj) {
		this.yjsj = yjsj;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getTzry_str() {
		return tzry_str;
	}

	public void setTzry_str(String tzry_str) {
		this.tzry_str = tzry_str;
	}

	public String getXsmc() {
		return xsmc;
	}

	public void setXsmc(String xsmc) {
		this.xsmc = xsmc;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getJsfsdm() {
		return jsfsdm;
	}

	public void setJsfsdm(String jsfsdm) {
		this.jsfsdm = jsfsdm;
	}

	public String getQysj() {
		return qysj;
	}

	public void setQysj(String qysj) {
		this.qysj = qysj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
