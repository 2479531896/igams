package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="JnsjbgjlDto")
public class JnsjbgjlDto extends JnsjbgjlModel{

	private static final long serialVersionUID = 1L;
	//患者姓名
	private String xm;
	//患者性别
	private String xb;
	//患者年龄
	private String nl;
	//病理入组报告名称
	private String blrzdwmc;
	//病理入组科室名称
	private String ksmc;
	//copd   (是：1   否：0   不清楚：2)
	private String copd;
	//血液系统恶性肿瘤   (是：1   否：0   不清楚：2)
	private String xyxtexzl;
	//实体器官移植   (是：1   否：0   不清楚：2)
	private String stqgyz;
	//长期接受激素治疗   (是：1   否：0   不清楚：2)
	private String cqjsjszl;
	//自身免疫性疾病   (是：1   否：0   不清楚：2)
	private String zsmyjb;
	//糖尿病   (是：1   否：0   不清楚：2)
	private String tnb;
	//肿瘤性疾病   (是：1   否：0   不清楚：2)
	private String zlxjb;
	//冠心病   (是：1   否：0   不清楚：2)
	private String gxb;
	//脑血管疾病   (是：1   否：0   不清楚：2)
	private String nxgjb;
	//慢性肾病   (是：1   否：0   不清楚：2)
	private String mxsb;
	//炎症性肠病   (溃疡性结肠炎：1   克罗斯病：0   不清楚：2)
	private String yzxcb;
	//出院诊断
	private String cyzd;
	//三个月前是否有住院史   (是：1   否：0)
	private String sgyqsfyzys;
	//入院时间
	private String rysj;
	//出院时间
	private String cysj;
	//住院花费
	private String zyhf;
	//就诊类型名称
	private String jzlxmc;
	//性别名称
	private String xbmc;
	//美罗培南敏感
	private String mlpnmg;
	//万古霉素敏感
	private String wgmsmg;
	//利奈唑胺敏感
	private String lnzamg;
	//甲硝唑敏感
	private String jxzmg;
	//利福昔明敏感
	private String lfxmmg;
	//克林霉素敏感
	private String klmsmg;
	//检测梭菌检测结果json
	private String jnsjjcjg_json;
	//导出关联字段
	private String SqlParam;
	//艰难梭菌检测结果： 艰难梭菌检测时间
	private String jnsjjcsj;
	private String jnsjjcsj2;
	private String jnsjjcsj3;
	private String jnsjjcsj4;
	private String jnsjjcsj5;
	//艰难梭菌检测结果： GDH结果
	private String gdhjg;
	private String gdhjg2;
	private String gdhjg3;
	private String gdhjg4;
	private String gdhjg5;
	//艰难梭菌检测结果： TOXIN结果
	private String toxinjg;
	private String toxinjg2;
	private String toxinjg3;
	private String toxinjg4;
	private String toxinjg5;
	//艰难梭菌检测结果： 大便艰难梭菌培养结果
	private String dbjnsjpyjg;
	private String dbjnsjpyjg2;
	private String dbjnsjpyjg3;
	private String dbjnsjpyjg4;
	private String dbjnsjpyjg5;
	//艰难梭菌检测结果： 艰难梭菌霉素培养结果
    private String jnsjmspyjg;
	private String jnsjmspyjg2;
	private String jnsjmspyjg3;
	private String jnsjmspyjg4;
	private String jnsjmspyjg5;
	//艰难梭菌CDI治疗： 治疗药物
	private String zlyw_j;
	private String zlyw_t;
	private String zlyw_w;
	//艰难梭菌CDI治疗： 单次剂量
	private String dcjl_j;
	private String dcjl_t;
	private String dcjl_w;
	//艰难梭菌CDI治疗： 使用方法
	private String syff_j;
	private String syff_t;
	private String syff_w;
	//艰难梭菌CDI治疗： 开始日期
	private String ksrq_j;
	private String ksrq_t;
	private String ksrq_w;
	//艰难梭菌CDI治疗： 结束日期
	private String jsrq_j;
	private String jsrq_t;
	private String jsrq_w;
	//艰难梭菌之前用药史： 药物类型
	private String ywlx;
	private String ywlx2;
	private String ywlx3;
	private String ywlx4;
	private String ywlx5;
	//艰难梭菌之前用药史： 药物子类型.
	private String ywzlx;
	private String ywzlx2;
	private String ywzlx3;
	private String ywzlx4;
	private String ywzlx5;
	//艰难梭菌之前用药史： 药物
	private String yw;
	private String yw2;
	private String yw3;
	private String yw4;
	private String yw5;
	//艰难梭菌之前用药史： 剂量
	private String jl;
	private String jl2;
	private String jl3;
	private String jl4;
	private String jl5;
	//艰难梭菌之前用药史： 开始用药日期
	private String ksyyrq;
	private String ksyyrq2;
	private String ksyyrq3;
	private String ksyyrq4;
	private String ksyyrq5;
	//艰难梭菌之前用药史： 停药时间
	private String tyrq;
	private String tyrq2;
	private String tyrq3;
	private String tyrq4;
	private String tyrq5;
	//艰难梭菌之前用药史： 其他药物名称
	private String qtywmc;
	private String qtywmc2;
	private String qtywmc3;
	private String qtywmc4;
	private String qtywmc5;

