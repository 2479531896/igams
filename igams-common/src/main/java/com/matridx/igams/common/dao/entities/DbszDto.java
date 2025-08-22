package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="DbszDto")
public class DbszDto extends DbszModel{
	private String dbsz_json;

	public String getDbsz_json() {
		return dbsz_json;
	}

	public void setDbsz_json(String dbsz_json) {
		this.dbsz_json = dbsz_json;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
