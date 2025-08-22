package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "SbwxModel")
public class SbwxModel extends BaseBasicModel {
    private String sbwxid;//设备维修id
    private String sbysid;//设备验收id
    private String gzms;//故障描述
    private String sqr;//申请人
    private String sqbm;//申请部门
    private String sqrq;//申请日期
    private String tjsj;//提交时间
    private String sfcsfy;//是否产生费用
    private String fyje;//费用金额
    private String wxqqwfs;//维修前去污方式
    private String wxdw;//维修单位
    private String wbry;//维保人员
    private String wxsj;//维修时间
    private String jl;//结论
    private String lb;//类别（0维修  1保养）
    private String bz;//备注
    private String wxhqwfs;//维修后去污方式
    private String kdx;//可读性
    private String sfyz;//是否验证
    private String yzr;//验证人
    private String yzrq;//验证日期
    private String hfsyrq;//恢复使用日期
    private String yzfs;//验证方式
    private String ddslid;//钉钉实例id
    private String yzjl;//验证结论
    private String zt;//状态
    private String jlbh;//记录编号
    private String pzcs;//排障措施
    private String yzqk;//验证情况
    private String sjbf;//数据备份
    private String bfcs;//备份措施

    public String getPzcs() {
        return pzcs;
    }

    public void setPzcs(String pzcs) {
        this.pzcs = pzcs;
    }

    public String getYzqk() {
        return yzqk;
    }

    public void setYzqk(String yzqk) {
        this.yzqk = yzqk;
    }

    public String getSjbf() {
        return sjbf;
    }

    public void setSjbf(String sjbf) {
        this.sjbf = sjbf;
    }

    public String getBfcs() {
        return bfcs;
    }

    public void setBfcs(String bfcs) {
        this.bfcs = bfcs;
    }

    public String getJlbh() {
        return jlbh;
    }

    public void setJlbh(String jlbh) {
        this.jlbh = jlbh;
    }

    public String getSbwxid() {
        return sbwxid;
    }

    public void setSbwxid(String sbwxid) {
        this.sbwxid = sbwxid;
    }

    public String getSbysid() {
        return sbysid;
    }

    public void setSbysid(String sbysid) {
        this.sbysid = sbysid;
    }

    public String getGzms() {
        return gzms;
    }

    public void setGzms(String gzms) {
        this.gzms = gzms;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getSqbm() {
        return sqbm;
    }

    public void setSqbm(String sqbm) {
        this.sqbm = sqbm;
    }

    public String getSqrq() {
        return sqrq;
    }

    public void setSqrq(String sqrq) {
        this.sqrq = sqrq;
    }

    public String getTjsj() {
        return tjsj;
    }

    public void setTjsj(String tjsj) {
        this.tjsj = tjsj;
    }

    public String getSfcsfy() {
        return sfcsfy;
    }

    public void setSfcsfy(String sfcsfy) {
        this.sfcsfy = sfcsfy;
    }

    public String getFyje() {
        return fyje;
    }

    public void setFyje(String fyje) {
        this.fyje = fyje;
    }

    public String getWxqqwfs() {
        return wxqqwfs;
    }

    public void setWxqqwfs(String wxqqwfs) {
        this.wxqqwfs = wxqqwfs;
    }

    public String getWxdw() {
        return wxdw;
    }

    public void setWxdw(String wxdw) {
        this.wxdw = wxdw;
    }

    public String getWbry() {
        return wbry;
    }

    public void setWbry(String wbry) {
        this.wbry = wbry;
    }

    public String getWxsj() {
        return wxsj;
    }

    public void setWxsj(String wxsj) {
        this.wxsj = wxsj;
    }

    public String getJl() {
        return jl;
    }

    public void setJl(String jl) {
        this.jl = jl;
    }

    public String getLb() {
        return lb;
    }

    public void setLb(String lb) {
        this.lb = lb;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getWxhqwfs() {
        return wxhqwfs;
    }

    public void setWxhqwfs(String wxhqwfs) {
        this.wxhqwfs = wxhqwfs;
    }

    public String getKdx() {
        return kdx;
    }

    public void setKdx(String kdx) {
        this.kdx = kdx;
    }

    public String getSfyz() {
        return sfyz;
    }

    public void setSfyz(String sfyz) {
        this.sfyz = sfyz;
    }

    public String getYzr() {
        return yzr;
    }

    public void setYzr(String yzr) {
        this.yzr = yzr;
    }

    public String getYzrq() {
        return yzrq;
    }

    public void setYzrq(String yzrq) {
        this.yzrq = yzrq;
    }

    public String getHfsyrq() {
        return hfsyrq;
    }

    public void setHfsyrq(String hfsyrq) {
        this.hfsyrq = hfsyrq;
    }

    public String getYzfs() {
        return yzfs;
    }

    public void setYzfs(String yzfs) {
        this.yzfs = yzfs;
    }

    public String getDdslid() {
        return ddslid;
    }

    public void setDdslid(String ddslid) {
        this.ddslid = ddslid;
    }

    public String getYzjl() {
        return yzjl;
    }

    public void setYzjl(String yzjl) {
        this.yzjl = yzjl;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
