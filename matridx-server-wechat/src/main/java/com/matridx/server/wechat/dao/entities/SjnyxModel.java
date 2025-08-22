package com.matridx.server.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjnyxModel")
public class SjnyxModel extends BaseModel{
	//送检ID
	private String sjid;
	//序号
	private String xh;
	//基因
	private String jy;
	//基因类
	private String jyl;
	//特性
	private String tx;
	//机理
	private String jl;
	//读数
	private String ds;
	//药品
	private String yp;
	//相关物种
	private String xgwz;
	//检测类型
	private String jclx;
	//检测子类型
	private String jczlx;
	//基因分型
	private String jyfx;
	//序列数
	private String xls;
	
	public String getJclx() {
		return jclx;
	}
	public void setJclx(String jclx) {
		this.jclx = jclx;
	}
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//基因
	public String getJy() {
		return jy;
	}
	public void setJy(String jy){
		this.jy = jy;
	}
	//基因类
	public String getJyl() {
		return jyl;
	}
	public void setJyl(String jyl){
		this.jyl = jyl;
	}
	//特性
	public String getTx() {
		return tx;
	}
	public void setTx(String tx){
		this.tx = tx;
	}
	//机理
	public String getJl() {
		return jl;
	}
	public void setJl(String jl){
		this.jl = jl;
	}
	//读数
	public String getDs() {
		return ds;
	}
	public void setDs(String ds){
		this.ds = ds;
	}
	//药物
	public String getYp() {
		return yp;
	}
	public void setYp(String yp){
		this.yp = yp;
	}
	//相关物种
	public String getXgwz() {
		return xgwz;
	}
	public void setXgwz(String xgwz){
		this.xgwz = xgwz;
	}
	//序号
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getJyfx() {
		return jyfx;
	}

	public void setJyfx(String jyfx) {
		this.jyfx = jyfx;
	}

	public String getXls() {
		return xls;
	}

	public void setXls(String xls) {
		this.xls = xls;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public String getJczlx() {
        return jczlx;
    }

    public void setJczlx(String jczlx) {
        this.jczlx = jczlx;
    }
}
