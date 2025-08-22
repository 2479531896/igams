package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="GrksmxModel")
public class GrksmxModel extends BaseModel {
    //个人考试明细ID
    private String grksmxid;
    //个人考试ID
    private String grksid;
    //人员ID
    private String ryid;
    //工作ID
    private String gzid;
    //考试ID
    private String ksid;
    //答题开始时间
    private String dtkssj;
    //答题结束时间
    private String dtjssj;
    //答题结果
    private String dtjg;
    //得分
    private String df;
    //序号
    private String xh;

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getGrksmxid() {
        return grksmxid;
    }

    public void setGrksmxid(String grksmxid) {
        this.grksmxid = grksmxid;
    }

    public String getDtkssj() {
        return dtkssj;
    }

    public void setDtkssj(String dtkssj) {
        this.dtkssj = dtkssj;
    }

    public String getDtjssj() {
        return dtjssj;
    }

    public void setDtjssj(String dtjssj) {
        this.dtjssj = dtjssj;
    }

    public String getGrksid() {
        return grksid;
    }

    public void setGrksid(String grksid) {
        this.grksid = grksid;
    }

    public String getRyid() {
        return ryid;
    }

    public void setRyid(String ryid) {
        this.ryid = ryid;
    }

    public String getGzid() {
        return gzid;
    }

    public void setGzid(String gzid) {
        this.gzid = gzid;
    }

    public String getKsid() {
        return ksid;
    }

    public void setKsid(String ksid) {
        this.ksid = ksid;
    }

    public String getDtjg() {
        return dtjg;
    }

    public void setDtjg(String dtjg) {
        this.dtjg = dtjg;
    }

    public String getDf() {
        return df;
    }

    public void setDf(String df) {
        this.df = df;
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
