package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="CnvjgModel")
public class CnvjgModel extends BaseModel {
    //cnv结果ID
    private String cnvjgid;
    //文库编号
    private String wkbh;
    //文库测序ID
    private String wkcxid;
    //CNV数据量
    private String sjl;
    //CNV分析比对上的序列数
    private String bdxls;
    //唯一比对序列数
    private String uniqxls;
    //GC含量
    private String gchl;
    //波动性
    private String waviness;
    //核型
    private String karyotype;
    // CNV分析得到的性别
    private String xb;
    //已知的该样本肿瘤结果
    private String zszl;
    //肿瘤审核结果
    private String shjg;
    //ai判断结果
    private String aijg;
    //审核时判定的样本的异常情况
    private String ybtz;
    //是否已审核
    private String sfsh;
    // 备注
    private String shbz;
    //是否已发报告
    private String sfyfbg;
    //ai给出的qc分数
    private String qcfs;
    //判断原因
    private String pdyy;
    //比对率
    private String bdl;
    //aijg的值
    private String onresult;
    //审核人员
    private String shry;
    //检验人员
    private String jyry;

    public String getShry() {
        return shry;
    }

    public void setShry(String shry) {
        this.shry = shry;
    }

    public String getJyry() {
        return jyry;
    }

    public void setJyry(String jyry) {
        this.jyry = jyry;
    }

    public String getBdl() {
        return bdl;
    }

    public void setBdl(String bdl) {
        this.bdl = bdl;
    }

    public String getPdyy() {
        return pdyy;
    }

    public void setPdyy(String pdyy) {
        this.pdyy = pdyy;
    }

    public String getSfsh() {
        return sfsh;
    }

    public void setSfsh(String sfsh) {
        this.sfsh = sfsh;
    }

    public String getCnvjgid() {
        return cnvjgid;
    }

    public void setCnvjgid(String cnvjgid) {
        this.cnvjgid = cnvjgid;
    }

    public String getWkbh() {
        return wkbh;
    }

    public void setWkbh(String wkbh) {
        this.wkbh = wkbh;
    }

    public String getWkcxid() {
        return wkcxid;
    }

    public void setWkcxid(String wkcxid) {
        this.wkcxid = wkcxid;
    }

    public String getSjl() {
        return sjl;
    }

    public void setSjl(String sjl) {
        this.sjl = sjl;
    }

    public String getBdxls() {
        return bdxls;
    }

    public void setBdxls(String bdxls) {
        this.bdxls = bdxls;
    }

    public String getUniqxls() {
        return uniqxls;
    }

    public void setUniqxls(String uniqxls) {
        this.uniqxls = uniqxls;
    }

    public String getGchl() {
        return gchl;
    }

    public void setGchl(String gchl) {
        this.gchl = gchl;
    }

    public String getWaviness() {
        return waviness;
    }

    public void setWaviness(String waviness) {
        this.waviness = waviness;
    }

    public String getKaryotype() {
        return karyotype;
    }

    public void setKaryotype(String karyotype) {
        this.karyotype = karyotype;
    }

    public String getXb() {
        return xb;
    }

    public void setXb(String xb) {
        this.xb = xb;
    }

    public String getZszl() {
        return zszl;
    }

    public void setZszl(String zszl) {
        this.zszl = zszl;
    }

    public String getShjg() {
        return shjg;
    }

    public void setShjg(String shjg) {
        this.shjg = shjg;
    }

    public String getAijg() {
        return aijg;
    }

    public void setAijg(String aijg) {
        this.aijg = aijg;
    }

    public String getYbtz() {
        return ybtz;
    }

    public void setYbtz(String ybtz) {
        this.ybtz = ybtz;
    }

    public String getShbz() {
        return shbz;
    }

    public void setShbz(String shbz) {
        this.shbz = shbz;
    }

    public String getSfyfbg() {
        return sfyfbg;
    }

    public void setSfyfbg(String sfyfbg) {
        this.sfyfbg = sfyfbg;
    }

    public String getQcfs() {
        return qcfs;
    }

    public void setQcfs(String qcfs) {
        this.qcfs = qcfs;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String getOnresult() {
        return onresult;
    }

    public void setOnresult(String onresult) {
        this.onresult = onresult;
    }
}
