package com.matridx.igams.web.dao.entities;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.type.Alias;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

@Alias(value="ClientDto")
public class ClientDto implements ClientDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String yhm;		//用户名
	private String yhid;	//用户ID
	private Set<String> resourceIds; 			//资源ID
	private String clientSecret;  				//用户密码
	private Set<String> scope;					//范围
	private Set<String> authorizedGrantTypes; 	//类别
	private Map<String, Object> additionalInformation;
	private Collection<GrantedAuthority> authorities = null;
	private Integer accessTokenValiditySeconds;
	private Integer refreshTokenValiditySeconds;
	private Set<String> registeredRedirectUri;
	
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setScope(Set<String> scope) {
		this.scope = scope;
	}

	public String getYhm() {
		return yhm;
	}

	public void setYhm(String yhm) {
		this.yhm = yhm;
	}

	public String getYhid() {
		return yhid;
	}

	public void setYhid(String yhid) {
		this.yhid = yhid;
	}

	public void setResourceIds(Set<String> resourceIds) {
		this.resourceIds = resourceIds;
	}

	@Override
	public String getClientId() {
		// TODO Auto-generated method stub
		return yhm;
	}

	@Override
	public Set<String> getResourceIds() {
		// TODO Auto-generated method stub
		return resourceIds;
	}

	@Override
	public boolean isSecretRequired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getClientSecret() {
		// TODO Auto-generated method stub
		return clientSecret;
	}

	@Override
	public boolean isScoped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> getScope() {
		// TODO Auto-generated method stub
		return scope;
	}

	@Override
	public Set<String> getAuthorizedGrantTypes() {
		// TODO Auto-generated method stub
		return authorizedGrantTypes;
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		// TODO Auto-generated method stub
		return registeredRedirectUri;
	}
	public void setRegisteredRedirectUri(Set<String> registeredRedirectUri) {
		// TODO Auto-generated method stub
		this.registeredRedirectUri = registeredRedirectUri;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	
	public void setAuthorities(Collection<GrantedAuthority> collection) {
		// TODO Auto-generated method stub
		this.authorities = collection;
	}

	@Override
	public Integer getAccessTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return accessTokenValiditySeconds;
	}
	
	public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
		// TODO Auto-generated method stub
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	@Override
	public Integer getRefreshTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return refreshTokenValiditySeconds;
	}
	public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
		// TODO Auto-generated method stub
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}

	@Override
	public boolean isAutoApprove(String scope) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> getAdditionalInformation() {
		// TODO Auto-generated method stub
		return additionalInformation;
	}

	public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
		this.authorizedGrantTypes = authorizedGrantTypes;
	}

	public void setAdditionalInformation(Map<String, Object> additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

}
