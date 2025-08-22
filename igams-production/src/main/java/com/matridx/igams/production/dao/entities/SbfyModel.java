package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author : 郭祥杰
 * @date :
 */
@Alias("SbfyModel")
public class SbfyModel extends BaseBasicModel {
    private String sbfyid; //设备费用id
    private String sbysid; //设备验收id
    private String fylx; //费用类型
    private String bm; //部门
    private String nr; //内容
    private String je; //金额
    private String sj; //时间

    public String getSbfyid() {
        return sbfyid;
    }

    public void setSbfyid(String sbfyid) {
        this.sbfyid = sbfyid;
    }

    public String getSbysid() {
        return sbysid;
    }

    public void setSbysid(String sbysid) {
        this.sbysid = sbysid;
    }

    public String getFylx() {
        return fylx;
    }

    public void setFylx(String fylx) {
        this.fylx = fylx;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getJe() {
        return je;
    }

    public void setJe(String je) {
        this.je = je;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }
}
