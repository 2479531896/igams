package com.matridx.igams.storehouse.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value = "CkxxDto")
public class CkxxDto extends CkxxModel {
    //申请人名称
    private String sqrmc;
    //供应商
    private String gys;
    //创建日期
    private String cjrq;
    //币种名称
    private String bizmc;
    //付款方式名称
    private String fkfsmc;
    //付款方名称
    private String fkfmc;
    //税率
    private String suil;
    //单价
    private String wsdj;
    //计划到货日期
    private String jhdhrq;
    //金额
    private String hjje;
    //供应商名称
    private String gysmc;
    //到货单号
    private String dhdh;
    //检验单号
    private String jydh;
    //检验日期
    private String jyrq;
    //入库日期
    private String rkrq;

    private String rkdh;
    private String u8rkdh;
    private String dhrq;
    private String kwmc;
    //父仓库名称
    private String fckmc;
    //录入人员
    private String lrrymc;
    //仓库类别
    private String cklbmc;
    //是否为父仓库标记
    private String fckflg;
    //录入时间格式化
    private String f_lrsj;
    //仓库类别
    private String[] cklbs;
    //仓库分类
    private String[] ckfls;
    //仓库类别代码
    private String cklbdm;
    //到货开始日期
    private String startTime;
    //到货结束日期
    private String endTime;
    private String lldh;
    private String ckrq;
    private String llr;
    private String flr;
    private String ckdh;
    private String sqr;
    private String fhdh;
    private String oaxsdh;
    //机构名称
    private String jgmc;
    //客户名称
    private String khmc;
    //到货数量
    private String dhsl;
    //出库数量
    private String cksl;
    //数量
    private String sl;
    //种类
    private String zl;
    //类型
    private String lx;
    //类别名称
    private String lbmc;
    //物料编码
    private String wlbm;
    //物料名称
    private String wlmc;
    //规格
    private String gg;
    //原厂货号
    private String ychh;
    //计量单位
    private String jldw;
    //生产批号
    private String scph;
    //追朔号
    private String zsh;
    //库存量
    private String kcl;
    //生产日期
    private String scrq;
    //有效期
    private String yxq;
    //单据号
    private String djh;
    //项目大类名称
    private String xmdlmc;
    //合同内部编号
    private String htnbbh;
    //物料种类
    private String wlzl;
    //物料数量
    private String wlsl;
    //流程
    private String lc;
    //发货退货标记
    private String sfbj;

    private String sqrq;
    private String jlbh;
    private String xmbmmc;
    private String sqbmmc;
    private String jg;
    private String sqly;
    //物料类别
    private String wllbmc;
    //检验结果
    private String jyjgmc;
    //质检数量
    private String zjsl;
    //取样量
    private String qyl;
    //借用数量
    private String jysl;
    //不合格数量
    private String bhgsl;
    //岗位名称
    private String gwmc;
    //流程序号
    private String lcxh;
    //审批平均时间
    private String avg_day;
    //审批平均时间
    private String shlb;    //审批平均时间
    private String csdm;
    //限制标记
    private String xzbj;
    //角色ID
    private String jsid;

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

    public String getShlb() {
        return shlb;
    }

    public void setShlb(String shlb) {
        this.shlb = shlb;
    }

    public String getCsdm() {
        return csdm;
    }

    public void setCsdm(String csdm) {
        this.csdm = csdm;
    }

    public String getGwmc() {
        return gwmc;
    }

    public void setGwmc(String gwmc) {
        this.gwmc = gwmc;
    }

    public String getLcxh() {
        return lcxh;
    }

    public void setLcxh(String lcxh) {
        this.lcxh = lcxh;
    }

    public String getAvg_day() {
        return avg_day;
    }

    public void setAvg_day(String avg_day) {
        this.avg_day = avg_day;
    }

    public String getJyjgmc() {
        return jyjgmc;
    }

    public void setJyjgmc(String jyjgmc) {
        this.jyjgmc = jyjgmc;
    }

    public String getZjsl() {
        return zjsl;
    }

    public void setZjsl(String zjsl) {
        this.zjsl = zjsl;
    }

    public String getQyl() {
        return qyl;
    }

    public void setQyl(String qyl) {
        this.qyl = qyl;
    }

    public String getJysl() {
        return jysl;
    }

