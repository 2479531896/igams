package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("GrjxmxModel")
public class GrjxmxModel extends BaseModel {
    private String grjxmxid;//个人绩效明细id
    private String grjxid;//个人绩效id
    private String fs;//分数
    private String zj;//总结
    private String jxmbid;//绩效模板id
    private String jxmbmxid;//绩效模板明细id
    private String gwid;//岗位id
    private String shr;//审核人

    public String getShr() {
        return shr;
    }

    public void setShr(String shr) {
        this.shr = shr;
    }

    public String getGwid() {
        return gwid;
    }

    public void setGwid(String gwid) {
        this.gwid = gwid;
    }

    public String getGrjxmxid() {
        return grjxmxid;
    }

    public void setGrjxmxid(String grjxmxid) {
        this.grjxmxid = grjxmxid;
    }

    public String getGrjxid() {
        return grjxid;
    }

    public void setGrjxid(String grjxid) {
        this.grjxid = grjxid;
    }

    public String getFs() {
        return fs;
    }

    public void setFs(String fs) {
        this.fs = fs;
    }

    public String getZj() {
        return zj;
    }

    public void setZj(String zj) {
        this.zj = zj;
    }

    public String getJxmbid() {
        return jxmbid;
    }

    public void setJxmbid(String jxmbid) {
        this.jxmbid = jxmbid;
    }

    public String getJxmbmxid() {
        return jxmbmxid;
    }

    public void setJxmbmxid(String jxmbmxid) {
        this.jxmbmxid = jxmbmxid;
    }
    private static final long serialVersionUID = 1L;
}
