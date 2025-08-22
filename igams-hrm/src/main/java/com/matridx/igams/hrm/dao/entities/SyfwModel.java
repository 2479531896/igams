package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("SyfwModel")
public class SyfwModel extends BaseModel {
    private String syfwid;//适用范围id
    private String jxmbid;//绩效模板id
    private String yhid;//用户id
    private String zt;//状态

    public String getSyfwid() {
        return syfwid;
    }

    public void setSyfwid(String syfwid) {
        this.syfwid = syfwid;
    }

    public String getJxmbid() {
        return jxmbid;
    }

    public void setJxmbid(String jxmbid) {
        this.jxmbid = jxmbid;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }
    private static final long serialVersionUID = 1L;

}
