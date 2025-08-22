package com.matridx.server.wechat.dao.entities;

import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias(value="SjyzDto")
public class SjyzDto extends SjyzModel{
	//标本编号
	private String ybbh;
	//内部编码
	private String nbbm;
	//验证内部编码
	private String yznbbm;
	//验证默认内部编码
	private String yzmrnbbm;
	//患者姓名
	private String hzxm;
	//年龄
	private String nl;
	//标本类型名称
	private String yblxmc;
	//代表
	private String db;
	//验证类别名称
	private String yzlbmc;
	//录入人员名称
	private String lrrymc;
	//验证类别[多]
	private String[] yzlbs;
	//检测项目
	private String jcxm;
	//标本类型[多]
	private String[] yblxs;
	//附件IDS
	private List<String> fjids;
	//验证结果名称
	private String yzjgmc;
	//业务类型
	private String ywlx;
	//检测单位
	private String jcdw;
	//区分代码	
	private String qfdm;
	//区分名称	
	private String qfmc;
	//客户通知名称
	private String khtzmc;
	//录入时间格式化
	private String lrsj_format;
	//是否默认[基础数据]
	private String sfmr;
	//验证代码
	private String yzdm;
	//当前角色
	private String jsid;
	//导出字段[导出]
	private String sqlparam;
	//标本类型[导出]
	private String yblx_flg;
	//验证类别[导出]
	private String yzlb_flg;
	//区分[导出]
	private String qf_flg;
	//验证结果[导出]
	private String yzjg_flg;
	//客户通知[导出]
	private String khtz_flg;
	//录入人员[导出]
	private String lrry_flg;
	private String flag;
	//是否通知
	private String sftz;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getSftz() {
		return sftz;
	}

	public void setSftz(String sftz) {
		this.sftz = sftz;
	}

	public String getYzjgmc() {
		return yzjgmc;
	}

	public void setYzjgmc(String yzjgmc) {
		this.yzjgmc = yzjgmc;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public String getYbbh() {
		return ybbh;
	}

	public void setYbbh(String ybbh) {
		this.ybbh = ybbh;
	}

	public String getNbbm() {
		return nbbm;
	}

	public void setNbbm(String nbbm) {
		this.nbbm = nbbm;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public String getYblxmc() {
		return yblxmc;
	}

	public void setYblxmc(String yblxmc) {
		this.yblxmc = yblxmc;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getYzlbmc() {
		return yzlbmc;
	}

	public void setYzlbmc(String yzlbmc) {
		this.yzlbmc = yzlbmc;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String[] getYzlbs() {
		return yzlbs;
	}

	public void setYzlbs(String[] yzlbs) {
		this.yzlbs = yzlbs;
		for (int i = 0; i < yzlbs.length; i++){
			this.yzlbs[i] = this.yzlbs[i].replace("'", "");
		}
	}

	public String getJcxm() {
		return jcxm;
	}

	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}

	public String[] getYblxs() {
		return yblxs;
	}

	public void setYblxs(String[] yblxs) {
		this.yblxs = yblxs;
		for (int i = 0; i < yblxs.length; i++){
			this.yblxs[i] = this.yblxs[i].replace("'", "");
		}
	}
	
	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getJcdw() {
		return jcdw;
	}

	public void setJcdw(String jcdw) {
		this.jcdw = jcdw;
	}

	public String getQfmc() {
		return qfmc;
	}

	public void setQfmc(String qfmc) {
		this.qfmc = qfmc;
	}

	public String getKhtzmc() {
		return khtzmc;
	}

	public void setKhtzmc(String khtzmc) {
		this.khtzmc = khtzmc;
	}

	public String getLrsj_format() {
		return lrsj_format;
	}

	public void setLrsj_format(String lrsj_format) {
		this.lrsj_format = lrsj_format;
	}

	public String getSfmr() {
		return sfmr;
	}

	public void setSfmr(String sfmr) {
		this.sfmr = sfmr;
	}


	public String getQfdm() {
		return qfdm;
	}

	public void setQfdm(String qfdm) {
		this.qfdm = qfdm;
	}


	public String getYznbbm() {
		return yznbbm;
	}

	public void setYznbbm(String yznbbm) {
		this.yznbbm = yznbbm;
	}


	public String getYzmrnbbm() {
		return yzmrnbbm;
	}

	public void setYzmrnbbm(String yzmrnbbm) {
		this.yzmrnbbm = yzmrnbbm;
	}

	public String getYzdm() {
		return yzdm;
	}

	public void setYzdm(String yzdm) {
		this.yzdm = yzdm;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getSqlparam() {
		return sqlparam;
	}

	public void setSqlparam(String sqlparam) {
		this.sqlparam = sqlparam;
	}

	public String getYblx_flg() {
		return yblx_flg;
	}

	public void setYblx_flg(String yblx_flg) {
		this.yblx_flg = yblx_flg;
	}

	public String getYzlb_flg() {
		return yzlb_flg;
	}

	public void setYzlb_flg(String yzlb_flg) {
		this.yzlb_flg = yzlb_flg;
	}

	public String getQf_flg() {
		return qf_flg;
	}

	public void setQf_flg(String qf_flg) {
		this.qf_flg = qf_flg;
	}

	public String getYzjg_flg() {
		return yzjg_flg;
	}

	public void setYzjg_flg(String yzjg_flg) {
		this.yzjg_flg = yzjg_flg;
	}

	public String getKhtz_flg() {
		return khtz_flg;
	}

	public void setKhtz_flg(String khtz_flg) {
		this.khtz_flg = khtz_flg;
	}

	public String getLrry_flg() {
		return lrry_flg;
	}

	public void setLrry_flg(String lrry_flg) {
		this.lrry_flg = lrry_flg;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
