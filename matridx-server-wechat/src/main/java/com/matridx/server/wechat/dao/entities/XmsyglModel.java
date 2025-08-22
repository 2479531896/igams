package com.matridx.server.wechat.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "XmsyglModel")
public class XmsyglModel extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String xmsyglid;//项目实验管理ID
    private String dyid;//对应ID
    private String syglid;//实验管理ID
    private String jcxmid;//检测项目ID
    private String jczxmid;//检测子项目ID
    private String ywid;//业务id
    private String ywlx;//类型
    private String wkdm;//文库代码

    public String getWkdm() {
        return wkdm;
    }

    public void setWkdm(String wkdm) {
        this.wkdm = wkdm;
    }



    public String getXmsyglid() {
        return xmsyglid;
    }

    public void setXmsyglid(String xmsyglid) {
        this.xmsyglid = xmsyglid;
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

    public String getYwid() {
        return ywid;
    }

    public void setYwid(String ywid) {
        this.ywid = ywid;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }
}
