package com.matridx.web.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 登出控制清空AccessToken
 * 
 * LogoutSuccessHandler 在 LogoutFilter成功执行之后被调用，来重定向或者转发到合适的目的地上，注意这个接口和 LogoutHandler 几乎一样，但是可以抛出异常
 * @author linwu
 *
 */
@Component
public class IgamsLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
implements LogoutSuccessHandler{
	
	//private static final String BEARER_AUTHENTICATION = "Bearer ";
	//private static final String HEADER_AUTHORIZATION = "authorization";
	
	@Autowired
	private TokenStore tokenStore;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		String token = request.getParameter("access_token");
		
		if(token !=null){
			OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token.split(" ")[0]);
			
			if(oAuth2AccessToken !=null){
				tokenStore.removeAccessToken(oAuth2AccessToken);
			}
		}
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect("/");//实现自定义重定向
	}
	
}
