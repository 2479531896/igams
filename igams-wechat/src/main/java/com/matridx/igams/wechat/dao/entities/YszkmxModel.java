package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;


@Alias(value="YszkmxModel")
public class YszkmxModel extends BaseModel {
    private static final long serialVersionUID = 1L;

    private String ysmxid;//应收明细ID
    private String yszkid;//应收账款ID
    private String xmglid;//项目管理ID
    private String jcxmid;//检测项目ID
    private String jczxmid;//检测子项目ID
    private String ywid;//业务id
    private String mc;//名称
    private String jsje;//结算金额
    private String sfje;//实付金额
    private String kdfy;//快递费用
    private String bz;//备注
    private String lx;//类型

    private String sfzykh;//是否主要客户

    public String getSfzykh() {
        return sfzykh;
    }

    public void setSfzykh(String sfzykh) {
        this.sfzykh = sfzykh;
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

    public String getYsmxid() {
        return ysmxid;
    }

    public void setYsmxid(String ysmxid) {
        this.ysmxid = ysmxid;
    }

    public String getYszkid() {
        return yszkid;
    }

    public void setYszkid(String yszkid) {
        this.yszkid = yszkid;
    }

    public String getXmglid() {
        return xmglid;
    }

    public void setXmglid(String xmglid) {
        this.xmglid = xmglid;
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


    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getJsje() {
        return jsje;
    }

    public void setJsje(String jsje) {
        this.jsje = jsje;
    }

    public String getSfje() {
        return sfje;
    }

    public void setSfje(String sfje) {
        this.sfje = sfje;
    }

    public String getKdfy() {
        return kdfy;
    }

    public void setKdfy(String kdfy) {
        this.kdfy = kdfy;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }
}

