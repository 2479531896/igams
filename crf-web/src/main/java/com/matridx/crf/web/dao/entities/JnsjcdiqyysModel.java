package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JnsjcdiqyysModel")
public class JnsjcdiqyysModel extends BaseModel{
	//用药史id
	private String yysid;
	//艰难梭菌报告记录id
	private String jnsjbgid;
	//药物类型
	private String ywlx;
	//药物子类型
	private String ywzlx;
	//药物
	private String yw;
	//剂量
	private String jl;
	//开始用药日期
	private String ksrq;
	//停药时间
	private String tyrq;
	//其他药物名称
	private String qtywmc;


	public String getYysid() {
		return yysid;
	}

	public void setYysid(String yysid) {
		this.yysid = yysid;
	}

	public String getJnsjbgid() {
		return jnsjbgid;
	}

	public void setJnsjbgid(String jnsjbgid) {
		this.jnsjbgid = jnsjbgid;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getYwzlx() {
		return ywzlx;
	}

	public void setYwzlx(String ywzlx) {
		this.ywzlx = ywzlx;
	}

	public String getYw() {
		return yw;
	}

	public void setYw(String yw) {
		this.yw = yw;
	}

	public String getJl() {
		return jl;
	}

	public void setJl(String jl) {
		this.jl = jl;
	}

	public String getKsrq() {
		return ksrq;
	}

	public void setKsrq(String ksrq) {
		this.ksrq = ksrq;
	}

	public String getTyrq() {
		return tyrq;
	}

	public void setTyrq(String tyrq) {
		this.tyrq = tyrq;
	}

	public String getQtywmc() {
		return qtywmc;
	}

	public void setQtywmc(String qtywmc) {
		this.qtywmc = qtywmc;
	}



	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
