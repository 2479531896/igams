package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjbgsmDto")
public class SjbgsmDto extends SjbgsmModel{
	//host_index
	private String host_index;
	//检测项目ID
	private String jcxmid;
	//检测项目ID
	private String jczxmid;
	//检测项目名称
	private String jcxmmc;
	//检测项目名称
	private String jczxmmc;
	//患者姓名
	private String hzxm;
	//性别
	private String xb;
	//年龄
	private String nl;
	//参数id
	private String csid;
	//标本编号
	private String ybbh;
	//内部编号
	private String nbbm;
	//电话
	private String dh;
	//送检单位
	private String hospitalname;
	//科室名称
	private String ksmc;
	//送检医生
	private String sjys;
	//标本类型
	private String yblxmc;
	//采样日期
	private String cyrq;
	//接收日期
	private String jsrq;
	//报告日期
	private String bgrq;
	//送检单位名称
	private String sjdwmc;
	//cskz3
	private String cskz3;
	private String bgsm_cskz1;
	private String bgsm_cskz2;
	private String bgsm_cskz3;

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getBgsm_cskz1() {
		return bgsm_cskz1;
	}

	public void setBgsm_cskz1(String bgsm_cskz1) {
		this.bgsm_cskz1 = bgsm_cskz1;
	}

	public String getBgsm_cskz2() {
		return bgsm_cskz2;
	}

	public void setBgsm_cskz2(String bgsm_cskz2) {
		this.bgsm_cskz2 = bgsm_cskz2;
	}

	public String getBgsm_cskz3() {
		return bgsm_cskz3;
	}

	public void setBgsm_cskz3(String bgsm_cskz3) {
		this.bgsm_cskz3 = bgsm_cskz3;
	}


	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getDh() {
		return dh;
	}

	public void setDh(String dh) {
		this.dh = dh;
	}

	public String getHospitalname() {
		return hospitalname;
	}

	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}

	public String getKsmc() {
		return ksmc;
	}

	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}

	public String getSjys() {
		return sjys;
	}

	public void setSjys(String sjys) {
		this.sjys = sjys;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getCyrq() {
		return cyrq;
	}

	public void setCyrq(String cyrq) {
		this.cyrq = cyrq;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	@Override
	public String getBgrq() {
		return bgrq;
	}

	@Override
	public void setBgrq(String bgrq) {
		this.bgrq = bgrq;
	}

	public String getCskz3() {
		return cskz3;
	}

	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
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

	public String getSjdwmc() {
		return sjdwmc;
	}

	public void setSjdwmc(String sjdwmc) {
		this.sjdwmc = sjdwmc;
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


	public String getHost_index() {
		return host_index;
	}


	public void setHost_index(String host_index) {
		this.host_index = host_index;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public String getJczxmid() {
        return jczxmid;
    }

    public void setJczxmid(String jczxmid) {
        this.jczxmid = jczxmid;
    }

    public String getJczxmmc() {
        return jczxmmc;
    }

    public void setJczxmmc(String jczxmmc) {
        this.jczxmmc = jczxmmc;
    }
}
