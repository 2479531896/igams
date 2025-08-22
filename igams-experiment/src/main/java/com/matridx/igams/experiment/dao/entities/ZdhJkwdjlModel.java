package com.matridx.igams.experiment.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias(value="ZdhJkwdjlModel")
public class ZdhJkwdjlModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    private String wdjlid;
    private String ybxxid;
    private String wd;
    private String sj;
    private String yqid;

    public String getYqid() {
        return yqid;
    }

    public void setYqid(String yqid) {
        this.yqid = yqid;
    }

    public String getWdjlid() {
        return wdjlid;
    }

    public void setWdjlid(String wdjlid) {
        this.wdjlid = wdjlid;
    }

    public String getYbxxid() {
        return ybxxid;
    }

    public void setYbxxid(String ybxxid) {
        this.ybxxid = ybxxid;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }
}
