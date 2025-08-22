package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="PxglxxDto")
public class PxglxxDto extends PxglxxModel{
	private String fwqz;//访问前缀
	private String pxlbmc;//培训类别名称
	private String pxzlbmc;//培训子类别名称
	private String gqsj;//过期时间
	private String pxbt;//培训标题

	public String getPxbt() {
		return pxbt;
	}

	public void setPxbt(String pxbt) {
		this.pxbt = pxbt;
	}

	public String getPxlbmc() {
		return pxlbmc;
	}

	public void setPxlbmc(String pxlbmc) {
		this.pxlbmc = pxlbmc;
	}

	public String getPxzlbmc() {
		return pxzlbmc;
	}

	public void setPxzlbmc(String pxzlbmc) {
		this.pxzlbmc = pxzlbmc;
	}

	public String getGqsj() {
		return gqsj;
	}

	public void setGqsj(String gqsj) {
		this.gqsj = gqsj;
	}

	public String getFwqz() {
		return fwqz;
	}

	public void setFwqz(String fwqz) {
		this.fwqz = fwqz;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
