package com.matridx.igams.production.dao.entities;


import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.BaseBasicModel;

/**
 * @program: igams
 * @description:
 * @create: 2022-08-01 18:42
 **/
@Alias(value = "CljlxxModel")
public class CljlxxModel  extends BaseBasicModel{
	private static final long serialVersionUID = 1L;
    //处理记录id
    private String cljlid;
    //领料id
    private String llid;
    //是否结束，0否1是
    private String sfjs;
    //备注
    private String bz;
    //售后反馈id
    private String shfkid;

    public String getCljlid() {
        return cljlid;
    }

    public void setCljlid(String cljlid) {
        this.cljlid = cljlid;
    }

    public String getLlid() {
        return llid;
    }

    public void setLlid(String llid) {
        this.llid = llid;
    }

    public String getSfjs() {
        return sfjs;
    }

    public void setSfjs(String sfjs) {
        this.sfjs = sfjs;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getShfkid() {
        return shfkid;
    }

    public void setShfkid(String shfkid) {
        this.shfkid = shfkid;
    }


}
