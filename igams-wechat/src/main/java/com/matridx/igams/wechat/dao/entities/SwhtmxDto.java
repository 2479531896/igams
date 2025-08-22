package com.matridx.igams.wechat.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;


/**
 * 商务合同明细(IgamsSwhtmx)实体类
 *
 * @author makejava
 * @since 2023-04-04 13:52:16
 */

@Alias(value="SwhtmxDto")
public class SwhtmxDto  extends SwhtmxModel{
    private static final long serialVersionUID = 1L;

    private String jcxmmc;//检测项目名称z
    private String khid;//客户id
    private String hbid;//伙伴id
    private String htzt;//合同状态
    private String jczxmmc;//检测子项目名称
    private List<String> mxids;//明细ids

    private String htksrq;//合同开始日期
    private String sflx;//是否履行
    private String json;//json
    private List<SwhtmxDto> dtos;
    private String cxbj;//用于特殊判断
    private String zt;//状态

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getCxbj() {
        return cxbj;
    }

    public void setCxbj(String cxbj) {
        this.cxbj = cxbj;
    }

    public List<SwhtmxDto> getDtos() {
        return dtos;
    }

    public void setDtos(List<SwhtmxDto> dtos) {
        this.dtos = dtos;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    private String htjsrq;//合同结束日期

    private String path;//用于处理数据多条取最新一条

    public String getSfbz() {
        return sfbz;
    }

    public void setSfbz(String sfbz) {
        this.sfbz = sfbz;
    }

    private String sfbz;//收费标准

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSflx() {
        return sflx;
    }

    public void setSflx(String sflx) {
        this.sflx = sflx;
    }

    public String getHtksrq() {
        return htksrq;
    }

    public void setHtksrq(String htksrq) {
        this.htksrq = htksrq;
    }

    public String getHtjsrq() {
        return htjsrq;
    }

    public void setHtjsrq(String htjsrq) {
        this.htjsrq = htjsrq;
    }

    public List<String> getMxids() {
        return mxids;
    }

    public void setMxids(List<String> mxids) {
        this.mxids = mxids;
    }

    public String getHtzt() {
        return htzt;
    }

    public void setHtzt(String htzt) {
        this.htzt = htzt;
    }

    public String getHbid() {
        return hbid;
    }

    public void setHbid(String hbid) {
        this.hbid = hbid;
    }

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
    }

    public String getJcxmmc() {
        return jcxmmc;
    }

    public void setJcxmmc(String jcxmmc) {
        this.jcxmmc = jcxmmc;
    }

    public String getJczxmmc() {
        return jczxmmc;
    }

    public void setJczxmmc(String jczxmmc) {
        this.jczxmmc = jczxmmc;
    }
}

