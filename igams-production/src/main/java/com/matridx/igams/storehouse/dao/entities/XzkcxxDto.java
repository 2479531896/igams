package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="XzkcxxDto")
public class XzkcxxDto extends XzkcxxModel{
    private String entire;

    public String getEntire() {
        return entire;
    }
    //货物名称
    private String hwmc;

    public String getHwmc() {
        return hwmc;
    }
    //货物规格
    private String hwgg;
    //货物计量单位
    private String hwjldw;
    //库位
    private String kw;
    //货物标准
    private String hwbz;
    //库位多
    private String[] kws;
    //可领数量
    private String klsl;
    //updateFlag
    private String updateFlag;
    private String dbsl;
    //调入库位
    private String drkw;
    //警戒提示
    private String jjts;
    //警戒提示多
    private String[] jjtss;
    private String sqlParam;
    //预定数标记
    private String ydsbj;
    //行政入库明细id
    private String xzrkmxid;
    private String rksl;
    private String ykcl;
    private String[] kcsfczs;//库存是否充足
    private String kcsfcz;//库存是否充足

    public String[] getKcsfczs() {
        return kcsfczs;
    }

    public void setKcsfczs(String[] kcsfczs) {
        this.kcsfczs = kcsfczs;
    }

    public String getKcsfcz() {
        return kcsfcz;
    }

    public void setKcsfcz(String kcsfcz) {
        this.kcsfcz = kcsfcz;
    }

    public String getYkcl() {
        return ykcl;
    }

    public void setYkcl(String ykcl) {
        this.ykcl = ykcl;
    }

    public String getRksl() {
        return rksl;
    }

    public void setRksl(String rksl) {
        this.rksl = rksl;
    }

    public String getXzrkmxid() {
        return xzrkmxid;
    }

    public void setXzrkmxid(String xzrkmxid) {
        this.xzrkmxid = xzrkmxid;
    }

    public String getYdsbj() {
        return ydsbj;
    }

    public void setYdsbj(String ydsbj) {
        this.ydsbj = ydsbj;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String[] getJjtss() {
        return jjtss;
    }

    public void setJjtss(String[] jjtss) {
        this.jjtss = jjtss;
    }

    public String getJjts() {
        return jjts;
    }

    public void setJjts(String jjts) {
        this.jjts = jjts;
    }

    public String getDrkw() {
        return drkw;
    }

    public void setDrkw(String drkw) {
        this.drkw = drkw;
    }

    public String getDbsl() {
        return dbsl;
    }

    public void setDbsl(String dbsl) {
        this.dbsl = dbsl;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getKlsl() {
        return klsl;
    }

    public void setKlsl(String klsl) {
        this.klsl = klsl;
    }

    public String[] getKws() {
        return kws;
    }

    public void setKws(String[] kws) {
        this.kws = kws;
    }

    public String getHwbz() {
        return hwbz;
    }

    public void setHwbz(String hwbz) {
        this.hwbz = hwbz;
    }

    public String getHwgg() {
        return hwgg;
    }

    public void setHwgg(String hwgg) {
        this.hwgg = hwgg;
    }

    public String getHwjldw() {
        return hwjldw;
    }

    public void setHwjldw(String hwjldw) {
        this.hwjldw = hwjldw;
    }

    public String getKw() {
        return kw;
    }

    public void setKw(String kw) {
        this.kw = kw;
    }

    public void setHwmc(String hwmc) {
        this.hwmc = hwmc;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
