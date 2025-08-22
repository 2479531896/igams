package com.matridx.igams.bioinformation.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="CnvjgxqDto")
public class CnvjgxqDto extends CnvjgxqModel {
    //遗传病起始位置
    private String xqqswz;
    //遗传病终止位置
    private String xqzzwz;
    //描述
    private String ms;
    //文库编号
    private String wkbh;
    //核型
    private String karyotype;
    //文库测序ID
    private String wkcxid;
    //癌基因数量
    private String ajysl;
    private List<String> cnvjgids;
    private String jym;
    private String wzzs;
    private List<CnvjgxqDto> ajyList;
    //名称
    private String mc;

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getAjysl() {
        return ajysl;
    }

    public void setAjysl(String ajysl) {
        this.ajysl = ajysl;
    }

    public List<String> getCnvjgids() {
        return cnvjgids;
    }

    public void setCnvjgids(List<String> cnvjgids) {
        this.cnvjgids = cnvjgids;
    }


    public String getWkcxid() {
        return wkcxid;
    }

    public void setWkcxid(String wkcxid) {
        this.wkcxid = wkcxid;
    }

    public String getKaryotype() {
        return karyotype;
    }

    public void setKaryotype(String karyotype) {
        this.karyotype = karyotype;
    }

    public String getWkbh() {
        return wkbh;
    }

    public void setWkbh(String wkbh) {
        this.wkbh = wkbh;
    }

    public String getXqqswz() {
        return xqqswz;
    }

    public void setXqqswz(String xqqswz) {
        this.xqqswz = xqqswz;
    }

    public String getXqzzwz() {
        return xqzzwz;
    }

    public void setXqzzwz(String xqzzwz) {
        this.xqzzwz = xqzzwz;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getJym() {
        return jym;
    }

    public void setJym(String jym) {
        this.jym = jym;
    }

    public String getWzzs() {
        return wzzs;
    }

    public void setWzzs(String wzzs) {
        this.wzzs = wzzs;
    }

    public List<CnvjgxqDto> getAjyList() {
        return ajyList;
    }

    public void setAjyList(List<CnvjgxqDto> ajyList) {
        this.ajyList = ajyList;
    }
}
