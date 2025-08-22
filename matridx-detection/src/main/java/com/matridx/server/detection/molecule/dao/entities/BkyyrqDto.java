package com.matridx.server.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="BkyyrqDto")
public class BkyyrqDto extends BkyyrqModel{

	//不可预约日期开始
	private String bkyyrqstart;
	//不可预约日期结束
	private String bkyyrqend;
	//不可预约时间段开始
	private String bkyysjdstart;
	//不可预约时间段结束
	private String bkyysjdend;
	//标记（修改、新增）
	private String flag;
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public String getBkyyrqstart() {
		return bkyyrqstart;
	}

	public void setBkyyrqstart(String bkyyrqstart) {
		this.bkyyrqstart = bkyyrqstart;
	}

	public String getBkyyrqend() {
		return bkyyrqend;
	}

	public void setBkyyrqend(String bkyyrqend) {
		this.bkyyrqend = bkyyrqend;
	}

	public String getBkyysjdstart() {
		return bkyysjdstart;
	}

	public void setBkyysjdstart(String bkyysjdstart) {
		this.bkyysjdstart = bkyysjdstart;
	}

	public String getBkyysjdend() {
		return bkyysjdend;
	}

	public void setBkyysjdend(String bkyysjdend) {
		this.bkyysjdend = bkyysjdend;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
