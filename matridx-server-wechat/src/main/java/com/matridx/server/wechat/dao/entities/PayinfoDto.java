package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="PayinfoDto")
public class PayinfoDto extends PayinfoModel{

	private static final long serialVersionUID = 1L;
	
	// 标本编号
	private String ybbh;
	// 送检ID
	private String sjid;
	// 微信ID
	private String wxid;
	// 关注平台
	private String gzpt;
	// 患者姓名
	private String hzxm;
	// 微信支付类型 public:公众号支付  mini:小程序支付  h5:H5支付  app:APP支付
	private String wxzflx;
	// 签名(校验)
	private String sign;
	// 退款金额
	private String tkje;
	// 送检付款金额
	private String sjfkje;
	// 实付金额
	private String sfje;
	// 钉钉ID
	private String ddid;
	// 合作伙伴
	private String sjhb;
	//检测类型
	private String jclx;
	//外部程序代码
	private String wbcxdm;

	public String getWbcxdm() {
		return wbcxdm;
	}

	public void setWbcxdm(String wbcxdm) {
		this.wbcxdm = wbcxdm;
	}

	public String getJclx() {
		return jclx;
	}

	public void setJclx(String jclx) {
		this.jclx = jclx;
	}

	public String getSjhb() {
		return sjhb;
	}
	public void setSjhb(String sjhb) {
		this.sjhb = sjhb;
	}
	public String getDdid() {
		return ddid;
	}
	public void setDdid(String ddid) {
		this.ddid = ddid;
	}
	public String getSfje() {
		return sfje;
	}
	public void setSfje(String sfje) {
		this.sfje = sfje;
	}
	public String getSjfkje() {
		return sjfkje;
	}
	public void setSjfkje(String sjfkje) {
		this.sjfkje = sjfkje;
	}
	public String getTkje() {
		return tkje;
	}
	public void setTkje(String tkje) {
		this.tkje = tkje;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getWxzflx() {
		return wxzflx;
	}
	public void setWxzflx(String wxzflx) {
		this.wxzflx = wxzflx;
	}
	public String getHzxm() {
		return hzxm;
	}
	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}
	public String getGzpt() {
		return gzpt;
	}
	public void setGzpt(String gzpt) {
		this.gzpt = gzpt;
	}
	public String getWxid() {
		return wxid;
	}
	public void setWxid(String wxid) {
		this.wxid = wxid;
	}
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid) {
		this.sjid = sjid;
	}
	public String getYbbh() {
		return ybbh;
	}
	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

}
