package com.matridx.igams.experiment.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="RwmbDto")
public class RwmbDto extends RwmbModel{
	//任务标签名称
	private String rwbqmc;
	//任务级别名称
	private String rejbmc;
	//子任务条数
	private String zrwcount;
	//负责人名称
	private String fzrmc;
	//文件类型
	private String ywlx;
	//附件ID复数
	private List<String> fjids;
	
	public String getFzrmc()
	{
		return fzrmc;
	}

	public void setFzrmc(String fzrmc)
	{
		this.fzrmc = fzrmc;
	}

	public String getZrwcount()
	{
		return zrwcount;
	}

	public void setZrwcount(String zrwcount)
	{
		this.zrwcount = zrwcount;
	}
	public String getRwbqmc()
	{
		return rwbqmc;
	}


	public void setRwbqmc(String rwbqmc)
	{
		this.rwbqmc = rwbqmc;
	}


	public String getRejbmc()
	{
		return rejbmc;
	}


	public void setRejbmc(String rejbmc)
	{
		this.rejbmc = rejbmc;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
