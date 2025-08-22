package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;


@Alias(value="YszkmxDto")
public class YszkmxDto extends YszkmxModel {
    private String xsid;
    private String dkje;
    private String oaxsdh;

    public String getXsid() {
        return xsid;
    }

    public void setXsid(String xsid) {
        this.xsid = xsid;
    }

    public String getDkje() {
        return dkje;
    }

    public void setDkje(String dkje) {
        this.dkje = dkje;
    }

    public String getOaxsdh() {
        return oaxsdh;
    }

    public void setOaxsdh(String oaxsdh) {
        this.oaxsdh = oaxsdh;
    }

    private static final long serialVersionUID = 1L;

}