	public String getBlrzdwmc() {
		return blrzdwmc;
	}

	public void setBlrzdwmc(String blrzdwmc) {
		this.blrzdwmc = blrzdwmc;
	}

	public String getKsmc() {
		return ksmc;
	}

	public void setKsmc(String ksmc) {
		this.ksmc = ksmc;
	}

	public String getYwlx2() {
		return ywlx2;
	}

	public void setYwlx2(String ywlx2) {
		this.ywlx2 = ywlx2;
	}

	public String getYwlx3() {
		return ywlx3;
	}

	public void setYwlx3(String ywlx3) {
		this.ywlx3 = ywlx3;
	}

	public String getYwlx4() {
		return ywlx4;
	}

	public void setYwlx4(String ywlx4) {
		this.ywlx4 = ywlx4;
	}

	public String getYwlx5() {
		return ywlx5;
	}

	public void setYwlx5(String ywlx5) {
		this.ywlx5 = ywlx5;
	}

	public String getYwzlx2() {
		return ywzlx2;
	}

	public void setYwzlx2(String ywzlx2) {
		this.ywzlx2 = ywzlx2;
	}

	public String getYwzlx3() {
		return ywzlx3;
	}

	public void setYwzlx3(String ywzlx3) {
		this.ywzlx3 = ywzlx3;
	}

	public String getYwzlx4() {
		return ywzlx4;
	}

	public void setYwzlx4(String ywzlx4) {
		this.ywzlx4 = ywzlx4;
	}

	public String getYwzlx5() {
		return ywzlx5;
	}

	public void setYwzlx5(String ywzlx5) {
		this.ywzlx5 = ywzlx5;
	}

	public String getYw2() {
		return yw2;
	}

	public void setYw2(String yw2) {
		this.yw2 = yw2;
	}

	public String getYw3() {
		return yw3;
	}

	public void setYw3(String yw3) {
		this.yw3 = yw3;
	}

	public String getYw4() {
		return yw4;
	}

	public void setYw4(String yw4) {
		this.yw4 = yw4;
	}

	public String getYw5() {
		return yw5;
	}

	public void setYw5(String yw5) {
		this.yw5 = yw5;
	}

	public String getJl2() {
		return jl2;
	}

	public void setJl2(String jl2) {
		this.jl2 = jl2;
	}

	public String getJl3() {
		return jl3;
	}

	public void setJl3(String jl3) {
		this.jl3 = jl3;
	}

	public String getJl4() {
		return jl4;
	}

	public void setJl4(String jl4) {
		this.jl4 = jl4;
	}

	public String getJl5() {
		return jl5;
	}

	public void setJl5(String jl5) {
		this.jl5 = jl5;
	}

	public String getKsyyrq2() {
		return ksyyrq2;
	}

	public void setKsyyrq2(String ksyyrq2) {
		this.ksyyrq2 = ksyyrq2;
	}

	public String getKsyyrq3() {
		return ksyyrq3;
	}

	public void setKsyyrq3(String ksyyrq3) {
		this.ksyyrq3 = ksyyrq3;
	}

	public String getKsyyrq4() {
		return ksyyrq4;
	}

	public void setKsyyrq4(String ksyyrq4) {
		this.ksyyrq4 = ksyyrq4;
	}

	public String getKsyyrq5() {
		return ksyyrq5;
	}

	public void setKsyyrq5(String ksyyrq5) {
		this.ksyyrq5 = ksyyrq5;
	}

	public String getTyrq2() {
		return tyrq2;
	}

	public void setTyrq2(String tyrq2) {
		this.tyrq2 = tyrq2;
	}

	public String getTyrq3() {
		return tyrq3;
	}

	public void setTyrq3(String tyrq3) {
		this.tyrq3 = tyrq3;
	}

