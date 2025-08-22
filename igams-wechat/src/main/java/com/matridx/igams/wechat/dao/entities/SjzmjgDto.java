package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="SjzmjgDto")
public class SjzmjgDto extends SjzmjgModel{
	//神经综合征
	private String sjzhz;
	//相关肿瘤
	private String xgzl;
	//提示
	private String ts;
	//解释信息
	private String jsjjy;
	//简称
	private String jc;

	public String getJc() {
		return jc;
	}

	public void setJc(String jc) {
		this.jc = jc;
	}

	public String getJsjjy() {
		return jsjjy;
	}

	public void setJsjjy(String jsjjy) {
		this.jsjjy = jsjjy;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getSjzhz() {
		return sjzhz;
	}

	public void setSjzhz(String sjzhz) {
		this.sjzhz = sjzhz;
	}

	public String getXgzl() {
		return xgzl;
	}

	public void setXgzl(String xgzl) {
		this.xgzl = xgzl;
	}

}
