package com.matridx.igams.storehouse.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="LlglDto")
public class LlglDto extends LlglModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//申请人名称
	private String sqrmc;
	//申请部门名称
	private String sqbmmc;
	//复数ID
	private List<String> wlids;
	//访问标记
	private String fwbj;
	//领料明细
	private String llmx_json;
	//业务类型
	private String ywlx;
	//钉钉审批ID
	private String ddspid;
	//钉钉审批人用户id
	private String sprid;
	//钉钉审批人用户姓名
	private String sprxm;
	//钉钉审批人钉钉ID
	private String sprddid;
	//钉钉审批人用户名
	private String spryhm;
	//钉钉审批人角色ID
	private String sprjsid;
	//钉钉审批人角色名称
	private String sprjsmc;
	//附件IDS
	private List<String> fjids;
	//货物领料明细
	private String hwllmx_json;
	//区分不同的审核页面
	private String xsbj;
	private String wlbm;
	// 物料名称(wlgl.wlmc)
	private String wlmc;
	// 追溯号(hwxx.zsh)
	private String zsh;
	// 报关单号(hwxx.bgdh)
	private String bgdh;
	// 质检人员(hwxx.zjry)
	private String zjry;
	// 分组(wlgl.wllb)
	private String fz;
	// 类别(根据wlgl.lb的基础数据扩展参数1,jcsj.csid)
	private String lb;
	//申请日期开始
	private String sqrqstart;
	//申请日期结束
	private String sqrqend;
	//领料日期开始（发料日期）
	private String flrqstart;
	//领料日期结束
	private String flrqend;
	//导出关联标记为所选择的字段
	private String sqlParam;
	// 到货日期(dhxx.dhrq)
	private String dhrq;
	// 供货商(gysxx.gysmc)
	private String gysmc;
	// 全部(模糊搜索用)
	private String entire;
	//可请领数
	private String kqls;
	// 库存数
	private String kcl;
	// 分组 数组
	private String[] fzs;
	// 类别 数组
	private String[] lbs;
	// 审核状态 数组
	private String[] zts;
	// 预定数
	private String qlyds;
	//出库参数代码
	private String ckcsdm;
	//机构代码
	private String jgdm;
	//领料人名称
	private String llrmc;
	//发料人名称
	private String flrmc;
	//仓库名称
	private String ckmc;
	//仓库代码
	private String ckdm;
	//真实姓名
	private String zsxm;
	//生产批号
	private String scph;
	//钉钉标记
	private String ddbj;
	//领料 ids
	private List<String> llids;
	//废弃标记（判断删除的数据是否为废弃）
	private String fqbj;
	//rabbit标记
	private String prefixFlg;
	// 货物领料信息
	private List<HwllxxDto> hwllxxDtos;
	//规格
	private String gg;
	//请领数量
	private String qlsl;
	//实领数量
	private String slsl;
	//项目编码
	private String xmbm;
	//项目大类
	private String xmdl;
	//计量单位
	private String jldw;
	//审核日期
	private String shrq;
	private String sum_qlsl;
	private String sum_slsl;
	//物料id
	private String wlid;
	//机构的扩展参数1
	private String jgdh;
	//机构ID
	private String jgid;
	//仓库分类
	private String ckqxlx;
	//仓库货物id
	private String ckhwid;
	//货物领料明细
	private List<HwllmxDto> hwllmxDtos;
	//提交标记
	private String tjbj;
	//审核人名称
	private String shrmc;
	//钉钉搜索条件
	private String ddentire;
	//客户简称
	private String khjc;
	//终端区域名称
	private String zdqymc;
	//归属人名称
	private String gsrmc;
	//是否签收
	private String sfqs;
	//客户类别s
	private String[] khlbs;
	//产品类型
	private String cplx;
	//销售类型名称
	private String xslxmc;
	//归属人部门名称
	private String gsrbmmc;
	//归属人部门代码
	private String gsrbmdm;
	//合同id
	private String htid;
	//部门设备负责人
	private String bmsbfzrmc;
	private String dcflag;//导出标记
	private String wlbm_t;
	private String wlmc_t;
	private String sjwcsj;//实际完成时间
	private String dhsl;//到货数量
	private String jhcl;//计划产量
	private String fzrmc;//负责人
	private String yfzr;//原负责人
	private String ysfsmc;//运输方式名称

	private String khmc;//客户名称
	//限制标记 库存领料限制用
	private String xzbj;
	//仓库id
	private String ckid;
    //领料明细ids
    List<String> llmxids;
	private String hsdj;//含税单价
	private String zje;//总金额
	private String lbcskz1;

	public String getLbcskz1() {
		return lbcskz1;
	}

	public void setLbcskz1(String lbcskz1) {
		this.lbcskz1 = lbcskz1;
	}

	public String getHsdj() {
		return hsdj;
	}

	public void setHsdj(String hsdj) {
		this.hsdj = hsdj;
	}

	public String getZje() {
		return zje;
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public List<String> getLlmxids() {
        return llmxids;
    }

    public void setLlmxids(List<String> llmxids) {
        this.llmxids = llmxids;
    }
	@Override
	public String getCkid() {
		return ckid;
	}

	@Override
	public void setCkid(String ckid) {
		this.ckid = ckid;
	}

	public String getXzbj() {
		return xzbj;
	}

	public void setXzbj(String xzbj) {
		this.xzbj = xzbj;
	}

	public String getKhmc() {
		return khmc;
	}

	public void setKhmc(String khmc) {
		this.khmc = khmc;
	}

	public String getYsfsmc() {
		return ysfsmc;
	}

	public void setYsfsmc(String ysfsmc) {
		this.ysfsmc = ysfsmc;
	}
	public String getYfzr() {
		return yfzr;
	}

	public void setYfzr(String yfzr) {
		this.yfzr = yfzr;
	}

	public String getFzrmc() {
		return fzrmc;
	}

	public void setFzrmc(String fzrmc) {
		this.fzrmc = fzrmc;
	}

	public String getJhcl() {
		return jhcl;
	}

	public void setJhcl(String jhcl) {
		this.jhcl = jhcl;
	}

	public String getWlbm_t() {
		return wlbm_t;
	}

	public void setWlbm_t(String wlbm_t) {
		this.wlbm_t = wlbm_t;
	}

	public String getWlmc_t() {
		return wlmc_t;
	}

	public void setWlmc_t(String wlmc_t) {
		this.wlmc_t = wlmc_t;
	}

	public String getSjwcsj() {
		return sjwcsj;
	}

	public void setSjwcsj(String sjwcsj) {
		this.sjwcsj = sjwcsj;
	}

	public String getDhsl() {
		return dhsl;
	}

	public void setDhsl(String dhsl) {
		this.dhsl = dhsl;
	}

	public String getScgs() {
		return scgs;
	}

	public void setScgs(String scgs) {
		this.scgs = scgs;
	}

	private String scgs;//生产工时

	public String getDcflag() {
		return dcflag;
	}

	public void setDcflag(String dcflag) {
		this.dcflag = dcflag;
	}
	//指令单号
	private String zldh;

	public String getZldh() {
		return zldh;
	}

	public void setZldh(String zldh) {
		this.zldh = zldh;
	}

	public String getBmsbfzrmc() {
		return bmsbfzrmc;
	}

	public void setBmsbfzrmc(String bmsbfzrmc) {
		this.bmsbfzrmc = bmsbfzrmc;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getGsrbmmc() {
		return gsrbmmc;
	}

	public void setGsrbmmc(String gsrbmmc) {
		this.gsrbmmc = gsrbmmc;
	}

	public String getGsrbmdm() {
		return gsrbmdm;
	}

	public void setGsrbmdm(String gsrbmdm) {
		this.gsrbmdm = gsrbmdm;
	}

	public String getXslxmc() {
		return xslxmc;
	}

	public void setXslxmc(String xslxmc) {
		this.xslxmc = xslxmc;
	}

	public String getCplx() {
		return cplx;
	}

	public void setCplx(String cplx) {
		this.cplx = cplx;
	}

	public String[] getKhlbs() {
		return khlbs;
	}

	public String getSfqs() {
		return sfqs;
	}

	public void setSfqs(String sfqs) {
		this.sfqs = sfqs;
	}

	public void setKhlbs(String[] khlbs) {
		this.khlbs = khlbs;
		for (int i = 0; i < khlbs.length; i++){
			this.khlbs[i]=this.khlbs[i].replace("'","");
		}
	}

	public String getZdqymc() {
		return zdqymc;
	}

	public void setZdqymc(String zdqymc) {
		this.zdqymc = zdqymc;
	}

	public String getGsrmc() {
		return gsrmc;
	}

	public void setGsrmc(String gsrmc) {
		this.gsrmc = gsrmc;
	}

	public String getKhjc() {
		return khjc;
	}

	public void setKhjc(String khjc) {
		this.khjc = khjc;
	}

	public String getDdentire() {
		return ddentire;
	}

	public void setDdentire(String ddentire) {
		this.ddentire = ddentire;
	}

	private String queryWlids;

	public String getQueryWlids() {
		return queryWlids;
	}

	public void setQueryWlids(String queryWlids) {
		this.queryWlids = queryWlids;
	}

	public String getShrmc() {
		return shrmc;
	}

	public void setShrmc(String shrmc) {
		this.shrmc = shrmc;
	}

	public List<String> getWlids() {
		return wlids;
	}

	public void setWlids(List<String> wlids) {
		this.wlids = wlids;
	}

	//审核时间
	private String shsj;

	public String getSprjsid() {
		return sprjsid;
	}

	public void setSprjsid(String sprjsid) {
		this.sprjsid = sprjsid;
	}

	public String getSprjsmc() {
		return sprjsmc;
	}

	public void setSprjsmc(String sprjsmc) {
		this.sprjsmc = sprjsmc;
	}

	public String getSprid() {
		return sprid;
	}

	public void setSprid(String sprid) {
		this.sprid = sprid;
	}

	public String getSprxm() {
		return sprxm;
	}

	public void setSprxm(String sprxm) {
		this.sprxm = sprxm;
	}

	public String getSprddid() {
		return sprddid;
	}

	public void setSprddid(String sprddid) {
		this.sprddid = sprddid;
	}

	public String getSpryhm() {
		return spryhm;
	}

	public void setSpryhm(String spryhm) {
		this.spryhm = spryhm;
	}

	public String getDdspid() {
		return ddspid;
	}

	public void setDdspid(String ddspid) {
		this.ddspid = ddspid;
	}

	public String getShsj() {
		return shsj;
	}

	public void setShsj(String shsj) {
		this.shsj = shsj;
	}

	public String getTjbj() {
		return tjbj;
	}

	public void setTjbj(String tjbj) {
		this.tjbj = tjbj;
	}

	public String getCkhwid() {
		return ckhwid;
	}

	public void setCkhwid(String ckhwid) {
		this.ckhwid = ckhwid;
	}

	public String getCkqxlx() {
		return ckqxlx;
	}

	public void setCkqxlx(String ckqxlx) {
		this.ckqxlx = ckqxlx;
	}

	public List<HwllmxDto> getHwllmxDtos() {
		return hwllmxDtos;
	}

	public void setHwllmxDtos(List<HwllmxDto> hwllmxDtos) {
		this.hwllmxDtos = hwllmxDtos;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	public String getJgdh() {
		return jgdh;
	}

	public void setJgdh(String jgdh) {
		this.jgdh = jgdh;
	}

	public String getScph() {
		return scph;
	}

	public void setScph(String scph) {
		this.scph = scph;
	}

	public String getGg() {
		return gg;
	}

	public void setGg(String gg) {
		this.gg = gg;
	}

	public String getQlsl() {
		return qlsl;
	}

	public void setQlsl(String qlsl) {
		this.qlsl = qlsl;
	}

	public String getSlsl() {
		return slsl;
	}

	public void setSlsl(String slsl) {
		this.slsl = slsl;
	}

	public String getXmbm() {
		return xmbm;
	}

	public void setXmbm(String xmbm) {
		this.xmbm = xmbm;
	}

	public String getXmdl() {
		return xmdl;
	}

	public void setXmdl(String xmdl) {
		this.xmdl = xmdl;
	}

	public String getJldw() {
		return jldw;
	}

	public void setJldw(String jldw) {
		this.jldw = jldw;
	}

	public String getWlid() {
		return wlid;
	}

	public void setWlid(String wlid) {
		this.wlid = wlid;
	}

	public String getPrefixFlg() {
		return prefixFlg;
	}

	public void setPrefixFlg(String prefixFlg) {
		this.prefixFlg = prefixFlg;
	}

	public String getDdbj() {
		return ddbj;
	}

	public void setDdbj(String ddbj) {
		this.ddbj = ddbj;
	}

	public String getFqbj() {
		return fqbj;
	}

	public void setFqbj(String fqbj) {
		this.fqbj = fqbj;
	}

	public String getSum_qlsl() {
		return sum_qlsl;
	}
	public void setSum_qlsl(String sum_qlsl) {
		this.sum_qlsl = sum_qlsl;
	}
	public String getSum_slsl() {
		return sum_slsl;
	}
	public void setSum_slsl(String sum_slsl) {
		this.sum_slsl = sum_slsl;
	}
	public String getShrq() {
		return shrq;
	}
	public void setShrq(String shrq) {
		this.shrq = shrq;
	}
	public List<HwllxxDto> getHwllxxDtos() {
		return hwllxxDtos;
	}
	public void setHwllxxDtos(List<HwllxxDto> hwllxxDtos) {
		this.hwllxxDtos = hwllxxDtos;
	}
	public List<String> getLlids() {
		return llids;
	}
	public void setLlids(List<String> llids) {
		this.llids = llids;
	}
	public String getZsxm() {
		return zsxm;
	}
	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}
	public String getCkdm() {
		return ckdm;
	}
	public void setCkdm(String ckdm) {
		this.ckdm = ckdm;
	}
	public String getCkmc() {
		return ckmc;
	}
	public void setCkmc(String ckmc) {
		this.ckmc = ckmc;
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
	public String getJgdm() {
		return jgdm;
	}
	public void setJgdm(String jgdm) {
		this.jgdm = jgdm;
	}
	public String getCkcsdm() {
		return ckcsdm;
	}
	public void setCkcsdm(String ckcsdm) {
		this.ckcsdm = ckcsdm;
	}
	public String getXsbj() {
			return xsbj;
	}
	public void setXsbj(String xsbj) {
		this.xsbj = xsbj;
	}
	public String getBgdh() {
		return bgdh;
	}
	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}
	public String getZjry() {
		return zjry;
	}
	public void setZjry(String zjry) {
		this.zjry = zjry;
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
	public String getZsh() {
		return zsh;
	}
	public void setZsh(String zsh) {
		this.zsh = zsh;
	}
	public String getFz() {
		return fz;
	}
	public void setFz(String fz) {
		this.fz = fz;
	}
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	public String getSqrqstart() {
		return sqrqstart;
	}
	public void setSqrqstart(String sqrqstart) {
		this.sqrqstart = sqrqstart;
	}
	public String getSqrqend() {
		return sqrqend;
	}
	public void setSqrqend(String sqrqend) {
		this.sqrqend = sqrqend;
	}
	public String getFlrqstart() {
		return flrqstart;
	}
	public void setFlrqstart(String flrqstart) {
		this.flrqstart = flrqstart;
	}
	public String getFlrqend() {
		return flrqend;
	}
	public void setFlrqend(String flrqend) {
		this.flrqend = flrqend;
	}
	public String getSqlParam() {
		return sqlParam;
	}
	public void setSqlParam(String sqlParam) {
		this.sqlParam = sqlParam;
	}
	public String getDhrq() {
		return dhrq;
	}
	public void setDhrq(String dhrq) {
		this.dhrq = dhrq;
	}
	public String getGysmc() {
		return gysmc;
	}
	public void setGysmc(String gysmc) {
		this.gysmc = gysmc;
	}
	public String getEntire() {
		return entire;
	}
	public void setEntire(String entire) {
		this.entire = entire;
	}
	public String getKqls() {
		return kqls;
	}
	public void setKqls(String kqls) {
		this.kqls = kqls;
	}
	public String getKcl() {
		return kcl;
	}
	public void setKcl(String kcl) {
		this.kcl = kcl;
	}
	public String[] getFzs() {
		return fzs;
	}
	public void setFzs(String[] fzs) {
		this.fzs = fzs;
		for (int i = 0; i < fzs.length; i++){
			this.fzs[i]=this.fzs[i].replace("'","");
		}
	}
	public String[] getLbs() {
		return lbs;
	}
	public void setLbs(String[] lbs) {
		this.lbs = lbs;
		for (int i = 0; i < lbs.length; i++){
			this.lbs[i]=this.lbs[i].replace("'","");
		}
	}
	public String[] getZts() {
		return zts;
	}
	public void setZts(String[] zts) {
		this.zts = zts;
		for (int i = 0; i < zts.length; i++){
			this.zts[i]=this.zts[i].replace("'","");
		}
	}
	public String getQlyds() {
		return qlyds;
	}
	public void setQlyds(String qlyds) {
		this.qlyds = qlyds;
	}
	public String getHwllmx_json() {
		return hwllmx_json;
	}
	public void setHwllmx_json(String hwllmx_json) {
		this.hwllmx_json = hwllmx_json;
	}
	public List<String> getFjids() {
		return fjids;
	}
	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	public String getYwlx() {
		return ywlx;
	}
	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}
	public String getLlmx_json() {
		return llmx_json;
	}
	public void setLlmx_json(String llmx_json) {
		this.llmx_json = llmx_json;
	}
	public String getFwbj() {
		return fwbj;
	}
	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}
	public String getSqrmc() {
		return sqrmc;
	}
	public void setSqrmc(String sqrmc) {
		this.sqrmc = sqrmc;
	}
	public String getSqbmmc() {
		return sqbmmc;
	}
	public void setSqbmmc(String sqbmmc) {
		this.sqbmmc = sqbmmc;
	}
	
}
