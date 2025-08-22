package com.matridx.web.config.security;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.web.dao.entities.XtyhDto;
import com.matridx.web.service.svcinterface.IXtyhService;


/**
 * NpriClientCredentialsTokenEndpointFilter 验证通过后会执行 /oauth/token,从而调用各种扩展的 NpriAuthentication
 * 比如短信验证，手机验证等
 * 
 * 但也可以把这种验证放到NpriDaoAuthenticationProvider 里做
 * @author linwu
 *
 */
public class MatridxTokenGranter extends AbstractTokenGranter{
	
	private static final String GRANT_TYPE = "matridx";
	public final String USERNAME = "client_id";
	public final String PASSWORD = "client_secret";
	protected OAuth2RequestFactory requestFactory;
	
	public MatridxTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
		this(tokenServices, clientDetailsService, requestFactory, MatridxTokenGranter.GRANT_TYPE);
	}

	public MatridxTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
		this.requestFactory = requestFactory;
	}

	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
		
		//XtyhDto t_xtyhDto = checkCode(parameters);
		
		String username = (String)parameters.get(USERNAME);
		String password = (String)parameters.get(PASSWORD);
		parameters.remove(PASSWORD);
		if(StringUtil.isNotBlank(password))
		{
			Base64 base64 = new Base64();
			try {
				password = new String(base64.decode(password), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*
		OAuth2Request storedOAuth2Request = this.requestFactory.createOAuth2Request(client, tokenRequest);
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(customUser, null, customUser.getAuthorities());
        authentication.setDetails(customUser);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(storedOAuth2Request, authentication);
        return oAuth2Authentication;
        */
        
		try {

			IXtyhService xtyhService = (IXtyhService)ServiceFactory.getService("xtyhServiceImpl");//获取方法所在class
			DataSource postsqlDataSource = (DataSource)ServiceFactory.getService("postsqlDataSource");//获取方法所在class
			XtyhDto t_xtyhDto = xtyhService.getDtoByName(username);
			//用户不存在的情况，抛出异常
			if(t_xtyhDto == null){
				throw new InvalidGrantException("用户名或者密码错误，请重新输入。");
			}
			JdbcClientDetailsService jdbcClientDetailsService= new JdbcClientDetailsService(postsqlDataSource);
	        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(username);

			BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
			if (!bpe.matches(password, clientDetails.getClientSecret())) {
				loginFail(xtyhService,t_xtyhDto);
				throw new InvalidGrantException("用户名或者密码错误，请重新输入。");
			}else {
				//登录成功，更新错误次数
				loginSucess(xtyhService,t_xtyhDto);
			}
			
			//userAuth = this.authenticationManager.authenticate(userAuth);
			Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password,client.getAuthorities());
			((AbstractAuthenticationToken)userAuth).setDetails(parameters);
			OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
			
			return new OAuth2Authentication(storedOAuth2Request, userAuth);
			
		} catch (AccountStatusException var8) {
			throw new InvalidGrantException(var8.getMessage());
		} catch (BadCredentialsException var9) {
			throw new InvalidGrantException(var9.getMessage());
		}
	}
	
	/**
	 * 密码错误时更新用户的错误次数，并进行提示
	 * @param xtyhDto
	 * @return
	 */
	private boolean loginFail(IXtyhService xtyhService,XtyhDto xtyhDto) {

		//最大限制数
		int maxLimit = 5;
		String errString = null;

        //在当日判断错误次数（若错误次数数大于3，则报提示）
		int currentInt = 0;
		if(StringUtil.isNotBlank(xtyhDto.getCwcs()))
		{
			currentInt = Integer.parseInt(xtyhDto.getCwcs());
		}
    	
  		if (maxLimit > currentInt + 1) {
  			if ((maxLimit - currentInt - 1) <= 2) {
  				String num = String.valueOf(maxLimit - currentInt - 1);
  				errString = "请注意，您还有"+ num + "次机会尝试，之后账户将被锁定。";
  			}
  		}else {
  			xtyhDto.setSfsd("1");
  			errString = "您已经超过最高限制！";
  		}
  		xtyhDto.setXgry(xtyhDto.getYhid());
  		xtyhDto.setCwcs(String.valueOf(currentInt + 1));
		// TODO: handle exception
    	xtyhService.updateLoginInfo(xtyhDto);
    	
    	if(StringUtil.isNotBlank(errString)) {
    		throw new InvalidGrantException(errString);
    	}
  		return true;
	}
	
	private boolean loginSucess(IXtyhService xtyhService,XtyhDto xtyhDto) {
        //在当日判断错误次数（若错误次数数大于3，则报提示）
		xtyhDto.setCwcs("0");
		xtyhDto.setXgry(xtyhDto.getYhid());
		// TODO: handle exception
    	xtyhService.updateLoginInfo(xtyhDto);
    	
  		return true;
	}
}
