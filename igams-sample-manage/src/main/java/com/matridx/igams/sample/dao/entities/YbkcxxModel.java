package com.matridx.igams.sample.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/*
 *@date 2022年06月06日12:29
 *
 */
@Alias(value="YbkcxxModel")
public class YbkcxxModel extends BaseModel {
	//盒子编号
	private String hzbh;
	//调拨明细id
	private String dbmxid;

	public String getDbmxid() {
		return dbmxid;
	}

	public void setDbmxid(String dbmxid) {
		this.dbmxid = dbmxid;
	}

	public String getHzbh() {
		return hzbh;
	}

	public void setHzbh(String hzbh) {
		this.hzbh = hzbh;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6975105873013232643L;

}