	public String getTyrq4() {
		return tyrq4;
	}

	public void setTyrq4(String tyrq4) {
		this.tyrq4 = tyrq4;
	}

	public String getTyrq5() {
		return tyrq5;
	}

	public void setTyrq5(String tyrq5) {
		this.tyrq5 = tyrq5;
	}

	public String getQtywmc2() {
		return qtywmc2;
	}

	public void setQtywmc2(String qtywmc2) {
		this.qtywmc2 = qtywmc2;
	}

	public String getQtywmc3() {
		return qtywmc3;
	}

	public void setQtywmc3(String qtywmc3) {
		this.qtywmc3 = qtywmc3;
	}

	public String getQtywmc4() {
		return qtywmc4;
	}

	public void setQtywmc4(String qtywmc4) {
		this.qtywmc4 = qtywmc4;
	}

	public String getQtywmc5() {
		return qtywmc5;
	}

	public void setQtywmc5(String qtywmc5) {
		this.qtywmc5 = qtywmc5;
	}

	public String getZlyw_j() {
		return zlyw_j;
	}

	public void setZlyw_j(String zlyw_j) {
		this.zlyw_j = zlyw_j;
	}

	public String getZlyw_t() {
		return zlyw_t;
	}

	public void setZlyw_t(String zlyw_t) {
		this.zlyw_t = zlyw_t;
	}

	public String getZlyw_w() {
		return zlyw_w;
	}

	public void setZlyw_w(String zlyw_w) {
		this.zlyw_w = zlyw_w;
	}

	public String getDcjl_j() {
		return dcjl_j;
	}

	public void setDcjl_j(String dcjl_j) {
		this.dcjl_j = dcjl_j;
	}

	public String getDcjl_t() {
		return dcjl_t;
	}

	public void setDcjl_t(String dcjl_t) {
		this.dcjl_t = dcjl_t;
	}

	public String getDcjl_w() {
		return dcjl_w;
	}

	public void setDcjl_w(String dcjl_w) {
		this.dcjl_w = dcjl_w;
	}

	public String getSyff_j() {
		return syff_j;
	}

	public void setSyff_j(String syff_j) {
		this.syff_j = syff_j;
	}

	public String getSyff_t() {
		return syff_t;
	}

	public void setSyff_t(String syff_t) {
		this.syff_t = syff_t;
	}

	public String getSyff_w() {
		return syff_w;
	}

	public void setSyff_w(String syff_w) {
		this.syff_w = syff_w;
	}

	public String getKsrq_j() {
		return ksrq_j;
	}

	public void setKsrq_j(String ksrq_j) {
		this.ksrq_j = ksrq_j;
	}

	public String getKsrq_t() {
		return ksrq_t;
	}

	public void setKsrq_t(String ksrq_t) {
		this.ksrq_t = ksrq_t;
	}

	public String getKsrq_w() {
		return ksrq_w;
	}

	public void setKsrq_w(String ksrq_w) {
		this.ksrq_w = ksrq_w;
	}

	public String getJsrq_j() {
		return jsrq_j;
	}

	public void setJsrq_j(String jsrq_j) {
		this.jsrq_j = jsrq_j;
	}

	public String getJsrq_t() {
		return jsrq_t;
	}

	public void setJsrq_t(String jsrq_t) {
		this.jsrq_t = jsrq_t;
	}

	public String getJsrq_w() {
		return jsrq_w;
	}

	public void setJsrq_w(String jsrq_w) {
		this.jsrq_w = jsrq_w;
	}

	public String getJnsjjcsj2() {
		return jnsjjcsj2;
	}

	public void setJnsjjcsj2(String jnsjjcsj2) {
		this.jnsjjcsj2 = jnsjjcsj2;
	}

	public String getJnsjjcsj3() {
		return jnsjjcsj3;
	}

	public void setJnsjjcsj3(String jnsjjcsj3) {
		this.jnsjjcsj3 = jnsjjcsj3;
	}

	public String getJnsjjcsj4() {
		return jnsjjcsj4;
	}

	public void setJnsjjcsj4(String jnsjjcsj4) {
		this.jnsjjcsj4 = jnsjjcsj4;
	}

	public String getJnsjjcsj5() {
		return jnsjjcsj5;
	}

	public void setJnsjjcsj5(String jnsjjcsj5) {
		this.jnsjjcsj5 = jnsjjcsj5;
	}

	public String getGdhjg2() {
		return gdhjg2;
	}

