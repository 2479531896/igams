package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="SjlcznModel")
public class SjlcznModel extends BaseModel{
	//送检ID
	private String sjid;
	//指南ID
	private String znid;
	//物种ID
	private String wzid;
	//检测类型
	private String jclx;
	//检测子类型
	private String jczlx;
	//送检ID
	public String getSjid() {
		return sjid;
	}
	public void setSjid(String sjid){
		this.sjid = sjid;
	}
	//指南ID
	public String getZnid() {
		return znid;
	}
	public void setZnid(String znid){
		this.znid = znid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public String getWzid() {
        return wzid;
    }

    public void setWzid(String wzid) {
        this.wzid = wzid;
    }

    public String getJclx() {
        return jclx;
    }

    public void setJclx(String jclx) {
        this.jclx = jclx;
    }

    public String getJczlx() {
        return jczlx;
    }

    public void setJczlx(String jczlx) {
        this.jczlx = jczlx;
    }
}
