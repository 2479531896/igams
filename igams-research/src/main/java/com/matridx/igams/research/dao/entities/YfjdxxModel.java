package com.matridx.igams.research.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="YfjdxxModel")
public class YfjdxxModel extends BaseModel{
	//研发阶段ID
	private String yfjdid;
	//研发ID
	private String yfid;
	//序号
	private String xh;
	//阶段ID
	private String jdid;
	//阶段名称
	private String jdmc;
	//开始时间 
	private String kssj;
	//期望完成时间
	private String qwwcsj;
	//实际完成时间
	private String sjwcsj;
	//负责人
	private String fzr;
	//研发阶段ID
	public String getYfjdid() {
		return yfjdid;
	}
	public void setYfjdid(String yfjdid){
		this.yfjdid = yfjdid;
	}
	//研发ID
	public String getYfid() {
		return yfid;
	}
	public void setYfid(String yfid){
		this.yfid = yfid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//阶段ID
	public String getJdid() {
		return jdid;
	}
	public void setJdid(String jdid){
		this.jdid = jdid;
	}
	//阶段名称
	public String getJdmc() {
		return jdmc;
	}
	public void setJdmc(String jdmc){
		this.jdmc = jdmc;
	}
	//开始时间 
	public String getKssj() {
		return kssj;
	}
	public void setKssj(String kssj){
		this.kssj = kssj;
	}
	//期望完成时间
	public String getQwwcsj() {
		return qwwcsj;
	}
	public void setQwwcsj(String qwwcsj){
		this.qwwcsj = qwwcsj;
	}
	//实际完成时间
	public String getSjwcsj() {
		return sjwcsj;
	}
	public void setSjwcsj(String sjwcsj){
		this.sjwcsj = sjwcsj;
	}
	//负责人
	public String getFzr() {
		return fzr;
	}
	public void setFzr(String fzr){
		this.fzr = fzr;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
