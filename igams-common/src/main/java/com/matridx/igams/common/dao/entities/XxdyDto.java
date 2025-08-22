package com.matridx.igams.common.dao.entities;

import org.apache.ibatis.type.Alias;

import java.util.List;

@Alias(value="XxdyDto")
public class XxdyDto extends XxdyModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//信息对应类型名称
	private String dylxmc;
	private String id;
	//信息对应参数代码（基础数据的参数代码）
	private String dylxcsdm;
    //信息对应类型cskz1（cskz1，主要用来区分JS\RE\JCSJ三种情况）
	private String cskz1;	
	//信息对应类型cskz2（cskz2存jclb，主要处理cskz1是JCSJ，cskz2是jclb的情况）
	private String cskz2;
	private String cskz;
	private String SqlParam; 	//导出关联标记位//所选择的字段
	private String dyxx_input;
	private String dyzxx_input;
	private List<JcsjDto> jcsjDtos;

	private String[]dylxs;//对应类型s

	private String[]dyxxs;//对应信息s

	private String dyxxid;
	private String beginTime;
	private String endTime;
	private String flag;
	/**
	 * 先声后缀标记
	 */
	private String xshz;

	private List<String> kzcs1s;

	private String jcdwmc;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDyxxid() {
		return dyxxid;
	}

	public void setDyxxid(String dyxxid) {
		this.dyxxid = dyxxid;
	}

	public List<JcsjDto> getJcsjDtos() {
		return jcsjDtos;
	}

	public void setJcsjDtos(List<JcsjDto> jcsjDtos) {
		this.jcsjDtos = jcsjDtos;
	}

	public String getDyxx_input() {
		return dyxx_input;
	}

	public void setDyxx_input(String dyxx_input) {
		this.dyxx_input = dyxx_input;
	}

	private String jcxmcskz3;//检测项目参数扩展3
	private List<String> dydms;//对应代码s
	private List<String> yxxs;//原信息s

	public List<String> getYxxs() {
		return yxxs;
	}

	public void setYxxs(List<String> yxxs) {
		this.yxxs = yxxs;
	}

	public List<String> getDydms() {
		return dydms;
	}

	public void setDydms(List<String> dydms) {
		this.dydms = dydms;
	}

	public String getJcxmcskz3() {
		return jcxmcskz3;
	}

	public void setJcxmcskz3(String jcxmcskz3) {
		this.jcxmcskz3 = jcxmcskz3;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSqlParam() {
		return SqlParam;
	}

	public void setSqlParam(String sqlParam) {
		SqlParam = sqlParam;
	}

	public String getCskz() {
		return cskz;
	}

	public void setCskz(String cskz) {
		this.cskz = cskz;
	}

	public String getCskz2() {
		return cskz2;
	}
	public void setCskz2(String cskz2) {
		this.cskz2 = cskz2;
	}
	public String getCskz1() {
		return cskz1;
	}
	public void setCskz1(String cskz1) {
		this.cskz1 = cskz1;
	}
	public String getDylxcsdm() {
		return dylxcsdm;
	}
	public void setDylxcsdm(String dylxcsdm) {
		this.dylxcsdm = dylxcsdm;
	}
	public String getDylxmc() {
		return dylxmc;
	}
	public void setDylxmc(String dylxmc) {
		this.dylxmc = dylxmc;
	}

	public String[] getDylxs() {
		return dylxs;
	}

	public void setDylxs(String[] dylxs) {
		this.dylxs = dylxs;
		for (int i = 0; i < dylxs.length; i++){
			this.dylxs[i]=this.dylxs[i].replace("'","");
		}
	}

	public String[] getDyxxs() {
		return dyxxs;
	}

	public void setDyxxs(String[] dyxxs) {
		this.dyxxs = dyxxs;
		for (int i = 0; i < dyxxs.length; i++){
			this.dyxxs[i]=this.dyxxs[i].replace("'","");
		}
	}

    public String getDyzxx_input() {
        return dyzxx_input;
    }

    public void setDyzxx_input(String dyzxx_input) {
        this.dyzxx_input = dyzxx_input;
    }

	public String getXshz() {
		return xshz;
	}

	public void setXshz(String xshz) {
		this.xshz = xshz;
	}
	
	public List<String> getKzcs1s() {
		return kzcs1s;
	}

	public void setKzcs1s(List<String> kzcs1s) {
		this.kzcs1s = kzcs1s;
	}

	public String getJcdwmc() {
		return jcdwmc;
	}

	public void setJcdwmc(String jcdwmc) {
		this.jcdwmc = jcdwmc;
	}
}
