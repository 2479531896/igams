package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="FpmxModel")
public class FpmxModel extends BaseBasicModel {


    //发票明细ID
    private String fpmxid;
    //发票ID
    private String fpid;
    //数量
    private String fpsl;
    //总金额
    private String fpje;


    public String getFpmxid() {
        return fpmxid;
    }

    public void setFpmxid(String fpmxid) {
        this.fpmxid = fpmxid;
    }

    public String getFpid() {
        return fpid;
    }

    public void setFpid(String fpid) {
        this.fpid = fpid;
    }

    public String getFpsl() {
        return fpsl;
    }

    public void setFpsl(String fpsl) {
        this.fpsl = fpsl;
    }

    public String getFpje() {
        return fpje;
    }

    public void setFpje(String fpje) {
        this.fpje = fpje;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
