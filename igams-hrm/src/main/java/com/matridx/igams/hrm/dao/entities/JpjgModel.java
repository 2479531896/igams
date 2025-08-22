package com.matridx.igams.hrm.dao.entities;

import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "JpjgModel")
public class JpjgModel extends BaseModel {
    //奖品管理id
    private String jpglid;
    //奖品明细id
    private String jpmxid;
    //奖品结果id
    private String jpjgid;
    //获奖时间
    private String hjsj;
    //用户id
    private String yhid;
    //奖品名称
    private String jpmc;

    public String getJpmc() {
        return jpmc;
    }

    public void setJpmc(String jpmc) {
        this.jpmc = jpmc;
    }

    public String getJpglid() {
        return jpglid;
    }

    public void setJpglid(String jpglid) {
        this.jpglid = jpglid;
    }

    public String getJpmxid() {
        return jpmxid;
    }

    public void setJpmxid(String jpmxid) {
        this.jpmxid = jpmxid;
    }

    public String getJpjgid() {
        return jpjgid;
    }

    public void setJpjgid(String jpjgid) {
        this.jpjgid = jpjgid;
    }

    public String getHjsj() {
        return hjsj;
    }

    public void setHjsj(String hjsj) {
        this.hjsj = hjsj;
    }

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
