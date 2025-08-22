package com.matridx.igams.experiment.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhJkcsModel")
public class ZdhJkcsModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    private String jkcsid;
    private String ybxxid;
    private String cs;
    private String yqid;

    public String getYqid() {
        return yqid;
    }

    public void setYqid(String yqid) {
        this.yqid = yqid;
    }

    public String getJkcsid() {
        return jkcsid;
    }

    public void setJkcsid(String jkcsid) {
        this.jkcsid = jkcsid;
    }

    public String getYbxxid() {
        return ybxxid;
    }

    public void setYbxxid(String ybxxid) {
        this.ybxxid = ybxxid;
    }

    public String getCs() {
        return cs;
    }

    public void setCs(String cs) {
        this.cs = cs;
    }
}
