package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="PcrsyjgDto")
public class PcrsyjgDto extends PcrsyjgModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//接收开始时间
	private String jssjstart;
	//接收结束时间
	private String jssjend;
	//检测单位限制
	private List<String> jcdwxz;
	//检测单位名称
	private String jcdwmc;
	//高级筛选 检测单位
	private String[] jcdws;

	public String[] getJcdws() {
		return jcdws;
	}

	public void setJcdws(String[] jcdws) {
		this.jcdws = jcdws;
		for (int i = 0; i < jcdws.length; i++) {
			this.jcdws[i] = this.jcdws[i].replace("'", "");
		}
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}

	public List<String> getJcdwxz() {
		return jcdwxz;
	}

	public void setJcdwxz(List<String> jcdwxz) {
		this.jcdwxz = jcdwxz;
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
}
