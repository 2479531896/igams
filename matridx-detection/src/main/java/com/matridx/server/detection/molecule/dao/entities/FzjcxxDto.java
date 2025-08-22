package com.matridx.server.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value="FzjcxxDto")
public class FzjcxxDto extends FzjcxxModel{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//患者信息
	private String xm;
	private String xb;
	private String xbmc;
	private String jclxdm;
	private String nl;
	private String sj;
	private String sfzh;
	private String xjzd;
	private String sfqr;
	private String twjcz;
	private String zjlx;
	//采集日期
	private String cjsj;
	private String cjry;
	private String syry;
	private String serial;
	private String cjryxm;
	//检测项目
	private String jcxmmc;
	private String jcjgmc;
	private String jcxm;
	private String jssjstart;
	private String jssjend;
	private String sysjstart;
	private String sysjend;
	private String cjsjstart;
	private String cjsjend;
	private String bgrqstart;
	private String bgrqend;

	private String fzxmid;
	private String fyk;
	private String jymc;
	private String bgjy;
	private String ctz;
	private String[] fzxmids;

	private String sqlParam;
	private String auditType;
	private String sqr;
	private String shry;
	private String shsj;
	private String sftg;
	private String shyj;
	private String shxx_shxxid;
	private List<String> fjids;
	private String sqsj;
	private String yyxxcskz1;
	private String dwmc;
	private String zjh;
	//标本状态名称
	private String bbztmc;
	private String fplx;
	private String buyerName;
	private String buyerTaxNum;
	private String buyerTel;
	private String buyerAccounts;
	private String buyerAccount;
	private String buyerPhone;
	private String email;
	private String buyerAddress;
	//检测对象参数代码
	private String jcdxcsdm;
	//分子检测子项目名称
	private String jczxmmc;
	private String jczxmid;
	//钉钉标记
	private String ddbj;

	//采样点名称
	private String cydmc;
	//采集人员名称
	private String cjrymc;
	//附件ID
	private String fjid;
	//签名
	private String sign;

	//标本编号
	private String ybbh;
	//分子检测项目名称
	private String fzxmmc;
	//检测单位名称
	private String jcdwmc;

	//检查项目（多）
	private String[] jcxmmcs;
	//检测项目id(多)
	private String[] jcxmids;
	//检测子项目id(多)
	private String[] jczxmids;
	//微信id
	private String wxid;
	private String str_jcxmmcs;
	private String yblxmc;
	//医院名称
	private String yymc;
	//科室名称
	private String ksmc;

	//录入人员list
	private List<String> lrrys;

	private String fzjclx;

	//模糊查询所有用的参数
	private String entire;
	private String sjdwbj;
	//标本状态
	private List<String> pjbbzts;
	//分子检测项目
	private List<FzjcxmDto> addFzjcxmDtos;
	//分子检测项目
	private List<String> delIds;

	public List<String> getDelIds() {
		return delIds;
	}

	public void setDelIds(List<String> delIds) {
		this.delIds = delIds;
	}

	public List<FzjcxmDto> getAddFzjcxmDtos() {
		return addFzjcxmDtos;
	}

	public void setAddFzjcxmDtos(List<FzjcxmDto> addFzjcxmDtos) {
		this.addFzjcxmDtos = addFzjcxmDtos;
	}


	public List<String> getPjbbzts() {
		return pjbbzts;
	}

	public void setPjbbzts(List<String> pjbbzts) {
		this.pjbbzts = pjbbzts;
	}

	public String getSjdwbj() {
		return sjdwbj;
	}

	public void setSjdwbj(String sjdwbj) {
		this.sjdwbj = sjdwbj;
	}

	public String getJczxmid() {
		return jczxmid;
	}

	public void setJczxmid(String jczxmid) {
		this.jczxmid = jczxmid;
	}

	public String getEntire()
	{
		return entire;
	}

	public void setEntire(String entire)
	{
		this.entire = entire;
	}
	public String getFzjclx() {
		return fzjclx;
	}

	public void setFzjclx(String fzjclx) {
		this.fzjclx = fzjclx;
	}

	public List<String> getLrrys() {
		return lrrys;
	}

