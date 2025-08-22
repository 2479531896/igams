package com.matridx.igams.warehouse.dao.entities;

import com.matridx.igams.production.dao.entities.QgmxDto;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="GysxxDto")
public class GysxxDto extends GysxxModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//供应商管理类别(高级筛选用)
	private String[] gfgllbs;
	//发展开始时间
	private String fzkssj;
	//发展结束时间
	private String fzjssj;
	//供应商管理类别名称
	private String gfgllbmc;
	//全部(查询用)
	private String entire;
	//省份名称
	private String sfmc;
	//省份扩展参数1
	private String sfKzcs1;
	//服务器
	private String server;
	private String mbywlx;//模板业务类型
	private String[] sfxss;//是否显示多
	List<QgmxDto> qgmxDtos;
	private String ywlx;//业务类型
	private String wjm;//文件名
	private String wjlj;//文件名
	private String storePath;//文件名
	private String[] zts;//是否停用
	private String tyrymc;//停用人员名称
	private String lrrymc;//录入人员名称
	private String disableFlag;//停用标记
	private String jxflg;

	public String getJxflg() {
		return jxflg;
	}

	public void setJxflg(String jxflg) {
		this.jxflg = jxflg;
	}

	public String getDisableFlag() {
		return disableFlag;
	}

	public void setDisableFlag(String disableFlag) {
		this.disableFlag = disableFlag;
	}

	public String getLrrymc() {
		return lrrymc;
	}

	public void setLrrymc(String lrrymc) {
		this.lrrymc = lrrymc;
	}

	public String getTyrymc() {
		return tyrymc;
	}

	public void setTyrymc(String tyrymc) {
		this.tyrymc = tyrymc;
	}

	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
	}

	public String getWjm() {
		return wjm;
	}

	public void setWjm(String wjm) {
		this.wjm = wjm;
	}

	public String getWjlj() {
		return wjlj;
	}

	public void setWjlj(String wjlj) {
		this.wjlj = wjlj;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public List<QgmxDto> getQgmxDtos() {
		return qgmxDtos;
	}

	public void setQgmxDtos(List<QgmxDto> qgmxDtos) {
		this.qgmxDtos = qgmxDtos;
	}

	public String[] getSfxss() {
		return sfxss;
	}

	public void setSfxss(String[] sfxss) {
		this.sfxss = sfxss;
	}

	public String getMbywlx() {
		return mbywlx;
	}

	public void setMbywlx(String mbywlx) {
		this.mbywlx = mbywlx;
	}

	//附件IDS
	private List<String> fjids;

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}
	//附件IDS
	private List<String> mbfjids;

	public List<String> getMbfjids() {
		return mbfjids;
	}

	public void setMbfjids(List<String> mbfjids) {
		this.mbfjids = mbfjids;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getSfKzcs1() {
		return sfKzcs1;
	}

	public void setSfKzcs1(String sfKzcs1) {
		this.sfKzcs1 = sfKzcs1;
	}

	public String getSfmc() {
		return sfmc;
	}

	public void setSfmc(String sfmc) {
		this.sfmc = sfmc;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getGfgllbmc() {
		return gfgllbmc;
	}

	public void setGfgllbmc(String gfgllbmc) {
		this.gfgllbmc = gfgllbmc;
	}

	public String getFzkssj() {
		return fzkssj;
	}

	public void setFzkssj(String fzkssj) {
		this.fzkssj = fzkssj;
	}

	public String getFzjssj() {
		return fzjssj;
	}

	public void setFzjssj(String fzjssj) {
		this.fzjssj = fzjssj;
	}

	public String[] getGfgllbs() {
		return gfgllbs;
	}

	public void setGfgllbs(String[] gfgllbs) {
		this.gfgllbs = gfgllbs;
	}
	
	
}