	public void setGdhjg2(String gdhjg2) {
		this.gdhjg2 = gdhjg2;
	}

	public String getGdhjg3() {
		return gdhjg3;
	}

	public void setGdhjg3(String gdhjg3) {
		this.gdhjg3 = gdhjg3;
	}

	public String getGdhjg4() {
		return gdhjg4;
	}

	public void setGdhjg4(String gdhjg4) {
		this.gdhjg4 = gdhjg4;
	}

	public String getGdhjg5() {
		return gdhjg5;
	}

	public void setGdhjg5(String gdhjg5) {
		this.gdhjg5 = gdhjg5;
	}

	public String getToxinjg2() {
		return toxinjg2;
	}

	public void setToxinjg2(String toxinjg2) {
		this.toxinjg2 = toxinjg2;
	}

	public String getToxinjg3() {
		return toxinjg3;
	}

	public void setToxinjg3(String toxinjg3) {
		this.toxinjg3 = toxinjg3;
	}

	public String getToxinjg4() {
		return toxinjg4;
	}

	public void setToxinjg4(String toxinjg4) {
		this.toxinjg4 = toxinjg4;
	}

	public String getToxinjg5() {
		return toxinjg5;
	}

	public void setToxinjg5(String toxingjg5) {
		this.toxinjg5 = toxingjg5;
	}

	public String getDbjnsjpyjg2() {
		return dbjnsjpyjg2;
	}

	public void setDbjnsjpyjg2(String dbjnsjpyjg2) {
		this.dbjnsjpyjg2 = dbjnsjpyjg2;
	}

	public String getDbjnsjpyjg3() {
		return dbjnsjpyjg3;
	}

	public void setDbjnsjpyjg3(String dbjnsjpyjg3) {
		this.dbjnsjpyjg3 = dbjnsjpyjg3;
	}

	public String getDbjnsjpyjg4() {
		return dbjnsjpyjg4;
	}

	public void setDbjnsjpyjg4(String dbjnsjpyjg4) {
		this.dbjnsjpyjg4 = dbjnsjpyjg4;
	}

	public String getDbjnsjpyjg5() {
		return dbjnsjpyjg5;
	}

	public void setDbjnsjpyjg5(String dbjnsjpyjg5) {
		this.dbjnsjpyjg5 = dbjnsjpyjg5;
	}

	public String getJnsjmspyjg2() {
		return jnsjmspyjg2;
	}

	public void setJnsjmspyjg2(String jnsjmspyjg2) {
		this.jnsjmspyjg2 = jnsjmspyjg2;
	}

	public String getJnsjmspyjg3() {
		return jnsjmspyjg3;
	}

	public void setJnsjmspyjg3(String jnsjmspyjg3) {
		this.jnsjmspyjg3 = jnsjmspyjg3;
	}

	public String getJnsjmspyjg4() {
		return jnsjmspyjg4;
	}

	public void setJnsjmspyjg4(String jnsjmspyjg4) {
		this.jnsjmspyjg4 = jnsjmspyjg4;
	}

	public String getJnsjmspyjg5() {
		return jnsjmspyjg5;
	}

	public void setJnsjmspyjg5(String jnsjmspyjg5) {
		this.jnsjmspyjg5 = jnsjmspyjg5;
	}

	public String getJnsjjcsj() {
		return jnsjjcsj;
	}

	public void setJnsjjcsj(String jnsjjcsj) {
		this.jnsjjcsj = jnsjjcsj;
	}

	public String getGdhjg() {
		return gdhjg;
	}

	public void setGdhjg(String gdhjg) {
		this.gdhjg = gdhjg;
	}

	public String getToxinjg() {
		return toxinjg;
	}

	public void setToxinjg(String toxinjg) {
		this.toxinjg = toxinjg;
	}

	public String getDbjnsjpyjg() {
		return dbjnsjpyjg;
	}

	public void setDbjnsjpyjg(String dbjnsjpyjg) {
		this.dbjnsjpyjg = dbjnsjpyjg;
	}

	public String getJnsjmspyjg() {
		return jnsjmspyjg;
	}

