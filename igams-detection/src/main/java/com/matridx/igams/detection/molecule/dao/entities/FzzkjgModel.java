package com.matridx.igams.detection.molecule.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="FzzkjgModel")
public class FzzkjgModel extends BaseModel{

	//分子质控结果ID
	private String fzzkjgid;
	//质控名称，实验号等
	private String zkmc;
	//关联ID，关联fzjcjg的glid
	private String glid;
	//CT值
	private String ctz;
	//普检结果
	private String jgid;

	public String getFzzkjgid() {
		return fzzkjgid;
	}

	public void setFzzkjgid(String fzzkjgid) {
		this.fzzkjgid = fzzkjgid;
	}

	public String getZkmc() {
		return zkmc;
	}

	public void setZkmc(String zkmc) {
		this.zkmc = zkmc;
	}

	public String getGlid() {
		return glid;
	}

	public void setGlid(String glid) {
		this.glid = glid;
	}

	public String getCtz() {
		return ctz;
	}

	public void setCtz(String ctz) {
		this.ctz = ctz;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
}
