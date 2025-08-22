package com.matridx.igams.web.dao.entities;


import com.matridx.igams.common.dao.BaseModel;
import org.apache.ibatis.type.Alias;

@Alias(value="RszpModel")
public class RszpModel extends BaseModel {
    //招聘id
    private String zpid;
    //需求岗位
    private String xqgw;
    //岗位base地
    private String gwbased;
    //负责区域及医院
    private String fzqyjyy;
    //需求人数
    private String xqrs;
    //预计年薪
    private String yjnx;
    //岗位现有人数
    private String gwxyrs;
    //岗位职责要求
    private String gwyqzz;
    //希望到岗日期
    private String xwdgrq;
    //发起人
    private String fqr;
    //发起人所在部门
    private String fqrbm;
    //发起时间
    private String fqsj;
    //外部程序id
    private String wbcxid;

    public String getWbcxid() {
        return wbcxid;
    }

    public void setWbcxid(String wbcxid) {
        this.wbcxid = wbcxid;
    }
    public String getZpid() {
        return zpid;
    }

    public void setZpid(String zpid) {
        this.zpid = zpid;
    }

    public String getXqgw() {
        return xqgw;
    }

    public void setXqgw(String xqgw) {
        this.xqgw = xqgw;
    }

    public String getGwbased() {
        return gwbased;
    }

    public void setGwbased(String gwbased) {
        this.gwbased = gwbased;
    }

    public String getFzqyjyy() {
        return fzqyjyy;
    }

    public void setFzqyjyy(String fzqyjyy) {
        this.fzqyjyy = fzqyjyy;
    }

    public String getXqrs() {
        return xqrs;
    }

    public void setXqrs(String xqrs) {
        this.xqrs = xqrs;
    }

    public String getYjnx() {
        return yjnx;
    }

    public void setYjnx(String yjnx) {
        this.yjnx = yjnx;
    }

    public String getGwxyrs() {
        return gwxyrs;
    }

    public void setGwxyrs(String gwxyrs) {
        this.gwxyrs = gwxyrs;
    }

    public String getGwyqzz() {
        return gwyqzz;
    }

    public void setGwyqzz(String gwyqzz) {
        this.gwyqzz = gwyqzz;
    }

    public String getXwdgrq() {
        return xwdgrq;
    }

    public void setXwdgrq(String xwdgrq) {
        this.xwdgrq = xwdgrq;
    }

    public String getFqr() {
        return fqr;
    }

    public void setFqr(String fqr) {
        this.fqr = fqr;
    }

    public String getFqrbm() {
        return fqrbm;
    }

    public void setFqrbm(String fqrbm) {
        this.fqrbm = fqrbm;
    }

    public String getFqsj() {
        return fqsj;
    }

    public void setFqsj(String fqsj) {
        this.fqsj = fqsj;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
