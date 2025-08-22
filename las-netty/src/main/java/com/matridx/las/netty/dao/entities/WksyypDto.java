package com.matridx.las.netty.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="WksyypDto")
public class WksyypDto extends WksyypModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//文库名称
	private String libraryName;
	//内部编号json 托盘1
	private String wksyyps_one;
	//内部编号json 托盘2
	private String wksyyps_two;
	//内部编号json 托盘3
	private String wksyyps_three;
	//内部编号json 托盘4
	private String wksyyps_four;
	//是否首位 1:是 0:否
	private String sfsw;
	//是否尾位 1:是 0:否
	private String sfww;

	
	
	public String getWksyyps_one() {
		return wksyyps_one;
	}
	public void setWksyyps_one(String wksyyps_one) {
		this.wksyyps_one = wksyyps_one;
	}
	public String getWksyyps_two() {
		return wksyyps_two;
	}
	public void setWksyyps_two(String wksyyps_two) {
		this.wksyyps_two = wksyyps_two;
	}
	public String getWksyyps_three() {
		return wksyyps_three;
	}
	public void setWksyyps_three(String wksyyps_three) {
		this.wksyyps_three = wksyyps_three;
	}
	public String getWksyyps_four() {
		return wksyyps_four;
	}
	public void setWksyyps_four(String wksyyps_four) {
		this.wksyyps_four = wksyyps_four;
	}
	public String getSfsw() {
		return sfsw;
	}
	public void setSfsw(String sfsw) {
		this.sfsw = sfsw;
	}
	public String getSfww() {
		return sfww;
	}
	public void setSfww(String sfww) {
		this.sfww = sfww;
	}
	public String getLibraryName() {
		return libraryName;
	}
	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}
	
}
