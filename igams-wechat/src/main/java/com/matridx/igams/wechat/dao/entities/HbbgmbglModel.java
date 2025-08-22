package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="HbbgmbglModel")
public class HbbgmbglModel extends BaseModel{
	//序号
	private String xh;
	//伙伴ID
	private String hbid;
	//检测项目ID
	private String jcxmid;
	//检测子项目ID
	private String jczxmid;
	//报告模板ID
	private String bgmbid;
	//报告模板ID2
	private String bgmbid2;

	public String getBgmbid2() {
		return bgmbid2;
	}

	public void setBgmbid2(String bgmbid2) {
		this.bgmbid2 = bgmbid2;
	}

	public String getBgmbid() {
		return bgmbid;
	}

	public void setBgmbid(String bgmbid) {
		this.bgmbid = bgmbid;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getHbid() {
		return hbid;
	}

	public void setHbid(String hbid) {
		this.hbid = hbid;
	}

	public String getJcxmid() {
		return jcxmid;
	}

	public void setJcxmid(String jcxmid) {
		this.jcxmid = jcxmid;
	}

	public String getJczxmid() {
		return jczxmid;
	}

	public void setJczxmid(String jczxmid) {
		this.jczxmid = jczxmid;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
