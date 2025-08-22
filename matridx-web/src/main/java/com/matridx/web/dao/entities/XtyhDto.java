package com.matridx.web.dao.entities;


import java.util.Collection;
import java.util.List;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.matridx.igams.common.dao.entities.QxModel;

@Alias(value="XtyhDto")
public class XtyhDto extends XtyhModel implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户角色列表
	private List<YhjsDto> yhjsDtos = null;
	//当前角色名称
	private String dqjsmc;
	//当前角色代码
	private String dqjsdm;
	//角色资源操作表
	private List<QxModel> qxModels;
	//角色ID列表
	private List<String> jsids;
	//原密码
	private String ymm;
	//原用户名
	private String yyhm;
	//角色ID
	private String jsid;
	//机构ID
	private String jgid;
	//机构名称
	private String jgmc;
	//模糊查询字段
	private String entire;
	//docker 操作标识
	private String czbs;
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

	public String getCzbs() {
		return czbs;
	}

	public void setCzbs(String czbs) {
		this.czbs = czbs;
	}

	public String getEntire() {
		return entire;
	}

	public void setEntire(String entire) {
		this.entire = entire;
	}

	public String getJgmc() {
		return jgmc;
	}

	public void setJgmc(String jgmc) {
		this.jgmc = jgmc;
	}

	private Collection<? extends GrantedAuthority> authorities;

	public List<QxModel> getQxModels() {
		return qxModels;
	}

	public void setQxModels(List<QxModel> qxModels) {
		this.qxModels = qxModels;
	}

	public List<YhjsDto> getYhjsDtos() {
		return yhjsDtos;
	}

	public void setYhjsDtos(List<YhjsDto> yhjsDtos) {
		this.yhjsDtos = yhjsDtos;
	}
	
	public String getJgid() {
		return jgid;
	}

	public void setJgid(String jgid) {
		this.jgid = jgid;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		// TODO Auto-generated method stub
		this.authorities = authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return mm;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return yhm;
	}

	/**
	 * 用户是否不过期  不过期为true
	 */
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 账户是否未锁定  未锁定为true
	 */
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !"1".equals(sfsd);
	}

	/**
	 * 证书是否不过期   不过期为true
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 是否可用  可用为true
	 */
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getDqjsmc() {
		return dqjsmc;
	}

	public void setDqjsmc(String dqjsmc) {
		this.dqjsmc = dqjsmc;
	}

	public String getDqjsdm() {
		return dqjsdm;
	}

	public void setDqjsdm(String dqjsdm) {
		this.dqjsdm = dqjsdm;
	}

	public List<String> getJsids() {
		return jsids;
	}

	public void setJsids(List<String> jsids) {
		this.jsids = jsids;
	}

	public String getYmm() {
		return ymm;
	}

	public void setYmm(String ymm) {
		this.ymm = ymm;
	}

	public String getYyhm() {
		return yyhm;
	}

	public void setYyhm(String yyhm) {
		this.yyhm = yyhm;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}
}
