package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="WklcznModel")
public class WklcznModel extends BaseModel {

    private String wkcxid;
    private String znid;

    public String getWkcxid() {
        return wkcxid;
    }

    public void setWkcxid(String wkcxid) {
        this.wkcxid = wkcxid;
    }

    public String getZnid() {
        return znid;
    }

    public void setZnid(String znid) {
        this.znid = znid;
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
