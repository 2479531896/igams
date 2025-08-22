package com.matridx.igams.hrm.dao.entities;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias("GrjxModel")
public class GrjxModel extends BaseBasicModel {
    private String grjxid;//个人绩效id
    private String jxmbid;//绩效模板id
    private String mbszid;//模板设置id
    private String xh;//序号
    private String yhid;//用户id
    private String gwid;//岗位id
    private String lx;//类型
    private String shr;//审核人
    private String bm;//部门
    private String nf;//年份
    private String yf;//月份
    private String zf;//周份
    private String khzf;//考核总分
    private String jxzf;//绩效总分
    private String sjbz;//上级备注
    private String zqkssj;//周期开始时间
    private String zqjssj;//周期结束时间
    private String qz;//权重
    private String zt;//状态
    private String sfjs;//当前周期绩效是否结束
    private String jxbz;//绩效备注
    private String qrzt;//确认状态

    public String getQrzt() {
        return qrzt;
    }

    public void setQrzt(String qrzt) {
        this.qrzt = qrzt;
    }

    public String getJxbz() {
        return jxbz;
    }

    public void setJxbz(String jxbz) {
        this.jxbz = jxbz;
    }

    public String getSfjs() {
        return sfjs;
    }

    public void setSfjs(String sfjs) {
        this.sfjs = sfjs;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getGrjxid() {
        return grjxid;
    }

    public void setGrjxid(String grjxid) {
        this.grjxid = grjxid;
    }

    public String getJxmbid() {
        return jxmbid;
    }

    public void setJxmbid(String jxmbid) {
        this.jxmbid = jxmbid;
    }

    public String getMbszid() {
        return mbszid;
    }

    public void setMbszid(String mbszid) {
        this.mbszid = mbszid;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getGwid() {
        return gwid;
    }

    public void setGwid(String gwid) {
        this.gwid = gwid;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getShr() {
        return shr;
    }

    public void setShr(String shr) {
        this.shr = shr;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getNf() {
        return nf;
    }

    public void setNf(String nf) {
        this.nf = nf;
    }

    public String getYf() {
        return yf;
    }

    public void setYf(String yf) {
        this.yf = yf;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    public String getKhzf() {
        return khzf;
    }

    public void setKhzf(String khzf) {
        this.khzf = khzf;
    }

    public String getJxzf() {
        return jxzf;
    }

    public void setJxzf(String jxzf) {
        this.jxzf = jxzf;
    }

    public String getSjbz() {
        return sjbz;
    }

    public void setSjbz(String sjbz) {
        this.sjbz = sjbz;
    }

    public String getZqkssj() {
        return zqkssj;
    }

    public void setZqkssj(String zqkssj) {
        this.zqkssj = zqkssj;
    }

    public String getZqjssj() {
        return zqjssj;
    }

    public void setZqjssj(String zqjssj) {
        this.zqjssj = zqjssj;
    }

    public String getQz() {
        return qz;
    }

    public void setQz(String qz) {
        this.qz = qz;
    }
    private static final long serialVersionUID = 1L;
}
