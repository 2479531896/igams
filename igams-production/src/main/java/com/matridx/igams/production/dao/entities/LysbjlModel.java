package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "LysbjlModel")
public class LysbjlModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    //设备记录id
    private String sbjlid;
    //设备id
    private String sbid;
    //记录时间
    private String jlsj;
    //记录人员
    private String jlry;
    //温度
    private String wd;
    //备注
    private String bz;
    //湿度
    private String sd;

    public String getSd() {
        return sd;
    }

    public void setSd(String sd) {
        this.sd = sd;
    }

    public String getSbjlid() {
        return sbjlid;
    }

    public void setSbjlid(String sbjlid) {
        this.sbjlid = sbjlid;
    }

    public String getSbid() {
        return sbid;
    }

    public void setSbid(String sbid) {
        this.sbid = sbid;
    }

    public String getJlsj() {
        return jlsj;
    }

    public void setJlsj(String jlsj) {
        this.jlsj = jlsj;
    }

    public String getJlry() {
        return jlry;
    }

    public void setJlry(String jlry) {
        this.jlry = jlry;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
