package com.matridx.web.dao.entities;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;

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
	//机构ID
	private String jgid;
	//是否可操作左键
	private String sfzj;
	private String sylxdm;//首页类型代码
	private String sylxlj;//首页类型路径

	public String getSylxdm() {
		return sylxdm;
	}

	public void setSylxdm(String sylxdm) {
		this.sylxdm = sylxdm;
	}

	public String getSylxlj() {
		return sylxlj;
	}

	public void setSylxlj(String sylxlj) {
		this.sylxlj = sylxlj;
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

	public String getSfzj() {
		return sfzj;
	}

	public void setSfzj(String sfzj) {
		this.sfzj = sfzj;
	}
}