    public void setJysl(String jysl) {
        this.jysl = jysl;
    }

    public String getBhgsl() {
        return bhgsl;
    }

    public void setBhgsl(String bhgsl) {
        this.bhgsl = bhgsl;
    }

    public String getWllbmc() {
        return wllbmc;
    }

    public void setWllbmc(String wllbmc) {
        this.wllbmc = wllbmc;
    }

    public String getSqrq() {
		return sqrq;
	}

	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}

	public String getJlbh() {
		return jlbh;
	}

	public void setJlbh(String jlbh) {
		this.jlbh = jlbh;
	}

	public String getXmbmmc() {
		return xmbmmc;
	}

	public void setXmbmmc(String xmbmmc) {
		this.xmbmmc = xmbmmc;
	}

	public String getSqbmmc() {
		return sqbmmc;
	}

	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}

	public String getJg() {
		return jg;
	}

	public void setJg(String jg) {
		this.jg = jg;
	}

	public String getSqly() {
		return sqly;
	}

	public void setSqly(String sqly) {
		this.sqly = sqly;
	}

	public String getSfbj() {
        return sfbj;
    }

    public void setSfbj(String sfbj) {
        this.sfbj = sfbj;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }

    public String getJydh() {
        return jydh;
    }

    public void setJydh(String jydh) {
        this.jydh = jydh;
    }

    public String getFhdh() {
        return fhdh;
    }

    public void setFhdh(String fhdh) {
        this.fhdh = fhdh;
    }

    public String getOaxsdh() {
        return oaxsdh;
    }

    public void setOaxsdh(String oaxsdh) {
        this.oaxsdh = oaxsdh;
    }

    public String getLldh() {
        return lldh;
    }

    public void setLldh(String lldh) {
        this.lldh = lldh;
    }

    public String getCkrq() {
        return ckrq;
    }

    public void setCkrq(String ckrq) {
        this.ckrq = ckrq;
    }

    public String getLlr() {
        return llr;
    }

    public void setLlr(String llr) {
        this.llr = llr;
    }

    public String getFlr() {
        return flr;
    }

    public void setFlr(String flr) {
        this.flr = flr;
    }

    public String getCkdh() {
        return ckdh;
    }

    public void setCkdh(String ckdh) {
        this.ckdh = ckdh;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getWlzl() {
        return wlzl;
    }

    public void setWlzl(String wlzl) {
        this.wlzl = wlzl;
    }

    public String getWlsl() {
        return wlsl;
    }

    public void setWlsl(String wlsl) {
        this.wlsl = wlsl;
    }

    public String getWlbm() {
        return wlbm;
    }

    public void setWlbm(String wlbm) {
        this.wlbm = wlbm;
    }

    public String getWlmc() {
        return wlmc;
    }

    public void setWlmc(String wlmc) {
        this.wlmc = wlmc;
    }

    public String getYchh() {
        return ychh;
    }

    public void setYchh(String ychh) {
        this.ychh = ychh;
    }

    public String getZsh() {
        return zsh;
    }

    public void setZsh(String zsh) {
        this.zsh = zsh;
    }

    public String getYxq() {
        return yxq;
    }

    public void setYxq(String yxq) {
        this.yxq = yxq;
    }

    public String getXmdlmc() {
        return xmdlmc;
    }

    public void setXmdlmc(String xmdlmc) {
        this.xmdlmc = xmdlmc;
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

    public String getScph() {
        return scph;
    }

    public void setScph(String scph) {
        this.scph = scph;
    }

    public String getKcl() {
        return kcl;
    }

    public void setKcl(String kcl) {
        this.kcl = kcl;
    }

    public String getScrq() {
        return scrq;
    }

    public void setScrq(String scrq) {
        this.scrq = scrq;
    }

    public String getDjh() {
        return djh;
    }

    public void setDjh(String djh) {
        this.djh = djh;
    }

    public String getHtnbbh() {
        return htnbbh;
    }

    public void setHtnbbh(String htnbbh) {
        this.htnbbh = htnbbh;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getZl() {
        return zl;
    }

    public void setZl(String zl) {
        this.zl = zl;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getLbmc() {
        return lbmc;
    }

    public void setLbmc(String lbmc) {
        this.lbmc = lbmc;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getJgmc() {
        return jgmc;
    }

    public void setJgmc(String jgmc) {
        this.jgmc = jgmc;
    }

    public String getDhsl() {
        return dhsl;
    }

    public void setDhsl(String dhsl) {
        this.dhsl = dhsl;
    }

    public String getCksl() {
        return cksl;
    }

    public void setCksl(String cksl) {
        this.cksl = cksl;
    }

    public String getCklbdm() {
        return cklbdm;
    }

    public void setCklbdm(String cklbdm) {
        this.cklbdm = cklbdm;
    }

    public String[] getCklbs() {
        return cklbs;
    }

    public void setCklbs(String[] cklbs) {
        this.cklbs = cklbs;
    }

    public String[] getCkfls() {
        return ckfls;
    }

    public void setCkfls(String[] ckfls) {
        this.ckfls = ckfls;
    }

    public String getFckflg() {
        return fckflg;
    }


    public void setFckflg(String fckflg) {
        this.fckflg = fckflg;
    }


    public String getFckmc() {
        return fckmc;
    }


    public void setFckmc(String fckmc) {
        this.fckmc = fckmc;
    }


    public String getLrrymc() {
        return lrrymc;
    }


    public void setLrrymc(String lrrymc) {
        this.lrrymc = lrrymc;
    }


    public String getCklbmc() {
        return cklbmc;
    }


    public void setCklbmc(String cklbmc) {
        this.cklbmc = cklbmc;
    }


    public String getF_lrsj() {
        return f_lrsj;
    }


    public void setF_lrsj(String f_lrsj) {
        this.f_lrsj = f_lrsj;
    }

    public String getKhmc() {
        return khmc;
    }

    public void setKhmc(String khmc) {
        this.khmc = khmc;
    }


    public String getRkdh() {
        return rkdh;
    }

    public void setRkdh(String rkdh) {
        this.rkdh = rkdh;
    }

    public String getU8rkdh() {
        return u8rkdh;
    }

    public void setU8rkdh(String u8rkdh) {
        this.u8rkdh = u8rkdh;
    }

    public String getDhrq() {
        return dhrq;
    }

    public void setDhrq(String dhrq) {
        this.dhrq = dhrq;
    }

    public String getKwmc() {
        return kwmc;
    }

    public void setKwmc(String kwmc) {
        this.kwmc = kwmc;
    }

    public String getSqrmc() {
        return sqrmc;
    }

    public void setSqrmc(String sqrmc) {
        this.sqrmc = sqrmc;
    }

    public String getGys() {
        return gys;
    }

    public void setGys(String gys) {
        this.gys = gys;
    }

    public String getCjrq() {
        return cjrq;
    }

    public void setCjrq(String cjrq) {
        this.cjrq = cjrq;
    }

    public String getBizmc() {
        return bizmc;
    }

    public void setBizmc(String bizmc) {
        this.bizmc = bizmc;
    }

    public String getFkfsmc() {
        return fkfsmc;
    }

    public void setFkfsmc(String fkfsmc) {
        this.fkfsmc = fkfsmc;
    }

    public String getFkfmc() {
        return fkfmc;
    }

    public void setFkfmc(String fkfmc) {
        this.fkfmc = fkfmc;
    }

    public String getSuil() {
        return suil;
    }

    public void setSuil(String suil) {
        this.suil = suil;
    }

    public String getWsdj() {
        return wsdj;
    }

    public void setWsdj(String wsdj) {
        this.wsdj = wsdj;
    }

    public String getJhdhrq() {
        return jhdhrq;
    }

    public void setJhdhrq(String jhdhrq) {
        this.jhdhrq = jhdhrq;
    }

    public String getHjje() {
        return hjje;
    }

    public void setHjje(String hjje) {
        this.hjje = hjje;
    }

    public String getGysmc() {
        return gysmc;
    }

    public void setGysmc(String gysmc) {
        this.gysmc = gysmc;
    }

    public String getDhdh() {
        return dhdh;
    }

    public void setDhdh(String dhdh) {
        this.dhdh = dhdh;
    }


    public String getJyrq() {
        return jyrq;
    }

    public void setJyrq(String jyrq) {
        this.jyrq = jyrq;
    }

    public String getRkrq() {
        return rkrq;
    }

    public void setRkrq(String rkrq) {
        this.rkrq = rkrq;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
