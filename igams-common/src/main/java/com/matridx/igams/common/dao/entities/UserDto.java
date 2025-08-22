package com.matridx.igams.common.dao.entities;


import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Alias(value="UserDto")
public class UserDto extends UserModel implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//用户查询列表
	private List<LscxqxDto> lscxqxDto = null;
	//查询ID列表
	private List<String> cxids;
	//查询ID
	private String cxid;
	//查询ID列表
	private List<String> rwids;
	//查询ID
	private String rwid;
	//token_id
	private String token_id;
	//复数id
	private List<String> cids;
	//用户id+接收时间的加密字段
	private String newSign;
	//角色id
	private String jsid;
	//角色名称
	private String jsmc;
	//外部程序id
	private String wbcxid;
	//类型
	private String lx;
	//是否同步
	private String sftb;
	private String appid;
	private String secret;
	//外部程序名称
	private String wbcxmc;
	//小程序跳转路径
	private String jumpdingtalkurl;
	//部门主管
	private String bmzgs;
	//邮箱
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBmzgs() {
		return bmzgs;
	}

	public void setBmzgs(String bmzgs) {
		this.bmzgs = bmzgs;
	}

	public String getJumpdingtalkurl() {
		return jumpdingtalkurl;
	}

	public void setJumpdingtalkurl(String jumpdingtalkurl) {
		this.jumpdingtalkurl = jumpdingtalkurl;
	}

	public String getWbcxmc() {
		return wbcxmc;
	}

	public void setWbcxmc(String wbcxmc) {
		this.wbcxmc = wbcxmc;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getSftb() {
		return sftb;
	}

	public void setSftb(String sftb) {
		this.sftb = sftb;
	}

	public String getWbcxid() {
		return wbcxid;
	}

	public void setWbcxid(String wbcxid) {
		this.wbcxid = wbcxid;
	}

	public String getJsid() {
		return jsid;
	}

	public void setJsid(String jsid) {
		this.jsid = jsid;
	}

	public String getJsmc() {
		return jsmc;
	}

	public void setJsmc(String jsmc) {
		this.jsmc = jsmc;
	}

	public String getNewSign() {
		return newSign;
	}
	public void setNewSign(String newSign) {
		this.newSign = newSign;
	}
	public List<String> getCids() {
		return cids;
	}
	public void setCids(String cids) {
		List<String> list;
		String[] str = cids.split(",");
		list = Arrays.asList(str);
		this.cids = list;
	}
	public void setCids(List<String> cids) {
		if( cids!=null&&!cids.isEmpty()){
            cids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.cids = cids;
	}
	public String getToken_id() {
		return token_id;
	}

	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}

	public List<LscxqxDto> getLscxqxDto() {
		return lscxqxDto;
	}

	public void setLscxqxDto(List<LscxqxDto> lscxqxDto) {
		this.lscxqxDto = lscxqxDto;
	}

	public List<String> getCxids() {
		return cxids;
	}

	public void setCxids(List<String> cxids) {
		this.cxids = cxids;
	}

	public String getCxid() {
		return cxid;
	}

	public void setCxid(String cxid) {
		this.cxid = cxid;
	}

	public List<String> getRwids() {
		return rwids;
	}
	public void setRwids(String rwids) {
		List<String> list;
		String[] str = rwids.split(",");
		list = Arrays.asList(str);
		this.rwids = list;
	}
	public void setRwids(List<String> rwids) {
		if( rwids!=null&&!rwids.isEmpty()){
			rwids.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
		}
		this.rwids = rwids;
	}
	public String getRwid() {
		return rwid;
	}

	public void setRwid(String rwid) {
		this.rwid = rwid;
	}

	public List<String> getYhms() {
		return yhms;
	}

	public void setYhms(List<String> yhms) {
		this.yhms = yhms;
	}

	private List<String> yhms;
	private Collection<? extends GrantedAuthority> authorities;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String getPassword() {
		return mm;
	}

	@Override
	public String getUsername() {
		return yhm;
	}

	/**
	 * 用户是否不过期  不过期为true
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 账户是否未锁定  未锁定为true
	 */
	@Override
	public boolean isAccountNonLocked() {
		return sfsd==null || !sfsd.equals("1");
	}

	/**
	 * 证书是否不过期   不过期为true
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 是否可用  可用为true
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
	

}
