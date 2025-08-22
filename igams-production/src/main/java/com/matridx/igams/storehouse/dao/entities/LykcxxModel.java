package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;


@Alias(value="LykcxxModel")
public class LykcxxModel extends BaseBasicModel {
	//留样库存id
	private String lykcid;
	//物料id
	private String wlid;
	//生产批号
	private String scph;
	//库存量
	private String kcl;
	//生产日期
	private String scrq;
	//有效期
	private String yxq;
	//留样小结
	private String lyxj;
	//上次观察日期
	private String scgcrq;

	public String getScgcrq() {
		return scgcrq;
	}

	public void setScgcrq(String scgcrq) {
		this.scgcrq = scgcrq;
	}
	public String getLykcid() {
		return lykcid;
	}

	public void setLykcid(String lykcid) {
		this.lykcid = lykcid;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getKcl() {
		return kcl;
	}

	public void setKcl(String kcl) {
		this.kcl = kcl;
	}

	public String getScrq() {
		return scrq;
	}

	public void setScrq(String scrq) {
		this.scrq = scrq;
	}

	public String getYxq() {
		return yxq;
	}

	public void setYxq(String yxq) {
		this.yxq = yxq;
	}

	public String getLyxj() {
		return lyxj;
	}

	public void setLyxj(String lyxj) {
		this.lyxj = lyxj;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
