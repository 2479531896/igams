package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="JcsqglModel")
public class JcsqglModel extends BaseBasicModel {

	//申请管理ID
	private String sqglid;
	//申请类型
	private String sqlx;
	//合作伙伴
	private String hb;
	//申请原因
	private String sqyy;
	//需求说明
	private String xqsm;
	//类型
	private String lx;
	//状态
	private String zt;
	//样本信息
	private String ybxx;
	//文库信息
	private String wkxx;
	//伙伴信息
	private String hbxx;
	//物种信息
	private String wzxx;

	public String getHbxx() {
		return hbxx;
	}

	public void setHbxx(String hbxx) {
		this.hbxx = hbxx;
	}

	public String getYbxx() {
		return ybxx;
	}

	public void setYbxx(String ybxx) {
		this.ybxx = ybxx;
	}

	public String getWkxx() {
		return wkxx;
	}

	public void setWkxx(String wkxx) {
		this.wkxx = wkxx;
	}

	public String getSqglid() {
		return sqglid;
	}

	public void setSqglid(String sqglid) {
		this.sqglid = sqglid;
	}

	public String getSqlx() {
		return sqlx;
	}

	public void setSqlx(String sqlx) {
		this.sqlx = sqlx;
	}

	public String getHb() {
		return hb;
	}

	public void setHb(String hb) {
		this.hb = hb;
	}

	public String getSqyy() {
		return sqyy;
	}

	public void setSqyy(String sqyy) {
		this.sqyy = sqyy;
	}

	public String getXqsm() {
		return xqsm;
	}

	public void setXqsm(String xqsm) {
		this.xqsm = xqsm;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getWzxx() {
		return wzxx;
	}

	public void setWzxx(String wzxx) {
		this.wzxx = wzxx;
	}
}
