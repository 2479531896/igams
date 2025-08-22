package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value = "ZlxyModel")
public class ZlxyModel extends BaseBasicModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String zlxyid;//质量协议id
    private String gysid;//供应商id
    private String fzr;//负责人
    private String zlxybh;//质量协议编号
    private String cjsj;//创建时间
    private String kssj;//开始时间
    private String dqsj;//到期时间
    private String bz;//备注
    private String zt;//状态
    private String htxj;//合同新旧0:旧1：新
    private String fddbr;//法定代表人
    private String szbj;//双章合同
    private String wbxybh;//外部协议编号
    private String htlx;//合同类型
    private String bchtid;//补充合同id

    public String getBchtid() {
        return bchtid;
    }

    public void setBchtid(String bchtid) {
        this.bchtid = bchtid;
    }

    public String getHtlx() {
        return htlx;
    }

    public void setHtlx(String htlx) {
        this.htlx = htlx;
    }

    public String getWbxybh() {
        return wbxybh;
    }

    public void setWbxybh(String wbxybh) {
        this.wbxybh = wbxybh;
    }

    public String getSzbj() {
        return szbj;
    }

    public void setSzbj(String szbj) {
        this.szbj = szbj;
    }

    public String getFddbr() {
        return fddbr;
    }

    public void setFddbr(String fddbr) {
        this.fddbr = fddbr;
    }

    public String getZlxyid() {
        return zlxyid;
    }

    public void setZlxyid(String zlxyid) {
        this.zlxyid = zlxyid;
    }

    public String getGysid() {
        return gysid;
    }

    public void setGysid(String gysid) {
        this.gysid = gysid;
    }

    public String getFzr() {
        return fzr;
    }

    public void setFzr(String fzr) {
        this.fzr = fzr;
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

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getHtxj() {
        return htxj;
    }

    public void setHtxj(String htxj) {
        this.htxj = htxj;
    }
}
