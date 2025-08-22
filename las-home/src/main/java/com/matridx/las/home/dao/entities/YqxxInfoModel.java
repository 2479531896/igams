package com.matridx.las.home.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias("YqxxInfoModel")
public class YqxxInfoModel extends BaseModel {
    private static final long serialVersionUID = 1L;
    //仪器信息id
    private String yqxxid;
    //仪器id
    private String yqid;
    //实验室id
    private String sysid;
    //样式
    private String style;
    //父区域
    private String fqy;
    //父区域id
    private String fqyid;
    //仪器信息名称
    private String name;
    //设备id
    private String deviceid;
    //区域
    private String qy;
    //位置
    private String wz;
    private String pzwz;
    //协议加id
    private String commanddeviceid;
    private String bz;
    private String csdm;

    public String getYqxxid() {
        return yqxxid;
    }

    public void setYqxxid(String yqxxid) {
        this.yqxxid = yqxxid;
    }

    public String getYqid() {
        return yqid;
    }

    public void setYqid(String yqid) {
        this.yqid = yqid;
    }

    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFqy() {
        return fqy;
    }

    public void setFqy(String fqy) {
        this.fqy = fqy;
    }

    public String getFqyid() {
        return fqyid;
    }

    public void setFqyid(String fqyid) {
        this.fqyid = fqyid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getQy() {
        return qy;
    }

    public void setQy(String qy) {
        this.qy = qy;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }

    public String getPzwz() {
        return pzwz;
    }

    public void setPzwz(String pzwz) {
        this.pzwz = pzwz;
    }

    public String getCommanddeviceid() {
        return commanddeviceid;
    }

    public void setCommanddeviceid(String commanddeviceid) {
        this.commanddeviceid = commanddeviceid;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getCsdm() {
        return csdm;
    }

    public void setCsdm(String csdm) {
        this.csdm = csdm;
    }
}