	public void setJnsjmspyjg(String jnsjmspyjg) {
		this.jnsjmspyjg = jnsjmspyjg;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getYwzlx() {
		return ywzlx;
	}

	public void setYwzlx(String ywzlx) {
		this.ywzlx = ywzlx;
	}

	public String getYw() {
		return yw;
	}

	public void setYw(String yw) {
		this.yw = yw;
	}

	public String getJl() {
		return jl;
	}

	public void setJl(String jl) {
		this.jl = jl;
	}

	public String getKsyyrq() {
		return ksyyrq;
	}

	public void setKsyyrq(String ksyyrq) {
		this.ksyyrq = ksyyrq;
	}

	public String getTyrq() {
		return tyrq;
	}

	public void setTyrq(String tyrq) {
		this.tyrq = tyrq;
	}

	public String getQtywmc() {
		return qtywmc;
	}

	public void setQtywmc(String qtywmc) {
		this.qtywmc = qtywmc;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getJnsjjcjg_json() {
		return jnsjjcjg_json;
	}

	public void setJnsjjcjg_json(String jnsjjcjg_json) {
		this.jnsjjcjg_json = jnsjjcjg_json;
	}

	public String getMlpnmg() {
		return mlpnmg;
	}

	public void setMlpnmg(String mlpnmg) {
		this.mlpnmg = mlpnmg;
	}

	public String getWgmsmg() {
		return wgmsmg;
	}

	public void setWgmsmg(String wgmsmg) {
		this.wgmsmg = wgmsmg;
	}

	public String getLnzamg() {
		return lnzamg;
	}

	public void setLnzamg(String lnzamg) {
		this.lnzamg = lnzamg;
	}

	public String getJxzmg() {
		return jxzmg;
	}

	public void setJxzmg(String jxzmg) {
		this.jxzmg = jxzmg;
	}

	public String getLfxmmg() {
		return lfxmmg;
	}

	public void setLfxmmg(String lfxmmg) {
		this.lfxmmg = lfxmmg;
	}

	public String getKlmsmg() {
		return klmsmg;
	}

	public void setKlmsmg(String klmsmg) {
		this.klmsmg = klmsmg;
	}

	public String getJzlxmc() {
		return jzlxmc;
	}

	public void setJzlxmc(String jzlxmc) {
		this.jzlxmc = jzlxmc;
	}

	public String getXbmc() {
		return xbmc;
	}

	public void setXbmc(String xbmc) {
		this.xbmc = xbmc;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXb() {
		return xb;
	}

	public void setXb(String xb) {
		this.xb = xb;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getCopd() {
		return copd;
	}

	public void setCopd(String copd) {
		this.copd = copd;
	}

	public String getXyxtexzl() {
		return xyxtexzl;
	}

	public void setXyxtexzl(String xyxtexzl) {
		this.xyxtexzl = xyxtexzl;
	}

	public String getStqgyz() {
		return stqgyz;
	}

	public void setStqgyz(String stqgyz) {
		this.stqgyz = stqgyz;
	}

	public String getCqjsjszl() {
		return cqjsjszl;
	}

	public void setCqjsjszl(String cqjsjszl) {
		this.cqjsjszl = cqjsjszl;
	}

	public String getZsmyjb() {
		return zsmyjb;
	}

	public void setZsmyjb(String zsmyjb) {
		this.zsmyjb = zsmyjb;
	}

	public String getTnb() {
		return tnb;
	}

	public void setTnb(String tnb) {
		this.tnb = tnb;
	}

	public String getZlxjb() {
		return zlxjb;
	}

	public void setZlxjb(String zlxjb) {
		this.zlxjb = zlxjb;
	}

	public String getGxb() {
		return gxb;
	}

	public void setGxb(String gxb) {
		this.gxb = gxb;
	}

	public String getNxgjb() {
		return nxgjb;
	}

	public void setNxgjb(String nxgjb) {
		this.nxgjb = nxgjb;
	}

	public String getMxsb() {
		return mxsb;
	}

	public void setMxsb(String mxsb) {
		this.mxsb = mxsb;
	}

	public String getYzxcb() {
		return yzxcb;
	}

	public void setYzxcb(String yzxcb) {
		this.yzxcb = yzxcb;
	}

	public String getCyzd() {
		return cyzd;
	}

	public void setCyzd(String cyzd) {
		this.cyzd = cyzd;
	}

	public String getSgyqsfyzys() {
		return sgyqsfyzys;
	}

	public void setSgyqsfyzys(String sgyqsfyzys) {
		this.sgyqsfyzys = sgyqsfyzys;
	}

	public String getRysj() {
		return rysj;
	}

	public void setRysj(String rysj) {
		this.rysj = rysj;
	}

	public String getCysj() {
		return cysj;
	}

	public void setCysj(String cysj) {
		this.cysj = cysj;
	}

	public String getZyhf() {
		return zyhf;
	}

	public void setZyhf(String zyhf) {
		this.zyhf = zyhf;
	}
}
