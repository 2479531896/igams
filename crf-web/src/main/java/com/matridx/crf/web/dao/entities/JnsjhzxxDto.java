package com.matridx.crf.web.dao.entities;

import org.apache.ibatis.type.Alias;

@Alias(value="JnsjhzxxDto")
public class JnsjhzxxDto extends JnsjhzxxModel{
	private String [] jzlxs;
	private String [] xbs;
	private String [] blrzdws;
	private String [] kss;
	//艰难梭菌报告id
	private String jnsjbgid;
	//开始日期(出院时间)
	private String cysjstart;
	//结束日期(出院时间)
	private String cysjend;
	//开始日期(入院时间)
	private String rysjstart;
	//结束日期(入院时间)
	private String rysjend;
	//开始日期(填表时间)
	private String tbsjstart;
	//结束日期(填表时间)
	private String tbsjend;
	//病例入组单位
	private String blrzdw;
	//科室
	private String ks;
	//病例入组编号
	private String blrzbh;
	//ID号
	private String idh;
	//住院号
	private String zyh;
	//填表人姓名
	private String tbrxm;
	//填表时间
	private String tbsj;
	//就诊类型
	private String jzlx;
	private String jzlxmc;
	private String blrzdwmc;
	private String ksmc;
	//当前角色
	private String dqjs;

	public String getDqjs() {
		return dqjs;
	}

	public void setDqjs(String dqjs) {
		this.dqjs = dqjs;
	}

	public String getJnsjbgid() { return jnsjbgid; }
	public void setJnsjbgid(String jnsjbgid) { this.jnsjbgid = jnsjbgid; }

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

	public String[] getBlrzdws() {
		return blrzdws;
	}

	public void setBlrzdws(String[] blrzdws) {
		this.blrzdws = blrzdws;
		for (int i = 0; i < blrzdws.length; i++){
			this.blrzdws[i]=this.blrzdws[i].replace("'","");
		}
	}

	public String[] getKss() {
		return kss;
	}

	public void setKss(String[] kss) {
		this.kss = kss;
		for (int i = 0; i < kss.length; i++){
			this.kss[i]=this.kss[i].replace("'","");
		}
	}

	public String[] getJzlxs() {
		return jzlxs;
	}

	public void setJzlxs(String[] jzlxs) {
		this.jzlxs = jzlxs;
		for (int i = 0; i < jzlxs.length; i++){
			this.jzlxs[i]=this.jzlxs[i].replace("'","");
		}
	}

	public String[] getXbs() {
		return xbs;
	}

	public void setXbs(String[] xbs) {
		this.xbs = xbs;
		for (int i = 0; i < xbs.length; i++){
			this.xbs[i]=this.xbs[i].replace("'","");
		}
	}

	public String getCysjstart() {
		return cysjstart;
	}

	public void setCysjstart(String cysjstart) {
		this.cysjstart = cysjstart;
	}

	public String getCysjend() {
		return cysjend;
	}

	public void setCysjend(String cysjend) {
		this.cysjend = cysjend;
	}

	public String getRysjstart() {
		return rysjstart;
	}

	public void setRysjstart(String rysjstart) {
		this.rysjstart = rysjstart;
	}

	public String getRysjend() {
		return rysjend;
	}

	public void setRysjend(String rysjend) {
		this.rysjend = rysjend;
	}

	public String getTbsjstart() {
		return tbsjstart;
	}

	public void setTbsjstart(String tbsjstart) {
		this.tbsjstart = tbsjstart;
	}

	public String getTbsjend() {
		return tbsjend;
	}

	public void setTbsjend(String tbsjend) {
		this.tbsjend = tbsjend;
	}

	public String getBlrzdw() {
		return blrzdw;
	}

	public void setBlrzdw(String blrzdw) {
		this.blrzdw = blrzdw;
	}

	public String getKs() {
		return ks;
	}

	public void setKs(String ks) {
		this.ks = ks;
	}

	public String getBlrzbh() {
		return blrzbh;
	}

	public void setBlrzbh(String blrzbh) {
		this.blrzbh = blrzbh;
	}

	public String getIdh() {
		return idh;
	}

	public void setIdh(String idh) {
		this.idh = idh;
	}

	public String getZyh() {
		return zyh;
	}

	public void setZyh(String zyh) {
		this.zyh = zyh;
	}

	public String getTbrxm() {
		return tbrxm;
	}

	public void setTbrxm(String tbrxm) {
		this.tbrxm = tbrxm;
	}

	public String getTbsj() {
		return tbsj;
	}

	public void setTbsj(String tbsj) {
		this.tbsj = tbsj;
	}

	public String getJzlx() {
		return jzlx;
	}

	public void setJzlx(String jzlx) {
		this.jzlx = jzlx;
	}

	public String getJzlxmc() {
		return jzlxmc;
	}

	public void setJzlxmc(String jzlxmc) {
		this.jzlxmc = jzlxmc;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

}