	public void setLrrys(String lrrys) {
		List<String> list = new ArrayList<String>();
		String str[] = lrrys.split(",");
		list = Arrays.asList(str);
		this.lrrys = list;
	}
	public void setLrrys(List<String> lrrys) {
		if(lrrys!=null && lrrys.size() > 0){
			for(int i=0;i<lrrys.size();i++){
				lrrys.set(i,lrrys.get(i).replace("[", "").replace("]", "").trim());
			}
		}
		this.lrrys = lrrys;
	}
	public String getKsmc() {
		return ksmc;
	}

	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}

	public String getYymc() {
		return yymc;
	}

	public void setYymc(String yymc) {
		this.yymc = yymc;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getFjid() {
		return fjid;
	}

	public void setFjid(String fjid) {
		this.fjid = fjid;
	}

	public String getDdbj() {
		return ddbj;
	}

	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
	}
	public String getCjrymc() {
		return cjrymc;
	}

	public void setCjrymc(String cjrymc) {
		this.cjrymc = cjrymc;
	}

	public String getCydmc() {
		return cydmc;
	}

	public void setCydmc(String cydmc) {
		this.cydmc = cydmc;
	}


	public String getFplx() {
		return fplx;
	}

	public void setFplx(String fplx) {
		this.fplx = fplx;
	}

	public String getBuyerTel() {
		return buyerTel;
	}

	public void setBuyerTel(String buyerTel) {
		this.buyerTel = buyerTel;
	}

	public String getBuyerAccounts() {
		return buyerAccounts;
	}

	public void setBuyerAccounts(String buyerAccounts) {
		this.buyerAccounts = buyerAccounts;
	}

	public String getBuyerAccount() {
		return buyerAccount;
	}

	public void setBuyerAccount(String buyerAccount) {
		this.buyerAccount = buyerAccount;
	}

	public String getJczxmmc() {
		return jczxmmc;
	}

	public void setJczxmmc(String jczxmmc) {
		this.jczxmmc = jczxmmc;
	}

	public String getJcdxcsdm() {
		return jcdxcsdm;
	}

	public void setJcdxcsdm(String jcdxcsdm) {
		this.jcdxcsdm = jcdxcsdm;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getBuyerTaxNum() {
		return buyerTaxNum;
	}

	public void setBuyerTaxNum(String buyerTaxNum) {
		this.buyerTaxNum = buyerTaxNum;
	}

	public String getBuyerPhone() {
		return buyerPhone;
	}

	public void setBuyerPhone(String buyerPhone) {
		this.buyerPhone = buyerPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBuyerAddress() {
		return buyerAddress;
	}

	public void setBuyerAddress(String buyerAddress) {
		this.buyerAddress = buyerAddress;
	}

	public String getZjh() {
		return zjh;
	}

	public void setZjh(String zjh) {
		this.zjh = zjh;
	}

	public String getYyxxcskz1() {
		return yyxxcskz1;
	}

	public void setYyxxcskz1(String yyxxcskz1) {
		this.yyxxcskz1 = yyxxcskz1;
	}

	public String getDwmc() {
		return dwmc;
	}

	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}


	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getStr_jcxmmcs() {
		return str_jcxmmcs;
	}

	public void setStr_jcxmmcs(String str_jcxmmcs) {
		this.str_jcxmmcs = str_jcxmmcs;
	}

	public String getJcxm() {
		return jcxm;
	}

	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}

	public String getWxid() {
		return wxid;
	}

	public void setWxid(String wxid) {
		this.wxid = wxid;
	}

	public String[] getJcxmids() {
		return jcxmids;
	}

	public void setJcxmids(String[] jcxmids) {
		this.jcxmids = jcxmids;
		for(int i=0;i<this.jcxmids.length;i++)
		{
			this.jcxmids[i] = this.jcxmids[i].replace("'", "");
		}
	}
	public String[] getJczxmids() {
		return jczxmids;
	}

	public void setJczxmids(String[] jczxmids) {
		this.jczxmids = jczxmids;
		for(int i=0;i<this.jczxmids.length;i++)
		{
			this.jczxmids[i] = this.jczxmids[i].replace("'", "");
		}
	}

	public String[] getJcxmmcs() {
		return jcxmmcs;
	}

	public void setJcxmmcs(String[] jcxms) {
		this.jcxmmcs = jcxms;
		for(int i=0;i<this.jcxmmcs.length;i++)
		{
			this.jcxmmcs[i] = this.jcxmmcs[i].replace("'", "");
		}
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public String getZjlx() {
		return zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	public String getTwjcz() {
		return twjcz;
	}

	public void setTwjcz(String twjcz) {
		this.twjcz = twjcz;
	}

	public String getSfqr() {
		return sfqr;
	}

	public void setSfqr(String sfqr) {
		this.sfqr = sfqr;
	}

	public String[] getFzxmids() {
		return fzxmids;
	}

	public void setFzxmids(String[] fzxmids) {
		this.fzxmids = fzxmids;
	}

	public String getFzxmmc() {
		return fzxmmc;
	}

	public void setFzxmmc(String fzxmmc) {
		this.fzxmmc = fzxmmc;
	}

	public String getJcjgmc() {
		return jcjgmc;
	}

	public void setJcjgmc(String jcjgmc) {
		this.jcjgmc = jcjgmc;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}

	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}

	public String getShxx_shxxid() {
		return shxx_shxxid;
	}

	public void setShxx_shxxid(String shxx_shxxid) {
		this.shxx_shxxid = shxx_shxxid;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getSqsj() {
		return sqsj;
	}

	public void setSqsj(String sqsj) {
		this.sqsj = sqsj;
	}

	public String getShry() {
		return shry;
	}

	public void setShry(String shry) {
		this.shry = shry;
	}

	public String getShsj() {
		return shsj;
	}

	public void setShsj(String shsj) {
		this.shsj = shsj;
	}

	public String getSftg() {
		return sftg;
	}

	public void setSftg(String sftg) {
		this.sftg = sftg;
	}

	public String getShyj() {
		return shyj;
	}

	public void setShyj(String shyj) {
		this.shyj = shyj;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getXjzd() {
		return xjzd;
	}

	public void setXjzd(String xjzd) {
		this.xjzd = xjzd;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getSqlParam() {
		return sqlParam;
	}

	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}

	public String getFzxmid() {
		return fzxmid;
	}

	public void setFzxmid(String fzxmid) {
		this.fzxmid = fzxmid;
	}

	public String getFyk() {
		return fyk;
	}

	public void setFyk(String fyk) {
		this.fyk = fyk;
	}

	public String getJymc() {
		return jymc;
	}

	public void setJymc(String jymc) {
		this.jymc = jymc;
	}

	public String getBgjy() {
		return bgjy;
	}

	public void setBgjy(String bgjy) {
		this.bgjy = bgjy;
	}

	public String getCtz() {
		return ctz;
	}

	public void setCtz(String ctz) {
		this.ctz = ctz;
	}

	public String getCjry() {
		return cjry;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getCjryxm() {
		return cjryxm;
	}

	public void setCjryxm(String cjryxm) {
		this.cjryxm = cjryxm;
	}

	public void setCjry(String cjry) {
		this.cjry = cjry;
	}

	@Override
	public String getSyry() {
		return syry;
	}

	@Override
	public void setSyry(String syry) {
		this.syry = syry;
	}

	public String getSj() {
		return sj;
	}

	public void setSj(String sj) {
		this.sj = sj;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getJssjstart() {
		return jssjstart;
	}

	public void setJssjstart(String jssjstart) {
		this.jssjstart = jssjstart;
	}

	public String getJssjend() {
		return jssjend;
	}

	public void setJssjend(String jssjend) {
		this.jssjend = jssjend;
	}

	public String getSysjstart() {
		return sysjstart;
	}

	public void setSysjstart(String sysjstart) {
		this.sysjstart = sysjstart;
	}

	public String getSysjend() {
		return sysjend;
	}

	public void setSysjend(String sysjend) {
		this.sysjend = sysjend;
	}

	public String getCjsjstart() {
		return cjsjstart;
	}

	public void setCjsjstart(String cjsjstart) {
		this.cjsjstart = cjsjstart;
	}

	public String getCjsjend() {
		return cjsjend;
	}

	public void setCjsjend(String cjsjend) {
		this.cjsjend = cjsjend;
	}

	public String getBgrqstart() {
		return bgrqstart;
	}

	public void setBgrqstart(String bgrqstart) {
		this.bgrqstart = bgrqstart;
	}

	public String getBgrqend() {
		return bgrqend;
	}

	public void setBgrqend(String bgrqend) {
		this.bgrqend = bgrqend;
	}

	public String getCjsj() {
		return cjsj;
	}

	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}

	public String getBbztmc() {
		return bbztmc;
	}

	public void setBbztmc(String bbztmc) {
		this.bbztmc = bbztmc;
	}

	public String getXbmc() {
		return xbmc;
	}

	public void setXbmc(String xbmc) {
		this.xbmc = xbmc;
	}

	public String getJclxdm() {
		return jclxdm;
	}

	public void setJclxdm(String jclxdm) {
		this.jclxdm = jclxdm;
	}
}