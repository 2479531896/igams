package com.matridx.igams.hrm.dao.entities;


import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="GrksglModel")
public class GrksglModel extends BaseModel {
    //个人考试ID
    private String grksid;
    //培训ID
    private String pxid;
    //人员ID
    private String ryid;
    //工作ID
    private String gzid;
    //考试开始时间
    private String kskssj;
    //考试结束时间
    private String ksjssj;
    //总分
    private String zf;

    public String getGrksid() {
        return grksid;
    }

    public void setGrksid(String grksid) {
        this.grksid = grksid;
    }

    public String getPxid() {
        return pxid;
    }

    public void setPxid(String pxid) {
        this.pxid = pxid;
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

    public String getKskssj() {
        return kskssj;
    }

    public void setKskssj(String kskssj) {
        this.kskssj = kskssj;
    }

    public String getKsjssj() {
        return ksjssj;
    }

    public void setKsjssj(String ksjssj) {
        this.ksjssj = ksjssj;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
