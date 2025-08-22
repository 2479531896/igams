package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="WkysmxModel")
public class WkysmxModel extends BaseModel {
    private String wkcxid;
    private String mxjgid;

    public String getWkcxid() {
        return wkcxid;
    }

    public void setWkcxid(String wkcxid) {
        this.wkcxid = wkcxid;
    }

    public String getMxjgid() {
        return mxjgid;
    }

    public void setMxjgid(String mxjgid) {
        this.mxjgid = mxjgid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
