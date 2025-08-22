package com.matridx.igams.production.dao.entities;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Alias("XqjhmxDto")
public class XqjhmxDto extends XqjhmxModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //物料名称
    private String wlmc;
    //物料编码
    private String wlbm;
    //生产商
    private String scs;
    //规格
    private String gg;
    //计量单位
    private String jldw;
    //序列号
    private String xlh;
    //生产数量标记
    private String scslbj;
    //领料类别
    private String lllb;
    //参数扩展1
    private String cskz1;
    //仓库分类ids
    private List<String> ckqxids;
    //库存量
    private String kcl;
    //可领数量
    private String klsl;
    //仓库货物id
    private String ckhwid;
    //仓库权限名称
    private String ckqxmc;
    //仓库权限类型
    private String ckqxlx;
    //复数sczts
    private List<String> sczts;
    //复数lxs
    private List<String> lxs;
    private String wllbmc;//物料类别名称
    private String wlzlbmc;//物料子类别名称
    private String wlzlbtcmc;//物料子类别统称
    private String ychh;//原厂货号
	private String ztmc;
	private String sfscmc;//是否生产
    private String rksj;//入库时间
    private String xqrq;//需求日期
    private String xqdh;//需求单号
    private String dhsl;//入库数量
    private String sqrmc;//申请人
    private String rkzt;//入库状态
	private String yhm;//用户名
    private String rksjstart;
    private String rksjend;
    private String sqsjstart;
    private String sqsjend;
    private String[] rkzts;//入库状态多
    private String entire;
    private String cpxqid;//产品需求id
    private String sqlParam;
    private String zt;//状态
    private String[] zts;//状态多
    private String kscsl;//可生产数量
    private String jsid;//角色id
    private String xzbj;//限制标记
    private String ckmc;//仓库名称

    public String getCkmc() {
        return ckmc;
    }

    public void setCkmc(String ckmc) {
        this.ckmc = ckmc;
    }

    public String getXzbj() {
        return xzbj;
    }

    public void setXzbj(String xzbj) {
        this.xzbj = xzbj;
    }

    public String getJsid() {
        return jsid;
    }

    public void setJsid(String jsid) {
        this.jsid = jsid;
    }

    public String getKscsl() {
        return kscsl;
    }

    public void setKscsl(String kscsl) {
        this.kscsl = kscsl;
    }

    public String[] getZts() {
        return zts;
    }

    public void setZts(String[] zts) {
        this.zts = zts;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getSqlParam() {
        return sqlParam;
    }

    public void setSqlParam(String sqlParam) {
        this.sqlParam = sqlParam;
    }

    public String getCpxqid() {
        return cpxqid;
    }

    public void setCpxqid(String cpxqid) {
        this.cpxqid = cpxqid;
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String[] getRkzts() {
        return rkzts;
    }

    public void setRkzts(String[] rkzts) {
        this.rkzts = rkzts;
    }

    public String getRksjstart() {
        return rksjstart;
    }

    public void setRksjstart(String rksjstart) {
        this.rksjstart = rksjstart;
    }

    public String getRksjend() {
        return rksjend;
    }

    public void setRksjend(String rksjend) {
        this.rksjend = rksjend;
    }

    @Override
    public String getSqsjstart() {
        return sqsjstart;
    }

    @Override
    public void setSqsjstart(String sqsjstart) {
        this.sqsjstart = sqsjstart;
    }

    @Override
    public String getSqsjend() {
        return sqsjend;
    }

    @Override
    public void setSqsjend(String sqsjend) {
        this.sqsjend = sqsjend;
    }

    public String getRkzt() {
        return rkzt;
    }

    public void setRkzt(String rkzt) {
        this.rkzt = rkzt;
    }

    public String getSqrmc() {
        return sqrmc;
    }

    public void setSqrmc(String sqrmc) {
        this.sqrmc = sqrmc;
    }
    public String getDhsl() {
        return dhsl;
    }

    public void setDhsl(String dhsl) {
        this.dhsl = dhsl;
    }

    public String getXqdh() {
        return xqdh;
    }

    public void setXqdh(String xqdh) {
        this.xqdh = xqdh;
    }

    public String getXqrq() {
        return xqrq;
    }

    public void setXqrq(String xqrq) {
        this.xqrq = xqrq;
    }

    public String getRksj() {
        return rksj;
    }
	public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public void setRksj(String rksj) {
        this.rksj = rksj;
    }

    public String getSfscmc() {
        return sfscmc;
    }

    public void setSfscmc(String sfscmc) {
        this.sfscmc = sfscmc;
    }

    private List<SczlmxDto> sczlmxDtos;


    public List<SczlmxDto> getSczlmxDtos() {
        return sczlmxDtos;
    }

    public void setSczlmxDtos(List<SczlmxDto> sczlmxDtos) {
        this.sczlmxDtos = sczlmxDtos;
    }

    public String getZtmc() {
        return ztmc;
    }

    public void setZtmc(String ztmc) {
        this.ztmc = ztmc;
    }


    public String getYchh() {
        return ychh;
    }

    public void setYchh(String ychh) {
        this.ychh = ychh;
    }

    public String getWlzlbtcmc() {
        return wlzlbtcmc;
    }

    public void setWlzlbtcmc(String wlzlbtcmc) {
        this.wlzlbtcmc = wlzlbtcmc;
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

    public List<String> getLxs() {
        return lxs;
    }
    public void setLxs(String lxs) {
        List<String> list = new ArrayList<>();
        if(StringUtil.isNotBlank(lxs)) {
            String[] str = lxs.split(",");
            list = Arrays.asList(str);
        }
        this.lxs = list;
    }
    public void setLxs(List<String> lxs) {
        if(!CollectionUtils.isEmpty(lxs)) {
            lxs.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
        }
        this.lxs = lxs;
    }
    public List<String> getSczts() {
        return sczts;
    }
    public void setSczts(String sczts) {
        List<String> list = new ArrayList<>();
        if(StringUtil.isNotBlank(sczts)) {
            String[] str = sczts.split(",");
            list = Arrays.asList(str);
        }
        this.sczts = list;
    }
    public void setSczts(List<String> sczts) {
        if(!CollectionUtils.isEmpty(sczts)) {
            sczts.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
        }
        this.sczts = sczts;
    }

    public List<String> getCkqxids() {
        return ckqxids;
    }

    public void setCkqxids(List<String> ckqxids) {
        this.ckqxids = ckqxids;
    }

    public String getKcl() {
        return kcl;
    }

    public void setKcl(String kcl) {
        this.kcl = kcl;
    }

    public String getKlsl() {
        return klsl;
    }

    public void setKlsl(String klsl) {
        this.klsl = klsl;
    }

    public String getCkhwid() {
        return ckhwid;
    }

    public void setCkhwid(String ckhwid) {
        this.ckhwid = ckhwid;
    }

    public String getCkqxmc() {
        return ckqxmc;
    }

    public void setCkqxmc(String ckqxmc) {
        this.ckqxmc = ckqxmc;
    }

    public String getCkqxlx() {
        return ckqxlx;
    }

    public void setCkqxlx(String ckqxlx) {
        this.ckqxlx = ckqxlx;
    }

    public String getCskz1() {
        return cskz1;
    }

    public void setCskz1(String cskz1) {
        this.cskz1 = cskz1;
    }

    public String getLllb() {
        return lllb;
    }

    public void setLllb(String lllb) {
        this.lllb = lllb;
    }

    public String getScslbj() {
        return scslbj;
    }

    public void setScslbj(String scslbj) {
        this.scslbj = scslbj;
    }

    public String getXlh() {
        return xlh;
    }

    public void setXlh(String xlh) {
        this.xlh = xlh;
    }

    public String getScs() {
        return scs;
    }

    public void setScs(String scs) {
        this.scs = scs;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getJldw() {
        return jldw;
    }

    public void setJldw(String jldw) {
        this.jldw = jldw;
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
}
