package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="JnsjjcjgModel")
public class JnsjjcjgModel extends BaseModel{

	private static final long serialVersionUID = 1L;
	//艰难梭菌检测结果id
	private String jnsjjcjgid;
	//艰难梭菌报告记录id
	private String jnsjbgid;
	//艰难梭菌检测时间
	private String jnsjjcsj;
	//GDH结果
	private String gdhjg;
	//TOXIN结果
	private String toxinjg;
	//大便艰难梭菌培养结果
	private String dbjnsjpyjg;
	//艰难梭菌毒素培养结果
	private String jnsjmspyjg;
	//
	private String scbj;

	//艰难梭菌报告id 
	public String getJnsjbgid() { return jnsjbgid; }
	public void setJnsjbgid(String jnsjbgid) { this.jnsjbgid = jnsjbgid; }
	//艰难梭菌检测结果id
	public String getJnsjjcjgid() { return jnsjjcjgid; }
	public void setJnsjjcjgid(String jnsjjcjgid) { this.jnsjjcjgid = jnsjjcjgid; }
	//艰难梭菌检测时间
	public String getJnsjjcsj() { return jnsjjcsj; }
	public void setJnsjjcsj(String jnsjjcsj) { this.jnsjjcsj = jnsjjcsj; }
	//GDH结果
	public String getGdhjg() {
		return gdhjg;
	}
	public void setGdhjg(String gdhjg){
		this.gdhjg = gdhjg;
	}
	//TOXIN结果
	public String getToxinjg() {
		return toxinjg;
	}
	public void setToxinjg(String toxinjg){
		this.toxinjg = toxinjg;
	}
	//大便艰难梭菌培养结果
	public String getDbjnsjpyjg() {
		return dbjnsjpyjg;
	}
	public void setDbjnsjpyjg(String dbjnsjpyjg){
		this.dbjnsjpyjg = dbjnsjpyjg;
	}
	//艰难梭菌毒素培养结果
	public String getJnsjmspyjg() {
		return jnsjmspyjg;
	}
	public void setJnsjmspyjg(String jnsjmspyjg){
		this.jnsjmspyjg = jnsjmspyjg;
	}

	@Override
	public String getScbj() {
		return scbj;
	}

	@Override
	public void setScbj(String scbj) {
		this.scbj = scbj;
	}
}
