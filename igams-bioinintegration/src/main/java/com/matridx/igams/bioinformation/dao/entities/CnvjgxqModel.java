package com.matridx.igams.bioinformation.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="CnvjgxqModel")
public class CnvjgxqModel extends BaseModel {
    //cnv结果详情id
    private String cnvjgxqid;
    private String cnvjgid;
    // CNV所在染色体
    private String rst;
    // CNV起始区带
    private String qsqd;
    //CNV终止区带
    private String zzqd;
    //CNV类型（dup/del)
    private String cnvlx;
    //是否嵌合
    private String sfqh;
    //是否非整倍体
    private String sffzsb;
    //起始位置 int CNV起始位置坐标
    private String qswz;
    //CNV终止位置坐标
    private String zzwz;
    //CNV拷贝数
    private String kbs;
    // 用于展示的CNV结果
    private String cnvjg;
    //用于展示的CNV详情
    private String cnvxq;
    //是否报该CNV
    private String sfhb;
    //芯片id
    private String xpid;
    //是否是审核时手动添加的CNV
    private String sfsdtj;

    public String getCnvjgxqid() {
        return cnvjgxqid;
    }

    public void setCnvjgxqid(String cnvjgxqid) {
        this.cnvjgxqid = cnvjgxqid;
    }

    public String getCnvjgid() {
        return cnvjgid;
    }

    public void setCnvjgid(String cnvjgid) {
        this.cnvjgid = cnvjgid;
    }

    public String getRst() {
        return rst;
    }

    public void setRst(String rst) {
        this.rst = rst;
    }

    public String getQsqd() {
        return qsqd;
    }

    public void setQsqd(String qsqd) {
        this.qsqd = qsqd;
    }

    public String getZzqd() {
        return zzqd;
    }

    public void setZzqd(String zzqd) {
        this.zzqd = zzqd;
    }

    public String getCnvlx() {
        return cnvlx;
    }

    public void setCnvlx(String cnvlx) {
        this.cnvlx = cnvlx;
    }

    public String getSfqh() {
        return sfqh;
    }

    public void setSfqh(String sfqh) {
        this.sfqh = sfqh;
    }

    public String getSffzsb() {
        return sffzsb;
    }

    public void setSffzsb(String sffzsb) {
        this.sffzsb = sffzsb;
    }

    public String getQswz() {
        return qswz;
    }

    public void setQswz(String qswz) {
        this.qswz = qswz;
    }

    public String getZzwz() {
        return zzwz;
    }

    public void setZzwz(String zzwz) {
        this.zzwz = zzwz;
    }

    public String getKbs() {
        return kbs;
    }

    public void setKbs(String kbs) {
        this.kbs = kbs;
    }

    public String getCnvjg() {
        return cnvjg;
    }

    public void setCnvjg(String cnvjg) {
        this.cnvjg = cnvjg;
    }

    public String getCnvxq() {
        return cnvxq;
    }

    public void setCnvxq(String cnvxq) {
        this.cnvxq = cnvxq;
    }

    public String getSfhb() {
        return sfhb;
    }

    public void setSfhb(String sfhb) {
        this.sfhb = sfhb;
    }

    public String getXpid() {
        return xpid;
    }

    public void setXpid(String xpid) {
        this.xpid = xpid;
    }

    public String getSfsdtj() {
        return sfsdtj;
    }

    public void setSfsdtj(String sfsdtj) {
        this.sfsdtj = sfsdtj;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
