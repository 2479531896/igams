package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="HbphzxxDto")
public class HbphzxxDto extends HbphzxxModel{

	private static final long serialVersionUID = 1L;
	//入ICU开始日期
	private String ricurqstart;
	//入ICU开始日期
	private String ricurqend;
	//入组开始日期
	private String rzrqstart;
	//入组结束日期
    private String rzrqend;

	public String getRzrqstart() {
		return rzrqstart;
	}

	public void setRzrqstart(String rzrqstart) {
		this.rzrqstart = rzrqstart;
	}

	public String getRzrqend() {
		return rzrqend;
	}

	public void setRzrqend(String rzrqend) {
		this.rzrqend = rzrqend;
	}

	public String getRicurqstart() {
		return ricurqstart;
	}

	public void setRicurqstart(String ricurqstart) {
		this.ricurqstart = ricurqstart;
	}

	public String getRicurqend() {
		return ricurqend;
	}

	public void setRicurqend(String ricurqend) {
		this.ricurqend = ricurqend;
	}
}
