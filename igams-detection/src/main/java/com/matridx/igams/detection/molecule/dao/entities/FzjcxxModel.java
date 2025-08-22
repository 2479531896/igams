package com.matridx.igams.detection.molecule.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="FzjcxxModel")
public class FzjcxxModel extends BaseBasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//分子检测ID
	private String fzjcid;
	//患者ID
	private String hzid;
	//支付方式
	private String zffs;
	//体温
	private String tw;
	//标本编号
	private String ybbh;
	//内部编号
	private String nbbh;
	//标本类型
	private String yblx;
	//预约日期
	private String yyrq;
	//预约检测日期
	private String yyjcrq;
	//标本状态
	private String bbzt;
	//确认时间
	private String qrsj;
	//确认人员
	private String qrry;
	//是否接收
	private String sfjs;
	//是否接收
	private String sfjsmc;
	//接收时间
	private String jssj;
	//接收人员
	private String jsry;
	//是否实验
	private String sfsy;
	//是否实验
	private String sfsymc;
	//实验人员
	private String syry;
	//实验时间
	private String sysj;
	//报告日期
	private String bgrq;
	//检测结果
	private String jcjg;
	//状态
	private String zt;
	//建议
	private String jy;
	//操作标识
	private String czbs;
	//备注
	private String bz;
	//实验号
	private String syh;
	//检测项目id
	private String jcxmid;
	//检测项目名称
	private String jcxmmc;
	//检测单位
	private String jcdw;
	//采样点
	private String cyd;
	//科室
	private String ks;
	//送检单位
	private String sjdw;
	//送检单位名称
	private String sjdwmc;
	//其他科室
	private String qtks;
	//微信ID
	private String wxid;
	//发送时间，核酸数据上传到市数据库时间
	private String fssj;
	//上报时间 ，执行完对接接口的最后一部执行更新库中数据上报时间
	private String sbsj;
	//发送标记
	private String fsbj;
	//批次任务号
	private String pctaskid;
	//单次任务号
	private String dctaskid;
	//付款标记
	private String fkbj;
	//付款金额
	private String fkje;
	//付款日期
	private String fkrq;
	//开票标记
	private String kpbj;
	//发票号码
	private String fphm;
	//标本子编号
	private String bbzbh;
	//检测对象类型（eg：人/物检）
	private String jcdxlx;
	//样品名称
	private String ypmc;
	//生产厂家
	private String sccj;
	//生产批次
	private String scpc;
	//生产地
	private String scd;
    //支付订单
	private String orderno;
	//发票路径
	private String imgurl;
	//发票PDF路径
	private String pdfurl;
	//实付金额
	private String sfje;
	//平台支付订单
	private String ptorderno;
	//退款订单
	private String refundno;
	//平台
	private String pt;
	//退款日期
	private String tkrq;
	//退款成功日期
	private String tkcgrq;
	//生产日期
	private String scrq;
	//报告完成时间
	private String bgwcsj;
	//唯一编号
	private String wybh;
	//是否交付
	private String sfjf;
	//采集信息上传卫健状态
	private String cjxxsczt;
	//采集信息上传卫健时间
	private String cjxxscsj;
	//床号
	private String ch;
	//外部采集人员姓名
	private String wbcjry;
		//检测类型
	private String jclx;
	//报告完成数
	private String bgwcs;
	//项目数
	private String xms;

	public String getBgwcs() {
		return bgwcs;
	}

	public void setBgwcs(String bgwcs) {
		this.bgwcs = bgwcs;
	}

	public String getXms() {
		return xms;
	}

	public void setXms(String xms) {
		this.xms = xms;
	}

	public String getCjxxscsj() {
		return cjxxscsj;
	}

	public void setCjxxscsj(String cjxxscsj) {
		this.cjxxscsj = cjxxscsj;
	}

	public String getCjxxsczt() {
		return cjxxsczt;
	}

	public void setCjxxsczt(String cjxxsczt) {
		this.cjxxsczt = cjxxsczt;
	}
	
	public String getWbcjry() {
		return wbcjry;
	}

	public void setWbcjry(String wbcjry) {
		this.wbcjry = wbcjry;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	public String getSfjf() {
		return sfjf;
	}

	public void setSfjf(String sfjf) {
		this.sfjf = sfjf;
	}

	public String getWybh() {
		return wybh;
	}

	public void setWybh(String wybh) {
		this.wybh = wybh;
	}

	public String getBgwcsj() {
		return bgwcsj;
	}

	public void setBgwcsj(String bgwcsj) {
		this.bgwcsj = bgwcsj;
	}

	public String getSbsj() {
		return sbsj;
	}

	public void setSbsj(String sbsj) {
		this.sbsj = sbsj;
	}

	public String getCzbs() {
		return czbs;
	}

	public void setCzbs(String czbs) {
		this.czbs = czbs;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getTkrq() {
		return tkrq;
	}

	public void setTkrq(String tkrq) {
		this.tkrq = tkrq;
	}

	public String getTkcgrq() {
		return tkcgrq;
	}

	public void setTkcgrq(String tkcgrq) {
		this.tkcgrq = tkcgrq;
	}

	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

	public String getScd() {
		return scd;
	}

	public void setScd(String scd) {
		this.scd = scd;
	}

	public String getZffs() {
		return zffs;
	}

	public void setZffs(String zffs) {
		this.zffs = zffs;
	}

	public String getRefundno() {
		return refundno;
	}

	public void setRefundno(String refundno) {
		this.refundno = refundno;
	}

	public String getPtorderno() {
		return ptorderno;
	}

	public void setPtorderno(String ptorderno) {
		this.ptorderno = ptorderno;
	}

	public String getSfje() {
		return sfje;
	}

	public void setSfje(String sfje) {
		this.sfje = sfje;
	}

	public String getPdfurl() {
		return pdfurl;
	}

	public void setPdfurl(String pdfurl) {
		this.pdfurl = pdfurl;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
    public String getJcdxlx() {
		return jcdxlx;
	}

	public void setJcdxlx(String jcdxlx) {
		this.jcdxlx = jcdxlx;
	}

	public String getYpmc() {
		return ypmc;
	}

	public void setYpmc(String ypmc) {
		this.ypmc = ypmc;
	}

	public String getSccj() {
		return sccj;
	}

	public void setSccj(String sccj) {
		this.sccj = sccj;
	}

	public String getScpc() {
		return scpc;
	}

	public void setScpc(String scpc) {
		this.scpc = scpc;
	}
	
	public String getFkbj() {
		return fkbj;
	}

	public void setFkbj(String fkbj) {
		this.fkbj = fkbj;
	}

	public String getFkje() {
		return fkje;
	}

	public void setFkje(String fkje) {
		this.fkje = fkje;
	}

	public String getFkrq() {
		return fkrq;
	}

	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}

	public String getKpbj() {
		return kpbj;
	}

	public void setKpbj(String kpbj) {
		this.kpbj = kpbj;
	}

	public String getFphm() {
		return fphm;
	}

	public void setFphm(String fphm) {
		this.fphm = fphm;
	}

	public String getBbzbh() {
		return bbzbh;
	}

	public void setBbzbh(String bbzbh) {
		this.bbzbh = bbzbh;
	}
	
	public String getPctaskid() {
		return pctaskid;
	}

	public void setPctaskid(String pctaskid) {
		this.pctaskid = pctaskid;
	}

	public String getDctaskid() {
		return dctaskid;
	}

	public void setDctaskid(String dctaskid) {
		this.dctaskid = dctaskid;
	}

	public String getFsbj() { return fsbj; }

	public void setFsbj(String fsbj) { this.fsbj = fsbj; }

	public String getFssj() {
		return fssj;
	}

	public void setFssj(String fssj) {
		this.fssj = fssj;
	}

	public String getBbzt() {
		return bbzt;
	}

	public void setBbzt(String bbzt) {
		this.bbzt = bbzt;
	}

	public String getSfjsmc() {
		return sfjsmc;
	}

	public void setSfjsmc(String sfjsmc) {
		this.sfjsmc = sfjsmc;
	}

	public String getSfsymc() {
		return sfsymc;
	}

	public void setSfsymc(String sfsymc) {
		this.sfsymc = sfsymc;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	public String getKs() {
		return ks;
	}

	public void setKs(String ks) {
		this.ks = ks;
	}

	public String getSjdw() {
		return sjdw;
	}

	public void setSjdw(String sjdw) {
		this.sjdw = sjdw;
	}

	public String getSjdwmc() {
		return sjdwmc;
	}

	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
	}

	public String getQtks() {
		return qtks;
	}

	public void setQtks(String qtks) {
		this.qtks = qtks;
	}

	public String getCyd() {
		return cyd;
	}

	public void setCyd(String cyd) {
		this.cyd = cyd;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getSyh() {
		return syh;
	}

	public void setSyh(String syh) {
		this.syh = syh;
	}
	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJcxmid() {
		return jcxmid;
	}

	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}

	public String getFzjcid() {
		return fzjcid;
	}

	public void setFzjcid(String fzjcid) {
		this.fzjcid = fzjcid;
	}

	public String getHzid() {
		return hzid;
	}

	public void setHzid(String hzid) {
		this.hzid = hzid;
	}

	public String getTw() {
		return tw;
	}

	public void setTw(String tw) {
		this.tw = tw;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getNbbh() {
		return nbbh;
	}

	public void setNbbh(String nbbh) {
		this.nbbh = nbbh;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getYyrq() {
		return yyrq;
	}

	public void setYyrq(String yyrq) {
		this.yyrq = yyrq;
	}

	public String getYyjcrq() {
		return yyjcrq;
	}

	public void setYyjcrq(String yyjcrq) {
		this.yyjcrq = yyjcrq;
	}

	public String getQrsj() {
		return qrsj;
	}

	public void setQrsj(String qrsj) {
		this.qrsj = qrsj;
	}

	public String getQrry() {
		return qrry;
	}

	public void setQrry(String qrry) {
		this.qrry = qrry;
	}

	public String getSfjs() {
		return sfjs;
	}

	public void setSfjs(String sfjs) {
		this.sfjs = sfjs;
	}

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getJsry() {
		return jsry;
	}

	public void setJsry(String jsry) {
		this.jsry = jsry;
	}

	public String getSfsy() {
		return sfsy;
	}

	public void setSfsy(String sfsy) {
		this.sfsy = sfsy;
	}

	public String getSyry() {
		return syry;
	}

	public void setSyry(String syry) {
		this.syry = syry;
	}

	public String getSysj() {
		return sysj;
	}

	public void setSysj(String sysj) {
		this.sysj = sysj;
	}

	public String getBgrq() {
		return bgrq;
	}

	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getJy() {
		return jy;
	}

	public void setJy(String jy) {
		this.jy = jy;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
