package com.matridx.igams.sample.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

/**
 * @author:JYK
 */
@Alias(value="JzllglDto")
public class JzllglDto extends JzllglModel{
    private String llrmc;//领料人
    private String flrmc;//发料人
    private String bmmc;//部门
    private String mc;//名称
    private String bh;//编号
    private String wz;//位置
    private String ly;//来源
    private String ph;//批号
    private String gg;//规格
    private String[] jzlxs;//菌种类型多
    private String[] jzfls;//菌种分类多
    private String sqsjstart;
    private String sqsjend;
    private String entire;
    private String lrrymc;
    List<JzllmxDto> jzllmxDtos;
    private String llmx_json;
    private String ckmx_json;
    List<String> tgids;
    private String kbs;//拷贝数

    public String getKbs() {
        return kbs;
    }

    public void setKbs(String kbs) {
        this.kbs = kbs;
    }

    public List<String> getTgids() {
        return tgids;
    }

    public void setTgids(List<String> tgids) {
        this.tgids = tgids;
    }

    public String getCkmx_json() {
        return ckmx_json;
    }

    public void setCkmx_json(String ckmx_json) {
        this.ckmx_json = ckmx_json;
    }

    public String getLlmx_json() {
        return llmx_json;
    }

    public void setLlmx_json(String llmx_json) {
        this.llmx_json = llmx_json;
    }

    public List<JzllmxDto> getJzllmxDtos() {
        return jzllmxDtos;
    }

    public void setJzllmxDtos(List<JzllmxDto> jzllmxDtos) {
        this.jzllmxDtos = jzllmxDtos;
    }
    public String getLrrymc() {
        return lrrymc;
    }

    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String[] getJzlxs() {
        return jzlxs;
    }

    public void setJzlxs(String[] jzlxs) {
        this.jzlxs = jzlxs;
    }

    public String[] getJzfls() {
        return jzfls;
    }

    public void setJzfls(String[] jzfls) {
        this.jzfls = jzfls;
    }

    public String getSqsjstart() {
        return sqsjstart;
    }

    public void setSqsjstart(String sqsjstart) {
        this.sqsjstart = sqsjstart;
    }

    public String getSqsjend() {
        return sqsjend;
    }

    public void setSqsjend(String sqsjend) {
        this.sqsjend = sqsjend;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getBh() {
        return bh;
    }

    public void setBh(String bh) {
        this.bh = bh;
    }

    public String getWz() {
        return wz;
    }

    public void setWz(String wz) {
        this.wz = wz;
    }

    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getLlrmc() {
        return llrmc;
    }

    public void setLlrmc(String llrmc) {
        this.llrmc = llrmc;
    }

    public String getFlrmc() {
        return flrmc;
    }

    public void setFlrmc(String flrmc) {
        this.flrmc = flrmc;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
