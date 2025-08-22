package com.matridx.igams.experiment.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.FjcfbDto;

@Alias(value="RwlyDto")
public class RwlyDto extends RwlyModel{
	//留言人员
	private String lyry;
	//留言时间
	private String lysj;
	private String yhm;
	//字体颜色
	private String color;
	//附件ID复数
	private List<String> fjids;
	//留言附件
	private List<FjcfbDto> fj_List;

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public List<FjcfbDto> getFj_List()
	{
		return fj_List;
	}
	public void setFj_List(List<FjcfbDto> fj_List)
	{
		this.fj_List = fj_List;
	}
	public List<String> getFjids()
	{
		return fjids;
	}
	public void setFjids(List<String> fjids)
	{
		this.fjids = fjids;
	}
	public String getColor()
	{
		return color;
	}
	public void setColor(String color)
	{
		this.color = color;
	}
	public String getLyry()
	{
		return lyry;
	}
	public void setLyry(String lyry)
	{
		this.lyry = lyry;
	}
	public String getLysj()
	{
		return lysj;
	}
	public void setLysj(String lysj)
	{
		this.lysj = lysj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
