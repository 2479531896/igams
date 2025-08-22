package com.matridx.igams.web.dao.entities;

import java.util.List;

import com.matridx.igams.common.dao.entities.JsxtqxDto;
import org.apache.ibatis.type.Alias;

import com.matridx.igams.common.dao.entities.SpgwDto;

@Alias(value="XtjsDto")
public class XtjsDto extends XtjsModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户ID
	private String yhid;
	//父角色名称
	private String fjsmc;
	//父角色代码
	private String fjsdm;
	//首页类型名称
	private String sylxmc;
	//首页类型地址
	private String sylxdz;
	//审批岗位成员
	private List<SpgwDto> spgwDtos;
	//系统用户
	private List<XtyhDto> xtyhDtos;
	//角色个人权限集合
	private List<JsgnqxDto> jsgrqxDtos;
	//角色显示字段
	private List<String> zds;
	//分布式标记
	private String prefix;
	//角色检测单位
	private List<JsjcdwDto> jsjcdwDtos;
	//仓库分类
	private List<String> ckqxlxDtos;
	//角色系统权限
	private List<JsxtqxDto> jsxtqxDtos;
	//全部查询内容
	private String entire;
	//列表加载的条数
	private String count;
	//从第几条开始
	private String start;
	//访问标记
	private String fwbj;
	//区分
	private String qf;

	public String getQf() {
		return qf;
	}

	public void setQf(String qf) {
		this.qf = qf;
	}

	public List<JsxtqxDto> getJsxtqxDtos() {
		return jsxtqxDtos;
	}

	public void setJsxtqxDtos(List<JsxtqxDto> jsxtqxDtos) {
		this.jsxtqxDtos = jsxtqxDtos;
	}

	//机构ID(复数)
	private List<String> jgids;
	//角色机构ID(复数 防止重复)
	private List<String> jsjgids;

	public List<String> getCkqxlxDtos() {
		return ckqxlxDtos;
	}

	public void setCkqxlxDtos(List<String> ckqxlxDtos) {
		this.ckqxlxDtos = ckqxlxDtos;
	}

	public List<String> getJsjgids() {
		return jsjgids;
	}

	public void setJsjgids(List<String> jsjgids) {
		this.jsjgids = jsjgids;
	}

	public List<String> getJgids() {
		return jgids;
	}

	public void setJgids(List<String> jgids) {
		this.jgids = jgids;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public List<JsjcdwDto> getJsjcdwDtos()
	{
		return jsjcdwDtos;
	}

	public void setJsjcdwDtos(List<JsjcdwDto> jsjcdwDtos)
	{
		this.jsjcdwDtos = jsjcdwDtos;
	}

	public List<String> getZds()
	{
		return zds;
	}

	public void setZds(List<String> zds)
	{
		this.zds = zds;
	}

	public List<JsgnqxDto> getJsgrqxDtos()
	{
		return jsgrqxDtos;
	}

	public void setJsgrqxDtos(List<JsgnqxDto> jsgrqxDtos)
	{
		this.jsgrqxDtos = jsgrqxDtos;
	}

	public List<XtyhDto> getXtyhDtos()
	{
		return xtyhDtos;
	}

	public void setXtyhDtos(List<XtyhDto> xtyhDtos)
	{
		this.xtyhDtos = xtyhDtos;
	}

	public List<SpgwDto> getSpgwDtos()
	{
		return spgwDtos;
	}

	public void setSpgwDtos(List<SpgwDto> spgwDtos)
	{
		this.spgwDtos = spgwDtos;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public String getFjsmc() {
		return fjsmc;
	}

	public void setFjsmc(String fjsmc) {
		this.fjsmc = fjsmc;
	}

	public String getFjsdm() {
		return fjsdm;
	}

	public void setFjsdm(String fjsdm) {
		this.fjsdm = fjsdm;
	}

	public String getSylxmc() {
		return sylxmc;
	}

	public void setSylxmc(String sylxmc) {
		this.sylxmc = sylxmc;
	}

	public String getSylxdz() {
		return sylxdz;
	}

	public void setSylxdz(String sylxdz) {
		this.sylxdz = sylxdz;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getFwbj() {
		return fwbj;
	}

	public void setFwbj(String fwbj) {
		this.fwbj = fwbj;
	}
}
