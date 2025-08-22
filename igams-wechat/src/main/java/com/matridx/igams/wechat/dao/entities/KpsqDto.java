package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.ShxxDto;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="KpsqDto")
public class KpsqDto extends KpsqModel{

	private List<ShxxDto> shxxDtos;//审核数据
	private List<String> fjids;//附件
	private String fpxzmc;//发票性质名称
	private String kpztmc;//开票主体名称
	private String kpdxmc;//开票对象名称
	private String kplxmc;//开票类型名称
	private String jgmc;//机构名称
	private String lrsjstart;
	private String lrsjend;
	private String kjrqstart;
	private String kjrqend;
	private String entire;
	private List<String> fpxzids;//发票性质
	private List<String> kpztids;//开票主体
	private List<String> kpdxids;
	private List<String> kplxids;
	//审批人角色ID
	private String sprjsid;
	//审批人角色名称
	private String sprjsmc;
	private String[] zts;//状态


	public String[] getZts() {
		return zts;
	}

	public void setZts(String[] zts) {
		this.zts = zts;
		for(int i=0;i<this.zts.length;i++)
		{
			this.zts[i] = this.zts[i].replace("'", "");
		}
	}

	public List<String> getKpztids() {
		return kpztids;
	}

	public void setKpztids(List<String> kpztids) {
		this.kpztids = kpztids;
	}

	public String getKpztmc() {
		return kpztmc;
	}

	public void setKpztmc(String kpztmc) {
		this.kpztmc = kpztmc;
	}

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

	public String getFpxzmc() {
		return fpxzmc;
	}

	public void setFpxzmc(String fpxzmc) {
		this.fpxzmc = fpxzmc;
	}

	public String getKpdxmc() {
		return kpdxmc;
	}

	public void setKpdxmc(String kpdxmc) {
		this.kpdxmc = kpdxmc;
	}

	public String getKplxmc() {
		return kplxmc;
	}

	public void setKplxmc(String kplxmc) {
		this.kplxmc = kplxmc;
	}

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	public String getLrsjstart() {
		return lrsjstart;
	}

	public void setLrsjstart(String lrsjstart) {
		this.lrsjstart = lrsjstart;
	}

	public String getLrsjend() {
		return lrsjend;
	}

	public void setLrsjend(String lrsjend) {
		this.lrsjend = lrsjend;
	}

	public String getKjrqstart() {
		return kjrqstart;
	}

	public void setKjrqstart(String kjrqstart) {
		this.kjrqstart = kjrqstart;
	}

	public String getKjrqend() {
		return kjrqend;
	}

	public void setKjrqend(String kjrqend) {
		this.kjrqend = kjrqend;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public List<String> getFpxzids() {
		return fpxzids;
	}

	public void setFpxzids(List<String> fpxzids) {
		this.fpxzids = fpxzids;
	}

	public List<String> getKpdxids() {
		return kpdxids;
	}

	public void setKpdxids(List<String> kpdxids) {
		this.kpdxids = kpdxids;
	}

	public List<String> getKplxids() {
		return kplxids;
	}

	public void setKplxids(List<String> kplxids) {
		this.kplxids = kplxids;
	}

	public List<String> getFjids() {
		return fjids;
	}

	public void setFjids(List<String> fjids) {
		this.fjids = fjids;
	}

	public List<ShxxDto> getShxxDtos() {
		return shxxDtos;
	}

	public void setShxxDtos(List<ShxxDto> shxxDtos) {
		this.shxxDtos = shxxDtos;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
