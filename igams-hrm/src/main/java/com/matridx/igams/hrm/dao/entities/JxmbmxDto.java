package com.matridx.igams.hrm.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias("JxmbmxDto")
public class JxmbmxDto extends JxmbmxModel {
    private List<JxmbmxDto> children;
    //模板名称
    private String mbmc;
    //总分
    private String zf;
    //类型
    private String lx;
    //模板审核ID
    private String mbshid;
    //修改项说明
    private String xgxsm;
    //评分信息
    private List<GrjxmxDto> pfxxs;
    //考核类型名称
    private String khlxmc;
    //使用日期
    private String syrq;
    //上级考核指标名称
    private String zbsjmc;

    public String getZbsjmc() {
        return zbsjmc;
    }

    public void setZbsjmc(String zbsjmc) {
        this.zbsjmc = zbsjmc;
    }

    public String getSyrq() {
        return syrq;
    }

    public void setSyrq(String syrq) {
        this.syrq = syrq;
    }

    public String getKhlxmc() {
        return khlxmc;
    }

    public void setKhlxmc(String khlxmc) {
        this.khlxmc = khlxmc;
    }

    public List<GrjxmxDto> getPfxxs() {
        return pfxxs;
    }

    public void setPfxxs(List<GrjxmxDto> pfxxs) {
        this.pfxxs = pfxxs;
    }

    public String getXgxsm() {
        return xgxsm;
    }

    public void setXgxsm(String xgxsm) {
        this.xgxsm = xgxsm;
    }

    public String getMbshid() {
        return mbshid;
    }

    public void setMbshid(String mbshid) {
        this.mbshid = mbshid;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    public String getMbmc() {
        return mbmc;
    }

    public void setMbmc(String mbmc) {
        this.mbmc = mbmc;
    }

    public List<JxmbmxDto> getChildren() {
        return children;
    }

    public void setChildren(List<JxmbmxDto> children) {
        this.children = children;
    }

    private static final long serialVersionUID = 1L;

}
