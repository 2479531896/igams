package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/*
 *@date 2022年08月30日18:13
 *
 */
@Alias(value="ZjszModel")
public class ZjszModel extends BaseModel {
    private String zjszid;	//质检设置id
    private String lykcid;	//留样库存id
    private String kssj;	//开始时间
    private String jssj;	//结束时间
    private String zjry;	//质检人员
    private String gzid;	//工作id

    public String getZjszid() {
        return zjszid;
    }

    public void setZjszid(String zjszid) {
        this.zjszid = zjszid;
    }

    public String getLykcid() {
        return lykcid;
    }

    public void setLykcid(String lykcid) {
        this.lykcid = lykcid;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getZjry() {
        return zjry;
    }

    public void setZjry(String zjry) {
        this.zjry = zjry;
    }

    public String getGzid() {
        return gzid;
    }

    public void setGzid(String gzid) {
        this.gzid = gzid;
    }

    private static final long serialVersionUID = 1L;
}
