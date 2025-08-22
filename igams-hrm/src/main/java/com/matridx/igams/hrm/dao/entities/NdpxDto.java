package com.matridx.igams.hrm.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias(value = "NdpxDto")
public class NdpxDto extends NdpxModel {

    //实际培训时间
    private String sjpxsj;
    //应参加人数
    private String ycjrs;
    //缺勤人数
    private String qqrs;
    //参培人数
    private String cprs;
    //培训记录编号
    private String pxjlbh;
    //部门名称
    private String ssbmmc;
    //讲师姓名
    private String jsxm;
    //培训方式名称
    private String pxfsmc;
    //录入人员名称
    private String lrrymc;
    //培训方式多
    private String[] pxfss;
    //年度多
    private String[] nds;
    private String jhpxsjstart;
    private String jhpxsjend;
    private String sjpxsjstart;
    private String sjpxsjend;
    //全部查询
    private String entire;
    //所属公司名称
    private String ssgsmc;
    //状态
    private String zt;
    //是否通过
    private String tgbj;
    //导出关联标记为所选择的字段
    private String sqlParam;
    //部门主管
    private String bmzgs;
    //讲师邮箱
    private String jsyx;
    //外部程序id
    private String wbcxid;
    //讲师用户名
    private String jsyhm;
    //是否参加
    private String sfcj;
    //负责人名称
    private String fzrmc;

    public String getFzrmc() {
        return fzrmc;
    }

    public void setFzrmc(String fzrmc) {
        this.fzrmc = fzrmc;
    }

    public String getSfcj() {
        return sfcj;
    }

    public void setSfcj(String sfcj) {
        this.sfcj = sfcj;
    }

    public String getJsyhm() {
        return jsyhm;
    }

    public void setJsyhm(String jsyhm) {
        this.jsyhm = jsyhm;
    }

    public String getWbcxid() {
        return wbcxid;
    }

    public void setWbcxid(String wbcxid) {
        this.wbcxid = wbcxid;
    }

    public String getJsyx() {
        return jsyx;
    }

    public void setJsyx(String jsyx) {
        this.jsyx = jsyx;
    }

    public String getBmzgs() {
        return bmzgs;
    }

    public void setBmzgs(String bmzgs) {
        this.bmzgs = bmzgs;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String getSsgsmc() {
        return ssgsmc;
    }

    public void setSsgsmc(String ssgsmc) {
        this.ssgsmc = ssgsmc;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getTgbj() {
        return tgbj;
    }

    public void setTgbj(String tgbj) {
        this.tgbj = tgbj;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String[] getPxfss() {
        return pxfss;
    }

    public void setPxfss(String[] pxfss) {
        this.pxfss = pxfss;
    }

    public String[] getNds() {
        return nds;
    }

    public void setNds(String[] nds) {
        this.nds = nds;
    }

    public String getJhpxsjstart() {
        return jhpxsjstart;
    }

    public void setJhpxsjstart(String jhpxsjstart) {
        this.jhpxsjstart = jhpxsjstart;
    }

    public String getJhpxsjend() {
        return jhpxsjend;
    }

    public void setJhpxsjend(String jhpxsjend) {
        this.jhpxsjend = jhpxsjend;
    }

    public String getSjpxsjstart() {
        return sjpxsjstart;
    }

    public void setSjpxsjstart(String sjpxsjstart) {
        this.sjpxsjstart = sjpxsjstart;
    }

    public String getSjpxsjend() {
        return sjpxsjend;
    }

    public void setSjpxsjend(String sjpxsjend) {
        this.sjpxsjend = sjpxsjend;
    }

    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getSjpxsj() {
        return sjpxsj;
    }

    public void setSjpxsj(String sjpxsj) {
        this.sjpxsj = sjpxsj;
    }

    public String getYcjrs() {
        return ycjrs;
    }

    public void setYcjrs(String ycjrs) {
        this.ycjrs = ycjrs;
    }

    public String getQqrs() {
        return qqrs;
    }

    public void setQqrs(String qqrs) {
        this.qqrs = qqrs;
    }

    public String getCprs() {
        return cprs;
    }

    public void setCprs(String cprs) {
        this.cprs = cprs;
    }

    public String getPxjlbh() {
        return pxjlbh;
    }

    public void setPxjlbh(String pxjlbh) {
        this.pxjlbh = pxjlbh;
    }

    public String getSsbmmc() {
        return ssbmmc;
    }

    public void setSsbmmc(String ssbmmc) {
        this.ssbmmc = ssbmmc;
    }

    public String getJsxm() {
        return jsxm;
    }

    public void setJsxm(String jsxm) {
        this.jsxm = jsxm;
    }

    public String getPxfsmc() {
        return pxfsmc;
    }

    public void setPxfsmc(String pxfsmc) {
        this.pxfsmc = pxfsmc;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
