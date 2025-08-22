package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value = "SjsyglModel")
public class SjsyglModel extends BaseModel {

    private String syglid;//实验管理ID
    private String sjid;//送检ID
    private String jcxmid;//检测项目 关联基础数据
    private String jczxmid;//检测子项目
    private String jclxid;//检测类型  基础数据管理，对项目切分，按照实验室分类
    private String jcdw;//检测单位 关联基础数据
    private String sfjs;//是否接收 0：不接收 1：接收
    private String nbzbm;//内部子编码
    private String xmmc;//项目名称
    private String jsrq;//接收日期
    private String jsry;//接收人员
    private String jcbj;//检测标记 用于标识该检测类型是否已检测 0：未检测 1：已检测
    private String syrq;//实验日期
    private String jt;//接头
    private String zt;//状态
    private String bz;//备注
    private String dyid;//对应id
    private String qysj;//取样时间
    private String qyry;//取样人员
    private String lx;//类型
    private String ywid;//业务ID
    private String jcsj;//检测时间
    private List<String> ywids;//业务IDs
    private String kzsj;
    //上机时间
    private String sjsj;
    //下机时间
    private String xjsj;
    //文库生信编码
    private String wksxbm;

    public String getXjsj() {
        return xjsj;
    }

    public void setXjsj(String xjsj) {
        this.xjsj = xjsj;
    }

    public String getWksxbm() {
        return wksxbm;
    }

    public void setWksxbm(String wksxbm) {
        this.wksxbm = wksxbm;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getSjsj() {
        return sjsj;
    }

    public void setSjsj(String sjsj) {
        this.sjsj = sjsj;
    }

    public String getJcsj() {
        return jcsj;
    }

    public void setJcsj(String jcsj) {
        this.jcsj = jcsj;
    }

    public List<String> getYwids() {
        return ywids;
    }

    public void setYwids(List<String> ywids) {
        this.ywids = ywids;
    }

    public String getYwid() {
        return ywid;
    }

    public void setYwid(String ywid) {
        this.ywid = ywid;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getQysj() {
        return qysj;
    }

    public void setQysj(String qysj) {
        this.qysj = qysj;
    }

    public String getQyry() {
        return qyry;
    }

    public void setQyry(String qyry) {
        this.qyry = qyry;
    }

    public String getDyid() {
        return dyid;
    }

    public void setDyid(String dyid) {
        this.dyid = dyid;
    }

    public String getSyglid() {
        return syglid;
    }

    public void setSyglid(String syglid) {
        this.syglid = syglid;
    }

    public String getSjid() {
        return sjid;
    }

    public void setSjid(String sjid) {
        this.sjid = sjid;
    }

    public String getJcxmid() {
        return jcxmid;
    }

    public void setJcxmid(String jcxmid) {
        this.jcxmid = jcxmid;
    }

    public String getJczxmid() {
        return jczxmid;
    }

    public void setJczxmid(String jczxmid) {
        this.jczxmid = jczxmid;
    }

    public String getJclxid() {
        return jclxid;
    }

    public void setJclxid(String jclxid) {
        this.jclxid = jclxid;
    }

    public String getJcdw() {
        return jcdw;
    }

    public void setJcdw(String jcdw) {
        this.jcdw = jcdw;
    }

    public String getSfjs() {
        return sfjs;
    }

    public void setSfjs(String sfjs) {
        this.sfjs = sfjs;
    }

    public String getNbzbm() {
        return nbzbm;
    }

    public void setNbzbm(String nbzbm) {
        this.nbzbm = nbzbm;
    }

    public String getJsrq() {
        return jsrq;
    }

    public void setJsrq(String jsrq) {
        this.jsrq = jsrq;
    }

    public String getJsry() {
        return jsry;
    }

    public void setJsry(String jsry) {
        this.jsry = jsry;
    }

    public String getJcbj() {
        return jcbj;
    }

    public void setJcbj(String jcbj) {
        this.jcbj = jcbj;
    }

    public String getSyrq() {
        return syrq;
    }

    public void setSyrq(String syrq) {
        this.syrq = syrq;
    }

    public String getJt() {
        return jt;
    }

    public void setJt(String jt) {
        this.jt = jt;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
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

    public String getKzsj() {
        return kzsj;
    }

    public void setKzsj(String kzsj) {
        this.kzsj = kzsj;
    }
}
