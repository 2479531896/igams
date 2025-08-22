package com.matridx.igams.production.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias("SczlglModel")
public class SczlglModel extends BaseBasicModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String sczlid;// 生产指令id
    private String zldh;// 指令单号
    private String zlrq;// 指令日期
    private String cpbh;// 生产编号
    private String jhcl;// 计划产量
    private String cplx;// 产品类型
    private String bz;// 备注
    private String zt;// 状态
    private String yysl;//引用数量
    private String shlx;//审核类型

    public String getShlx() {
        return shlx;
    }

    public void setShlx(String shlx) {
        this.shlx = shlx;
    }

    public String getYysl() {
        return yysl;
    }

    public void setYysl(String yysl) {
        this.yysl = yysl;
    }

    public String getSczlid() {
        return sczlid;
    }

    public void setSczlid(String sczlid) {
        this.sczlid = sczlid;
    }

    public String getZldh() {
        return zldh;
    }

    public void setZldh(String zldh) {
        this.zldh = zldh;
    }

    public String getZlrq() {
        return zlrq;
    }

    public void setZlrq(String zlrq) {
        this.zlrq = zlrq;
    }

    public String getCpbh() {
        return cpbh;
    }

    public void setCpbh(String cpbh) {
        this.cpbh = cpbh;
    }

    public String getJhcl() {
        return jhcl;
    }

    public void setJhcl(String jhcl) {
        this.jhcl = jhcl;
    }

    public String getCplx() {
        return cplx;
    }

    public void setCplx(String cplx) {
        this.cplx = cplx;
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
}
