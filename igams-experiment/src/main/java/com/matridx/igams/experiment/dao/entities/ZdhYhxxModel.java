package com.matridx.igams.experiment.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value="ZdhYhxxModel")
public class ZdhYhxxModel extends BaseBasicModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -710102442364923768L;
	//用户ID';
    private String yhid;
    //微信ID';
    private String wechatid;
    //基础数据ID';
    private String jcsjid;
    //状态';
    private String zt;
    //用户名';
    private String yhm;
    //密码';
    private String mm;

    public String getYhid() {
        return yhid;
    }

    public void setYhid(String yhid) {
        this.yhid = yhid;
    }

    public String getWechatid() {
        return wechatid;
    }

    public void setWechatid(String wechatid) {
        this.wechatid = wechatid;
    }

    public String getJcsjid() {
        return jcsjid;
    }

    public void setJcsjid(String jcsjid) {
        this.jcsjid = jcsjid;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }
}
