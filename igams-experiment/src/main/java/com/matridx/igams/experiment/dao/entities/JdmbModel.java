package com.matridx.igams.experiment.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JdmbModel")
public class JdmbModel extends BaseModel{
	//阶段ID
	private String jdid;
	//模板ID
	private String mbid;
	//序号
	private String xh;
	//阶段名称
	private String jdmc;
	//父阶段ID
	private String fjdid;
	//阶段类型  阶段或者任务
	private String jdlx;
	//显示标记  0:不显示  1：显示
	private String xsbj;
	//研发类型 不同的类型有不同的模板 ，暂不用
	private String yflx;
	//阶层  1：第一层  2：第二层
	private String jc;
	//默认负责人
	private String mrfzr;
	//期限
	private String qx;
	//扩展参数 用于标识特殊阶段，比如立项等
	private String kzcs;
	//阶段任务
	private List<RwmbDto> rwmbmc;
	
	public List<RwmbDto> getRwmbmc()
	{
		return rwmbmc;
	}

	public void setRwmbmc(List<RwmbDto> rwmbmc)
	{
		this.rwmbmc = rwmbmc;
	}
	//阶段ID
	public String getJdid() {
		return jdid;
	}
	public void setJdid(String jdid){
		this.jdid = jdid;
	}
	//模板ID
	public String getMbid() {
		return mbid;
	}
	public void setMbid(String mbid){
		this.mbid = mbid;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh){
		this.xh = xh;
	}
	//阶段名称
	public String getJdmc() {
		return jdmc;
	}
	public void setJdmc(String jdmc){
		this.jdmc = jdmc;
	}
	//父阶段ID
	public String getFjdid() {
		return fjdid;
	}
	public void setFjdid(String fjdid){
		this.fjdid = fjdid;
	}
	//阶段类型  阶段或者任务
	public String getJdlx() {
		return jdlx;
	}
	public void setJdlx(String jdlx){
		this.jdlx = jdlx;
	}
	//显示标记  0:不显示  1：显示
	public String getXsbj() {
		return xsbj;
	}
	public void setXsbj(String xsbj){
		this.xsbj = xsbj;
	}
	//研发类型 不同的类型有不同的模板 ，暂不用
	public String getYflx() {
		return yflx;
	}
	public void setYflx(String yflx){
		this.yflx = yflx;
	}
	//阶层  1：第一层  2：第二层
	public String getJc() {
		return jc;
	}
	public void setJc(String jc){
		this.jc = jc;
	}
	//默认负责人
	public String getMrfzr() {
		return mrfzr;
	}
	public void setMrfzr(String mrfzr){
		this.mrfzr = mrfzr;
	}
	//期限
	public String getQx() {
		return qx;
	}
	public void setQx(String qx){
		this.qx = qx;
	}
	//扩展参数 用于标识特殊阶段，比如立项等
	public String getKzcs() {
		return kzcs;
	}
	public void setKzcs(String kzcs){
		this.kzcs = kzcs;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
