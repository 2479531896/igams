package com.matridx.igams.web.dao.entities;


import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="RslyModel")
public class RslyModel  extends BaseModel {
    //录用ID
    private String lyid;
    //招聘ID
    private String zpid;
    //入职员工姓名
    private String rzygxm;
    //用人部门
    private String yrbm;
    //职位
    private String zw;
    //手机
    private String sj;
    //员工类型
    private String yglx;
    //入职日期
    private String rzrq;
    //备注
    private String bz;
    //审批ID
    private String spid;
    //外部程序id
    private String wbcxid;

    public String getWbcxid() {
        return wbcxid;
    }

    public void setWbcxid(String wbcxid) {
        this.wbcxid = wbcxid;
    }
    public String getLyid() {
        return lyid;
    }

    public void setLyid(String lyid) {
        this.lyid = lyid;
    }

    public String getZpid() {
        return zpid;
    }

    public void setZpid(String zpid) {
        this.zpid = zpid;
    }

    public String getRzygxm() {
        return rzygxm;
    }

    public void setRzygxm(String rzygxm) {
        this.rzygxm = rzygxm;
    }

    public String getYrbm() {
        return yrbm;
    }

    public void setYrbm(String yrbm) {
        this.yrbm = yrbm;
    }

    public String getZw() {
        return zw;
    }

    public void setZw(String zw) {
        this.zw = zw;
    }

    public String getSj() {
        return sj;
    }

    public void setSj(String sj) {
        this.sj = sj;
    }

    public String getYglx() {
        return yglx;
    }

    public void setYglx(String yglx) {
        this.yglx = yglx;
    }

    public String getRzrq() {
        return rzrq;
    }

    public void setRzrq(String rzrq) {
        this.rzrq = rzrq;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getSpid() {
        return spid;
    }

    public void setSpid(String spid) {
        this.spid = spid;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
