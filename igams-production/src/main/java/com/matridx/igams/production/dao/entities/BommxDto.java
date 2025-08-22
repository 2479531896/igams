package com.matridx.igams.production.dao.entities;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.ibatis.type.Alias;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias("BommxDto")
public class BommxDto extends BommxModel {
    private String scs;//生产商
    private String wlmc;//子件物料名称
    private String wlbm;//子件物料编码
    private String jldw;//计量单位
    private String gg;//规格
    private String mjwlbm;//母件物料编码
    private String mjwlid;//母件物料id
    private String bomlbmc;//母件物料名称
    private String bomlb;
    private String zwlbm;//子物料编码
    private String bbrq;	//版本日期
    private String bbh;		//版本号
    private String bbsm;	//版本说明
    private String xqjhmxid;//需求计划明细id
    private String wllbmc;//物料类别名称
    private String wlzlbmc;//物料子类别名称
    private String wlzlbtcmc;//物料子类别统称
    private String ychh;//原厂货号
    private String wlid;//物料id
    private String kcl;//库存量
    private String yjzyl;//预计总用量
    private String xqsl;//需求数量
    private String yjcml;//预计采买量
    private String jsid;//角色id
    private String scsl;//生产数量
    private String qlsl;//请领数量
    private String klsl;//可领数量
    private String yds;//预定数
    private String ckhwid;//仓库货物id
    private String lbmc;//物料质量类别名称
    private String lbcskz1;//物料质量类别参数扩展1
    private String ckid;//仓库id
    private String ckmc;//仓库名称
    private String bz;//备注
    private String xzbj;//限制标记

    public String getXzbj() {
        return xzbj;
    }

    public void setXzbj(String xzbj) {
        this.xzbj = xzbj;
    }

    public String getBz() {
        return bz;
    }

    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getYds() {
        return yds;
    }

    public void setYds(String yds) {
        this.yds = yds;
    }

    public String getCkhwid() {
        return ckhwid;
    }

    public void setCkhwid(String ckhwid) {
        this.ckhwid = ckhwid;
    }

    public String getLbmc() {
        return lbmc;
    }

    public void setLbmc(String lbmc) {
        this.lbmc = lbmc;
    }

    public String getLbcskz1() {
        return lbcskz1;
    }

    public void setLbcskz1(String lbcskz1) {
        this.lbcskz1 = lbcskz1;
    }

    public String getCkid() {
        return ckid;
    }

    public void setCkid(String ckid) {
        this.ckid = ckid;
    }

    public String getCkmc() {
        return ckmc;
    }

    public void setCkmc(String ckmc) {
        this.ckmc = ckmc;
    }

    public String getKlsl() {
        return klsl;
    }

    public void setKlsl(String klsl) {
        this.klsl = klsl;
    }

    public String getQlsl() {
        return qlsl;
    }

    public void setQlsl(String qlsl) {
        this.qlsl = qlsl;
    }

    public String getScsl() {
        return scsl;
    }

    public void setScsl(String scsl) {
        this.scsl = scsl;
    }

    public String getJsid() {
        return jsid;
    }

    public void setJsid(String jsid) {
        this.jsid = jsid;
    }

    public String getXqsl() {
        return xqsl;
    }

    public void setXqsl(String xqsl) {
        this.xqsl = xqsl;
    }

    public String getYjcml() {
        return yjcml;
    }

    public void setYjcml(String yjcml) {
        this.yjcml = yjcml;
    }

    public String getYjzyl() {
        return yjzyl;
    }

    public void setYjzyl(String yjzyl) {
        this.yjzyl = yjzyl;
    }

    public String getKcl() {
        return kcl;
    }

    public void setKcl(String kcl) {
        this.kcl = kcl;
    }

    public String getWlid() {
        return wlid;
    }

    public void setWlid(String wlid) {
        this.wlid = wlid;
    }

    public String getWllbmc() {
        return wllbmc;
    }

    public void setWllbmc(String wllbmc) {
        this.wllbmc = wllbmc;
    }

    public String getWlzlbmc() {
        return wlzlbmc;
    }

    public void setWlzlbmc(String wlzlbmc) {
        this.wlzlbmc = wlzlbmc;
    }

    public String getWlzlbtcmc() {
        return wlzlbtcmc;
    }

    public void setWlzlbtcmc(String wlzlbtcmc) {
        this.wlzlbtcmc = wlzlbtcmc;
    }

    public String getYchh() {
        return ychh;
    }

    public void setYchh(String ychh) {
        this.ychh = ychh;
    }

    //复数xqjhmxids
    private List<String> xqjhmxids;
    public List<String> getXqjhmxids() {
        return xqjhmxids;
    }
    public void setXqjhmxids(String xqjhmxids) {
        List<String> list = new ArrayList<>();
        if(StringUtil.isNotBlank(xqjhmxids)) {
            String[] str = xqjhmxids.split(",");
            list = Arrays.asList(str);
        }
        this.xqjhmxids = list;
    }
    public void setXqjhmxids(List<String> xqjhmxids) {
        if(!CollectionUtils.isEmpty(xqjhmxids)){
            xqjhmxids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
        }
        this.xqjhmxids = xqjhmxids;
    }

    public String getXqjhmxid() {
        return xqjhmxid;
    }

    public void setXqjhmxid(String xqjhmxid) {
        this.xqjhmxid = xqjhmxid;
    }

    public String getBbrq() {
        return bbrq;
    }

    public void setBbrq(String bbrq) {
        this.bbrq = bbrq;
    }

    public String getBbh() {
        return bbh;
    }

    public void setBbh(String bbh) {
        this.bbh = bbh;
    }

    public String getBbsm() {
        return bbsm;
    }

    public void setBbsm(String bbsm) {
        this.bbsm = bbsm;
    }

    public String getZwlbm() {
        return zwlbm;
    }

    public void setZwlbm(String zwlbm) {
        this.zwlbm = zwlbm;
    }

    public String getBomlb() {
        return bomlb;
    }

    public void setBomlb(String bomlb) {
        this.bomlb = bomlb;
    }

    public String getBomlbmc() {
        return bomlbmc;
    }

    public void setBomlbmc(String bomlbmc) {
        this.bomlbmc = bomlbmc;
    }

    public String getMjwlbm() {
        return mjwlbm;
    }

    public void setMjwlbm(String mjwlbm) {
        this.mjwlbm = mjwlbm;
    }

    public String getMjwlid() {
        return mjwlid;
    }

    public void setMjwlid(String mjwlid) {
        this.mjwlid = mjwlid;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
    }

    public String getScs() {
        return scs;
    }

    public void setScs(String scs) {
        this.scs = scs;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;
}
