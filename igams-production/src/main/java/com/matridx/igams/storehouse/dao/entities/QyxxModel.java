package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="QyxxModel")
public class QyxxModel extends BaseBasicModel {
    private static final long serialVersionUID = 1L;
    private String qyid;//取样id
    private String lykcid;//留样库存id
    private String qyl;//取样量
    private String qyr;//取样人
    private String yt;//用途
    private String bz;//备注
    private String qyxj;//取样小结

    public String getQyid() {
        return qyid;
    }

    public void setQyid(String qyid) {
        this.qyid = qyid;
    }

    public String getLykcid() {
        return lykcid;
    }

    public void setLykcid(String lykcid) {
        this.lykcid = lykcid;
    }

    public String getQyl() {
        return qyl;
    }

    public void setQyl(String qyl) {
        this.qyl = qyl;
    }

    public String getQyr() {
        return qyr;
    }

    public void setQyr(String qyr) {
        this.qyr = qyr;
    }

    public String getYt() {
        return yt;
    }

    public void setYt(String yt) {
        this.yt = yt;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getQyxj() {
        return qyxj;
    }

    public void setQyxj(String qyxj) {
        this.qyxj = qyxj;
    }
}
