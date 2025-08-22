package com.matridx.igams.storehouse.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import org.apache.ibatis.type.Alias;

@Alias(value = "JcghglModel")
public class JcghglModel extends BaseBasicModel {

    ///借出归还ID
    private String jcghid;
    //借出借用ID
    private String jcjyid;
    //归还单号
    private String ghdh;
    //部门
    private String bm;
    //单位
    private String dwlx;
    //单位ID
    private String dwid;
    //归还日期
    private String ghrq;
    //是否支付运费
    private String sfzfyf;
    //关联ID
    private String glid;
    //备注
    private String bz;
    //状态
    private String zt;
    //u8归还单号
    private String u8ghdh;
    //销售类型
    private String xslx;
    private String qydh;//请验单号

    public String getQydh() {
        return qydh;
    }

    public void setQydh(String qydh) {
        this.qydh = qydh;
    }

    public String getXslx() {
        return xslx;
    }

    public void setXslx(String xslx) {
        this.xslx = xslx;
    }

    public String getU8ghdh() {
        return u8ghdh;
    }

    public void setU8ghdh(String u8ghdh) {
        this.u8ghdh = u8ghdh;
    }

    public String getDwlx() {
        return dwlx;
    }

    public void setDwlx(String dwlx) {
        this.dwlx = dwlx;
    }

    public String getJcghid() {
        return jcghid;
    }

    public void setJcghid(String jcghid) {
        this.jcghid = jcghid;
    }

    public String getJcjyid() {
        return jcjyid;
    }

    public void setJcjyid(String jcjyid) {
        this.jcjyid = jcjyid;
    }

    public String getGhdh() {
        return ghdh;
    }

    public void setGhdh(String ghdh) {
        this.ghdh = ghdh;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getDwid() {
        return dwid;
    }

    public void setDwid(String dwid) {
        this.dwid = dwid;
    }

    public String getGhrq() {
        return ghrq;
    }

    public void setGhrq(String ghrq) {
        this.ghrq = ghrq;
    }

    public String getSfzfyf() {
        return sfzfyf;
    }

    public void setSfzfyf(String sfzfyf) {
        this.sfzfyf = sfzfyf;
    }

    public String getGlid() {
        return glid;
    }

    public void setGlid(String glid) {
        this.glid = glid;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
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
