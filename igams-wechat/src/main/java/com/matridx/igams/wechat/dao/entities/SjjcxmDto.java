package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjjcxmDto")
public class SjjcxmDto extends SjjcxmModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//检测项目名称
	private String jcxmmc;
	//检测子项目名称
	private String jczxmmc;
	//检测项目代码
	private String jcxmdm;
	//检测子项目代码
	private String jczxmdm;
	
	private String tkfsmc;//退款方式
	//基础数据kzcs1
	private String cskz1;
	//基础数据kzcs3
	private String cskz3;
	private String bs;
	private String json;
	//变动实付金额
	private String bdsfje;
	private String sfjq;//是否结清

	public String getSfjq() {
		return sfjq;
	}

	public void setSfjq(String sfjq) {
		this.sfjq = sfjq;
	}

	public String getBdsfje() {
		return bdsfje;
	}

	public void setBdsfje(String bdsfje) {
		this.bdsfje = bdsfje;
	}
	private String hbmc;

	public String getHbmc() {
		return hbmc;
	}

	public void setHbmc(String hbmc) {
		this.hbmc = hbmc;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getTkfsmc() {
		return tkfsmc;
	}

	public void setTkfsmc(String tkfsmc) {
		this.tkfsmc = tkfsmc;
	}

	public String getBs() {
		return bs;
	}

	public void setBs(String bs) {
		this.bs = bs;
	}

	public String getCskz3() {
		return cskz3;
	}

	public void setCskz3(String cskz3) {
		this.cskz3 = cskz3;
	}

	public String getCskz1() {
		return cskz1;
	}

	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}

    public String getJcxmmc() {
        return jcxmmc;
    }

    public void setJcxmmc(String jcxmmc) {
        this.jcxmmc = jcxmmc;
    }

    public String getJczxmmc() {
        return jczxmmc;
    }

    public void setJczxmmc(String jczxmmc) {
        this.jczxmmc = jczxmmc;
    }

	public String getJcxmdm() {
		return jcxmdm;
	}

	public void setJcxmdm(String jcxmdm) {
		this.jcxmdm = jcxmdm;
	}

	public String getJczxmdm() {
		return jczxmdm;
	}

	public void setJczxmdm(String jczxmdm) {
		this.jczxmdm = jczxmdm;
	}
}
