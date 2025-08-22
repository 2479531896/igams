package com.matridx.server.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="FzjcxxModel")
public class FzjcxxModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fzjcid;//分子检测ID
	private String hzid;//患者ID
	private String tw;//体温
	private String ybbh;//样本编号（物检时对应检疫单号）
	private String nbbh;//内部编号
	private String yblx;//样本类型，这里采用基础数据，现阶段默认为咽拭子
	private String yyrq;//预约日期
	private String yyjcrq;//预约检测日期
	private String cjsj;//采集时间
	private String cjry;//采集人员
	private String sfjs;//是否接收 1 接收，0 拒绝，可能存在样本损坏需要重新采集的情况
	private String jssj;//接收时间
	private String jsry;//接收人员
	private String sfsy;//是否实验 1 已实验，0未实验
	private String syry;//实验人员
	private String sysj;//实验时间
	private String bgrq;//报告日期
	private String jcxmid;//检测项目ID
	private String jcxmmc;//检测项目名称
	private String jcjgmc;//检测结果名称
	private String cyd;//采样点
	private String jy;//建议
	private String syh;//实验室流水号
	private String jcdw;//检测单位关联基础数据
	private String sjdw;//送检单位
	private String sjdwmc;//送检单位名称
	private String ks;//科室
	private String qtks;//其他科室
	private String bz;//备注
	private String zt;//审核状态
	private String wxid;//微信ID
	private String qrry;//确认人员
	private String qrsj;//
	private String xm;//姓名
	private String bbzt;//标本状态
	private String xb;//性别
	private String sjys;//主治医师
	private String nl;//年龄
	private String mz;//门诊
	private String zyh;//住院号
	private String lxdh;//联系电话
	private String ch;//床号
	private String fssj;//发送时间，核酸数据上传到市数据库的时间
	private String fsbj;//
	private String pctaskid;//批次任务号，申请-检验-常规顺序
	private String dctaskid;//单次任务号，申请-检验-常规顺序
	private String scjg;//新冠报告上传结果，申请加1，检验加2，常规加4，结果为7则表示正确入库
	private String bbzbh;//标本子编号
	private String fkbj;//付款标记
	private String fkje;//付款金额
	private String fkrq;//付款日期
	private String kpbj;//开票标记
	private String fphm;//发票号码
	private String zffs;//支付方式
	private String orderno;//支付订单order
	private String imgurl;//发票路径
	private String pdfurl;//发票PDF路径
	private String refundno;//退款订单order
	private String sfje;//实付金额
	private String ptorderno;//平台订单
	private String ypmc;//样品名称
	private String sccj;//生产厂家
	private String scpc;//生产批次
	private String pt;//平台
	private String jcdxlx;//检测对象类型，分为人检和物检
	private String tkrq;//退款日期
	private String tkcgrq;//退款成功日期
	private String yblxmc;//样本类型名称
	private String scrq;//生产日期
	private String scd;//生产地（物检）
	private String czbs;//操作标识
	private String wybh;//唯一编号
	private String sbsj;//上报卫建委时间
	private String bgwcsj;//报告完成时间
	private String sfjf;//是否交付
	private String cjxxscsj;//采集上传时间，对应最新一次上传成功或者失败的时间
	private String cjxxsczt;//采集信息上传状态 0从未上传过，1上传过，2上传失败
	private String jclx;//检测类型
	private String wbcjry;//外部采集人员
	//医生电话
	private String ysdh;
	//标本状态名称
	private String bbztmc;
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

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getCjry() {
		return cjry;
	}

	public void setCjry(String cjry) {
		this.cjry = cjry;
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

	public String getJcxmid() {
		return jcxmid;
	}

	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getJcjgmc() {
		return jcjgmc;
	}

	public void setJcjgmc(String jcjgmc) {
		this.jcjgmc = jcjgmc;
	}

	public String getCyd() {
		return cyd;
	}

	public void setCyd(String cyd) {
		this.cyd = cyd;
	}

	public String getJy() {
		return jy;
	}

	public void setJy(String jy) {
		this.jy = jy;
	}

	public String getSyh() {
		return syh;
	}

	public void setSyh(String syh) {
		this.syh = syh;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
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

	public String getKs() {
		return ks;
	}

	public void setKs(String ks) {
		this.ks = ks;
	}

	public String getQtks() {
		return qtks;
	}

	public void setQtks(String qtks) {
		this.qtks = qtks;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	public String getQrry() {
		return qrry;
	}

	public void setQrry(String qrry) {
		this.qrry = qrry;
	}

	public String getQrsj() {
		return qrsj;
	}

	public void setQrsj(String qrsj) {
		this.qrsj = qrsj;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getBbzt() {
		return bbzt;
	}

	public void setBbzt(String bbzt) {
		this.bbzt = bbzt;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getSjys() {
		return sjys;
	}

	public void setSjys(String sjys) {
		this.sjys = sjys;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getZyh() {
		return zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getLxdh() {
		return lxdh;
	}

	public void setLxdh(String lxdh) {
		this.lxdh = lxdh;
	}

	public String getCh() {
		return ch;
	}

	public void setCh(String ch) {
		this.ch = ch;
	}

	public String getFssj() {
		return fssj;
	}

	public void setFssj(String fssj) {
		this.fssj = fssj;
	}

	public String getFsbj() {
		return fsbj;
	}

	public void setFsbj(String fsbj) {
		this.fsbj = fsbj;
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

	public String getScjg() {
		return scjg;
	}

	public void setScjg(String scjg) {
		this.scjg = scjg;
	}

	public String getBbzbh() {
		return bbzbh;
	}

	public void setBbzbh(String bbzbh) {
		this.bbzbh = bbzbh;
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

	public String getZffs() {
		return zffs;
	}

	public void setZffs(String zffs) {
		this.zffs = zffs;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getPdfurl() {
		return pdfurl;
	}

	public void setPdfurl(String pdfurl) {
		this.pdfurl = pdfurl;
	}

	public String getRefundno() {
		return refundno;
	}

	public void setRefundno(String refundno) {
		this.refundno = refundno;
	}

	public String getSfje() {
		return sfje;
	}

	public void setSfje(String sfje) {
		this.sfje = sfje;
	}

	public String getPtorderno() {
		return ptorderno;
	}

	public void setPtorderno(String ptorderno) {
		this.ptorderno = ptorderno;
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

	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

	public String getJcdxlx() {
		return jcdxlx;
	}

	public void setJcdxlx(String jcdxlx) {
		this.jcdxlx = jcdxlx;
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

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getScd() {
		return scd;
	}

	public void setScd(String scd) {
		this.scd = scd;
	}

	public String getCzbs() {
		return czbs;
	}

	public void setCzbs(String czbs) {
		this.czbs = czbs;
	}

	public String getWybh() {
		return wybh;
	}

	public void setWybh(String wybh) {
		this.wybh = wybh;
	}

	public String getSbsj() {
		return sbsj;
	}

	public void setSbsj(String sbsj) {
		this.sbsj = sbsj;
	}

	public String getBgwcsj() {
		return bgwcsj;
	}

	public void setBgwcsj(String bgwcsj) {
		this.bgwcsj = bgwcsj;
	}

	public String getSfjf() {
		return sfjf;
	}

	public void setSfjf(String sfjf) {
		this.sfjf = sfjf;
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

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	public String getWbcjry() {
		return wbcjry;
	}

	public void setWbcjry(String wbcjry) {
		this.wbcjry = wbcjry;
	}

	public String getYsdh() {
		return ysdh;
	}

	public void setYsdh(String ysdh) {
		this.ysdh = ysdh;
	}

	public String getBbztmc() {
		return bbztmc;
	}

	public void setBbztmc(String bbztmc) {
		this.bbztmc = bbztmc;
	}
}
