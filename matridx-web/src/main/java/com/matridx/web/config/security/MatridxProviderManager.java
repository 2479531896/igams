package com.matridx.web.config.security;

import java.util.Arrays;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

/**
 * 2021-05-03  -- 2
 * /oauth/token
 * 这个如果配置支持allowFormAuthenticationForClients的，且url中有client_id和client_secret的会走ClientCredentialsTokenEndpointFilter来保护
 * 如果没有支持allowFormAuthenticationForClients或者有支持但是url中没有client_id和client_secret的，走basic认证保护
 * 
 * 而ClientCredentialsTokenEndpointFilter 默认只是创建了DaoAuthenticationProvider，里面对用户名密码进行了验证，无法进行自定义，所以需要扩展
 * 
 * 默认的ProviderManager 只是创建了DaoAuthenticationProvider，无法对里面的密码判断逻辑进行修改，比如当密码不对时，更新错误次数等，
 * 所以在 NpriClientCredentialsTokenEndpointFilter 里创建 NpriProviderManager
 * @author linwu
 *
 */
public class MatridxProviderManager extends ProviderManager{
	
	public MatridxProviderManager(AuthenticationProvider... providers) {
		super(Arrays.asList(providers), null);
	}
}
