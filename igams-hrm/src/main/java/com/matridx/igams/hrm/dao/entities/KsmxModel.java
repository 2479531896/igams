package com.matridx.igams.hrm.dao.entities;


import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="KsmxModel")
public class KsmxModel  extends BaseModel {
    //考试明细ID
    private String ksmxid;
    //考试ID
    private String ksid;
    //选项代码
    private String xxdm;
    //选项内容
    private String xxnr;

    public String getKsmxid() {
        return ksmxid;
    }

    public void setKsmxid(String ksmxid) {
        this.ksmxid = ksmxid;
    }

    public String getKsid() {
        return ksid;
    }

    public void setKsid(String ksid) {
        this.ksid = ksid;
    }

    public String getXxdm() {
        return xxdm;
    }

    public void setXxdm(String xxdm) {
        this.xxdm = xxdm;
    }

    public String getXxnr() {
        return xxnr;
    }

    public void setXxnr(String xxnr) {
        this.xxnr = xxnr;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
