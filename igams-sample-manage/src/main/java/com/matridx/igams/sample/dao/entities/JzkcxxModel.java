package com.matridx.igams.sample.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="JzkcxxModel")
public class JzkcxxModel extends BaseModel {
    private String jzkcid;//菌种库id
    private String bxid;//冰箱id
    private String chtid;//抽屉id
    private String hzid;//盒子id
    private String wz;//位置
    private String bh;//编号
    private String mc;//名称
    private String ph;//批号
    private String ly;//来源
    private String rksl;//入库数量
    private String kcl;//库存量
    private String yds;//预定数
    private String gg;//规格
    private String fl;//分类
    private String lx;//类型
    private String bz;//备注
    private String kbs;//拷贝数

    public String getKbs() {
        return kbs;
    }

    public void setKbs(String kbs) {
        this.kbs = kbs;
    }

    public String getJzkcid() {
        return jzkcid;
    }

    public void setJzkcid(String jzkcid) {
        this.jzkcid = jzkcid;
    }

    public String getBxid() {
        return bxid;
    }

    public void setBxid(String bxid) {
        this.bxid = bxid;
    }

    public String getChtid() {
        return chtid;
    }

    public void setChtid(String chtid) {
        this.chtid = chtid;
    }

    public String getHzid() {
        return hzid;
    }

    public void setHzid(String hzid) {
        this.hzid = hzid;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getRksl() {
        return rksl;
    }

    public void setRksl(String rksl) {
        this.rksl = rksl;
    }

    public String getKcl() {
        return kcl;
    }

    public void setKcl(String kcl) {
        this.kcl = kcl;
    }

    public String getYds() {
        return yds;
    }

    public void setYds(String yds) {
        this.yds = yds;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
