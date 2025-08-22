package com.matridx.igams.experiment.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="RwlyModel")
public class RwlyModel extends BaseModel{
	//留言ID
	private String lyid;
	//任务ID
	private String rwid;
	//留言信息
	private String lyxx;
	//留言类型
	private String  lylx;
	//录入人员
	private String lrry;
	//修改人员
	private String xgry;
	//删除人员
	private String scry;
	
	public String getLylx()
	{
		return lylx;
	}
	public void setLylx(String lylx)
	{
		this.lylx = lylx;
	}
	public String getLrry()
	{
		return lrry;
	}
	public void setLrry(String lrry)
	{
		this.lrry = lrry;
	}
	public String getXgry()
	{
		return xgry;
	}
	public void setXgry(String xgry)
	{
		this.xgry = xgry;
	}
	public String getScry()
	{
		return scry;
	}
	public void setScry(String scry)
	{
		this.scry = scry;
	}
	//留言ID
	public String getLyid() {
		return lyid;
	}
	public void setLyid(String lyid){
		this.lyid = lyid;
	}
	//任务ID
	public String getRwid() {
		return rwid;
	}
	public void setRwid(String rwid){
		this.rwid = rwid;
	}
	//留言信息
	public String getLyxx() {
		return lyxx;
	}
	public void setLyxx(String lyxx){
		this.lyxx = lyxx;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
