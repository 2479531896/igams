package com.matridx.igams.web.config.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.security.IgamsGrantedAuthority;

public class MatridxTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities = (List<GrantedAuthority>)authentication.getAuthorities();
		
		IgamsGrantedAuthority authority = (IgamsGrantedAuthority)authorities.get(0);
		User user = (User)authority.getYhxx();
		//User user = (User) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("jsdm", user.getDqjsdm());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
	}

}
