package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="YsmxModel")
public class YsmxModel extends BaseModel{
	//预算明细ID
	private String ysmxid;
	//预算管理ID
	private String ysglid;
	//科目大类
	private String kmdl;
	//科目分类
	private String kmfl;
	//预算金额
	private String ysje;

	public String getYsglid() {
		return ysglid;
	}

	public void setYsglid(String ysglid) {
		this.ysglid = ysglid;
	}

	public String getYsmxid() {
		return ysmxid;
	}

	public void setYsmxid(String ysmxid) {
		this.ysmxid = ysmxid;
	}

	public String getKmdl() {
		return kmdl;
	}

	public void setKmdl(String kmdl) {
		this.kmdl = kmdl;
	}

	public String getKmfl() {
		return kmfl;
	}

	public void setKmfl(String kmfl) {
		this.kmfl = kmfl;
	}

	public String getYsje() {
		return ysje;
	}

	public void setYsje(String ysje) {
		this.ysje = ysje;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
