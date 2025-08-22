package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="PxqxszModel")
public class PxqxszModel extends BaseModel {
    //培训id
    private String pxid;
    //培训权限ID
    private String pxqxid;
    //机构ID
    private String jgid;

    public String getPxid() {
        return pxid;
    }

    public void setPxid(String pxid) {
        this.pxid = pxid;
    }

    public String getPxqxid() {
        return pxqxid;
    }

    public void setPxqxid(String pxqxid) {
        this.pxqxid = pxqxid;
    }

    public String getJgid() {
        return jgid;
    }

    public void setJgid(String jgid) {
        this.jgid = jgid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
