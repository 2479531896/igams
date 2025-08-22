package com.matridx.igams.wechat.dao.entities;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

@Alias(value="PayinfoDto")
public class PayinfoDto extends PayinfoModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//退款金额(合计)
	private String tkje;
	//退款中金额(合计)
	private String tkzje;
	//实付金额
	private String sfje;
	//签名验证
	private String sign;
	//患者姓名
	private String hzxm;
	//标本编号
	private String ybbh;
	//内部编码
	private String nbbm;
	//支付方式
	private String zffs;
	//合作伙伴
	private String sjhb;
	//交易开始时间
	private String jysjstart;
	//交易结束时间
	private String jysjend;
	//高级筛选支付结果状态（多）
	private String[] jgs;
	//支付结果名称
	private String jgmc;
	//导出关联标记为所选择的字段
	private String sqlParam;
	//关联表标记
	private String sjxx_flg;
	//应付金额（送检表中应付金额）
	private String yfje;
	//交易金额,以元为单位
	private BigDecimal jyje_yuan;
	//检查项目名称
	private String jcxmmc;
	//交易总金额
	private String jyzje;


	public String getJyzje() {
		return jyzje;
	}

	public void setJyzje(String jyzje) {
		this.jyzje = jyzje;
	}

	public String getJcxmmc() {
		return jcxmmc;
	}
	public void setJcxmmc(String jcxmmc) {
		this.jcxmmc = jcxmmc;
	}
	public String getYfje() {
		return yfje;
	}
	public void setYfje(String yfje) {
		this.yfje = yfje;
	}
	public BigDecimal getJyje_yuan() {
		return jyje_yuan;
	}
	public void setJyje_yuan(BigDecimal jyje_yuan) {
		this.jyje_yuan = jyje_yuan;
	}
	public String getSjxx_flg() {
		return sjxx_flg;
	}
	public void setSjxx_flg(String sjxx_flg) {
		this.sjxx_flg = sjxx_flg;
	}
	public String getSqlParam() {
		return sqlParam;
	}
	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}
	public String getJgmc() {
		return jgmc;
	}
	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}
	public String getNbbm() {
		return nbbm;
	}
	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}
	public String[] getJgs() {
		return jgs;
	}
	public void setJgs(String[] jgs) {
		this.jgs = jgs;
		for(int i=0; i<this.jgs.length; i++) {
			this.jgs[i] = this.jgs[i].replace("'", "");
		}
	}
	public String getJysjstart() {
		return jysjstart;
	}
	public void setJysjstart(String jysjstart) {
		this.jysjstart = jysjstart;
	}
	public String getJysjend() {
		return jysjend;
	}
	public void setJysjend(String jysjend) {
		this.jysjend = jysjend;
	}
	public String getSjhb() {
		return sjhb;
	}
	public void setSjhb(String sjhb) {
		this.sjhb = sjhb;
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
	public String getZffs() {
		return zffs;
	}
	public void setZffs(String zffs) {
		this.zffs = zffs;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTkje() {
		return tkje;
	}
	public void setTkje(String tkje) {
		this.tkje = tkje;
	}
	public String getTkzje() {
		return tkzje;
	}
	public void setTkzje(String tkzje) {
		this.tkzje = tkzje;
	}
	public String getSfje() {
		return sfje;
	}
	public void setSfje(String sfje) {
		this.sfje = sfje;
	}
}
