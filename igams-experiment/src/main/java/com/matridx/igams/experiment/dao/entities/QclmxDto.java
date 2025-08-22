package com.matridx.igams.experiment.dao.entities;
import org.apache.ibatis.type.Alias;
@Alias(value="QclmxDto")
public class QclmxDto extends QclmxModel{
	//科学记数法（细胞计数）
	private String format_xbjs;
	
	public String getFormat_xbjs()
	{
		return format_xbjs;
	}


	public void setFormat_xbjs(String format_xbjs)
	{
		this.format_xbjs = format_xbjs;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
