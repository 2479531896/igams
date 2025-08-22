package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

/**
 * @author:JYK
 */
@Alias(value="HbszModel")
public class HbszModel extends BaseModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3695628270913906610L;
	private String hbszid;//红包设置id
    private String hbmc;//红包名称
    private String tzq;//通知群
    private String fssj;//发送时间
    private String tzxx;//通知信息
    private String rwid;//定时任务id
    private String ys;//样式
    private String xh;//序号

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getYs() {
        return ys;
    }

    public void setYs(String ys) {
        this.ys = ys;
    }

    public String getHbszid() {
        return hbszid;
    }

    public void setHbszid(String hbszid) {
        this.hbszid = hbszid;
    }

    public String getHbmc() {
        return hbmc;
    }

    public void setHbmc(String hbmc) {
        this.hbmc = hbmc;
    }

    public String getTzq() {
        return tzq;
    }

    public void setTzq(String tzq) {
        this.tzq = tzq;
    }

    public String getFssj() {
        return fssj;
    }

    public void setFssj(String fssj) {
        this.fssj = fssj;
    }

    public String getTzxx() {
        return tzxx;
    }

    public void setTzxx(String tzxx) {
        this.tzxx = tzxx;
    }

    public String getRwid() {
        return rwid;
    }

    public void setRwid(String rwid) {
        this.rwid = rwid;
    }
}
