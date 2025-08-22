package com.matridx.igams.detection.molecule.dao.entities;

import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.BaseModel;

@Alias(value="FzjcjgModel")
public class FzjcjgModel extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//分子检测结果ID
	private String fzjcjgid;
	//分子项目ID
	private String fzxmid;
	//检测结果
	private String jcjg;
	//分子检测ID
	private String fzjcid;
	//CT值
	private String ctz;
	//结果ID
	private String jgid;
	//结果参数扩展1
	private String jgcskz1;
	//关联ID
	private String glid;

	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}

	public String getJgcskz1() {
		return jgcskz1;
	}

	public void setJgcskz1(String jgcskz1) {
		this.jgcskz1 = jgcskz1;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getCtz() {
		return ctz;
	}

	public void setCtz(String ctz) {
		this.ctz = ctz;
	}

	public String getFzjcid() {
		return fzjcid;
	}

	public void setFzjcid(String fzjcid) {
		this.fzjcid = fzjcid;
	}

	public String getFzjcjgid() {
		return fzjcjgid;
	}

	public void setFzjcjgid(String fzjcjgid) {
		this.fzjcjgid = fzjcjgid;
	}

	public String getFzxmid() {
		return fzxmid;
	}

	public void setFzxmid(String fzxmid) {
		this.fzxmid = fzxmid;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}
}
