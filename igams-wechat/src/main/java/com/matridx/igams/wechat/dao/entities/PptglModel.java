package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="PptglModel")
public class PptglModel extends BaseModel {

    private String pptglid; //主键id

    private String pptmc;//pptgl名称

    private String bz;//备注


    public String getPptglid() {
        return pptglid;
    }

    public void setPptglid(String pptglid) {
        this.pptglid = pptglid;
    }

    public String getPptmc() {
        return pptmc;
    }

    public void setPptmc(String pptmc) {
        this.pptmc = pptmc;
    }


    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}
