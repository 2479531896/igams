package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="WknyfxModel")
public class WknyfxModel extends BaseModel {

    private String wkcxid;
    private String nyjgid;

    public String getWkcxid() {
        return wkcxid;
    }

    public void setWkcxid(String wkcxid) {
        this.wkcxid = wkcxid;
    }

    public String getNyjgid() {
        return nyjgid;
    }

    public void setNyjgid(String nyjgid) {
        this.nyjgid = nyjgid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
