package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="WkdlfxModel")
public class WkdlfxModel extends BaseModel {

    private String wkcxid;
    private String dlfxid;

    public String getWkcxid() {
        return wkcxid;
    }

    public void setWkcxid(String wkcxid) {
        this.wkcxid = wkcxid;
    }

    public String getDlfxid() {
        return dlfxid;
    }

    public void setDlfxid(String dlfxid) {
        this.dlfxid = dlfxid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
