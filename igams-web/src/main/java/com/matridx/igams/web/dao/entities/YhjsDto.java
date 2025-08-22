package com.matridx.igams.web.dao.entities;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Alias(value="YhjsDto")
public class YhjsDto extends YhjsModel implements GrantedAuthority{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//角色名称
	private String jsmc;
	//角色代码
	private String jsdm;
	//父角色id
	private String fjsid;
	//首页类型
	private String sylx;
	//用户名
	private String yhm;
	//真实姓名
	private String zsxm;
	//单位限定标记
	private String dwxdbj;
	//审批岗位成员
	private String spgwmc;
	//机构ID
	private String jgid;
	//机构名称
	private String jgmc;
	//仓库权限
	private String ckqx;
	//首页类型名称
	private String sylxmc;
	//首页类型路径
	private String sylxlj;
	//首页类型代码
	private String sylxdm;

	private List<YhjgqxDto> dtos;

	public List<YhjgqxDto> getDtos() {
		return dtos;
	}

	public void setDtos(List<YhjgqxDto> dtos) {
		this.dtos = dtos;
	}

	public String getSylxdm() {
		return sylxdm;
	}

	public void setSylxdm(String sylxdm) {
		this.sylxdm = sylxdm;
	}

	public String getSylxmc() {
		return sylxmc;
	}

	public void setSylxmc(String sylxmc) {
		this.sylxmc = sylxmc;
	}

	public String getSylxlj() {
		return sylxlj;
	}

	public void setSylxlj(String sylxlj) {
		this.sylxlj = sylxlj;
	}

	public String getCkqx() {
		return ckqx;
	}

	public void setCkqx(String ckqx) {
		this.ckqx = ckqx;
	}

	public String getJgmc()
	{
		return jgmc;
	}

	public void setJgmc(String jgmc)
	{
		this.jgmc = jgmc;
	}

	public String getSpgwmc()
	{
		return spgwmc;
	}

	public void setSpgwmc(String spgwmc)
	{
		this.spgwmc = spgwmc;
	}

	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return jsid;
	}

	public String getJsmc() {
		return jsmc;
	}

	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}

	public String getFjsid() {
		return fjsid;
	}

	public void setFjsid(String fjsid) {
		this.fjsid = fjsid;
	}

	public String getSylx() {
		return sylx;
	}

	public void setSylx(String sylx) {
		this.sylx = sylx;
	}

	public String getJsdm() {
		return jsdm;
	}

	public void setJsdm(String jsdm) {
		this.jsdm = jsdm;
	}

	public String getZsxm() {
		return zsxm;
	}

	public void setZsxm(String zsxm) {
		this.zsxm = zsxm;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getDwxdbj() {
		return dwxdbj;
	}

	public void setDwxdbj(String dwxdbj) {
		this.dwxdbj = dwxdbj;
	}

	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

}
