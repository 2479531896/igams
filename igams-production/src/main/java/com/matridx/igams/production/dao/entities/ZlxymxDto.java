package com.matridx.igams.production.dao.entities;

import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "ZlxymxDto")
public class ZlxymxDto extends ZlxymxModel{
    private String wlbm;//物料编码
    private String wlmc;//物料名称
    private String scs;//生产商
    private String zlxybh;//协议编号
    private String cjsj;//创建时间
    private String kssj;//开始时间
    private String dqsj;//到期时间
    private String gysid;//供应商ID
    private String jldw;//计量单位
    private String gg;//规格
    private String zlyqmc;//质量要求
    private String[] sjxmhs;
    private String sjxmhmc;

    public String getSjxmhmc() {
        return sjxmhmc;
    }

    public void setSjxmhmc(String sjxmhmc) {
        this.sjxmhmc = sjxmhmc;
    }

    public String[] getSjxmhs() {
        return sjxmhs;
    }

    public void setSjxmhs(String[] sjxmhs) {
        this.sjxmhs = sjxmhs;
    }

    public String getZlyqmc() {
        return zlyqmc;
    }

    public void setZlyqmc(String zlyqmc) {
        this.zlyqmc = zlyqmc;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getGysid() {
        return gysid;
    }

    public void setGysid(String gysid) {
        this.gysid = gysid;
    }

    public String getScs() {
        return scs;
    }

    public void setScs(String scs) {
        this.scs = scs;
    }

    public String getZlxybh() {
        return zlxybh;
    }

    public void setZlxybh(String zlxybh) {
        this.zlxybh = zlxybh;
    }

    public String getCjsj() {
        return cjsj;
    }

    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getDqsj() {
        return dqsj;
    }

    public void setDqsj(String dqsj) {
        this.dqsj = dqsj;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }
}
