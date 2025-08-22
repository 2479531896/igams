package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "Mngszjg2Model")
public class Mngszjg2Model extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String jgid;
    private String wkbh;
    private String wkcxid;
    private String finaljson;

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getWkbh() {
		return wkbh;
	}

	public void setWkbh(String wkbh) {
		this.wkbh = wkbh;
	}

	public String getWkcxid() {
		return wkcxid;
	}

	public void setWkcxid(String wkcxid) {
		this.wkcxid = wkcxid;
	}

	public String getFinaljson() {
		return finaljson;
	}

	public void setFinaljson(String finaljson) {
		this.finaljson = finaljson;
	}
}
