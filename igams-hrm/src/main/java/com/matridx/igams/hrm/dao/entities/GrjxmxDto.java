package com.matridx.igams.hrm.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author:JYK
 */
@Alias("GrjxmxDto")
public class GrjxmxDto extends GrjxmxModel{
    private static final long serialVersionUID = 1L;
    private String mbmc;//模板名称
    private String zh;//序号
    private String khsj;//考核时间
    private String zf;//总分
    private String khzf;//考核总分
    private String zt;//状态
    private String khzq;//考核周期
    private String zm;//姓名
    private String bm;//部门
    private String zbsjid;//指标上级id
    private String jxfs;//绩效分数
    private String mbfs;//模板分数
    private String khzb;//考核指标
    private String mbzf;//模板总分
    private String jxzf;//绩效总分
    private String qz;//权重
    private List<String> jxfss;//绩效分数s
    private List<String> jxzfs;//绩效总分s
    private String grjxidorfid;//通过个人绩效id和fid查询所有绩效信息
    private String gwmc;//岗位名称
    private String shrmc;//审核人名称
    private String lcxh;//流程序号
    private String jxshid;//绩效审核id
    private String sm;//说明

    public String getSm() {
        return sm;
    }

    public void setSm(String sm) {
        this.sm = sm;
    }

    public String getJxshid() {
        return jxshid;
    }

    public void setJxshid(String jxshid) {
        this.jxshid = jxshid;
    }

    public String getLcxh() {
        return lcxh;
    }

    public void setLcxh(String lcxh) {
        this.lcxh = lcxh;
    }

    public String getShrmc() {
        return shrmc;
    }

    public void setShrmc(String shrmc) {
        this.shrmc = shrmc;
    }

    public String getGwmc() {
        return gwmc;
    }

    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }

    public String getGrjxidorfid() {
        return grjxidorfid;
    }

    public void setGrjxidorfid(String grjxidorfid) {
        this.grjxidorfid = grjxidorfid;
    }

    public void setJxfss(String jxfss) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(jxfss)) {
            String[] str = jxfss.split(",");
            list = Arrays.asList(str);
        }
        this.jxfss = list;
    }
    public void setJxzfs(String jxzfs) {
        List<String> list = new ArrayList<String>();
        if(StringUtil.isNotBlank(jxzfs)) {
            String[] str = jxzfs.split(",");
            list = Arrays.asList(str);
        }
        this.jxzfs = list;
    }
    public List<String> getJxfss() {
        return jxfss;
    }

    public void setJxfss(List<String> jxfss) {
        this.jxfss = jxfss;
    }

    public List<String> getJxzfs() {
        return jxzfs;
    }

    public void setJxzfs(List<String> jxzfs) {
        this.jxzfs = jxzfs;
    }

    public String getQz() {
        return qz;
    }

    public void setQz(String qz) {
        this.qz = qz;
    }

    public String getJxzf() {
        return jxzf;
    }

    public void setJxzf(String jxzf) {
        this.jxzf = jxzf;
    }

    public String getMbzf() {
        return mbzf;
    }

    public void setMbzf(String mbzf) {
        this.mbzf = mbzf;
    }

    public String getJxfs() {
        return jxfs;
    }

    public void setJxfs(String jxfs) {
        this.jxfs = jxfs;
    }

    public String getMbfs() {
        return mbfs;
    }

    public void setMbfs(String mbfs) {
        this.mbfs = mbfs;
    }

    public String getKhzb() {
        return khzb;
    }

    public void setKhzb(String khzb) {
        this.khzb = khzb;
    }

    private List<GrjxmxDto> children;

    public String getZbsjid() {
        return zbsjid;
    }

    public void setZbsjid(String zbsjid) {
        this.zbsjid = zbsjid;
    }

    public void setChildren(List<GrjxmxDto> children) {
        this.children = children;
    }

    public List<GrjxmxDto> getChildren() {
        return children;
    }

    public String getMbmc() {
        return mbmc;
    }

    public void setMbmc(String mbmc) {
        this.mbmc = mbmc;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }

    public String getKhsj() {
        return khsj;
    }

    public void setKhsj(String khsj) {
        this.khsj = khsj;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    public String getKhzf() {
        return khzf;
    }

    public void setKhzf(String khzf) {
        this.khzf = khzf;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getKhzq() {
        return khzq;
    }

    public void setKhzq(String khzq) {
        this.khzq = khzq;
    }

    public String getZm() {
        return zm;
    }

    public void setZm(String zm) {
        this.zm = zm;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }
}
