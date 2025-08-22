package com.matridx.web.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.web.service.svcinterface.IXtyhService;

/**
 * 2021-05-03  --1
 * 
 * /oauth/token
 * 这个如果配置支持allowFormAuthenticationForClients的，且url中有client_id和client_secret的会走ClientCredentialsTokenEndpointFilter来保护
 * 如果没有支持allowFormAuthenticationForClients或者有支持但是url中没有client_id和client_secret的，走basic认证保护
 * 
 * 而ClientCredentialsTokenEndpointFilter 默认只是创建了DaoAuthenticationProvider，里面对用户名密码进行了验证，无法进行自定义，所以需要扩展
 * 
 * 在里面需要创建新的AuthenticationManager ,并把自定义的 密码判断处理 DaoAuthenticationProvider加载到类里
 * @author linwu
 *
 */
public class MatridxClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter{
	
	//private AuthorizationServerSecurityConfigurer configurer;
    private AuthenticationEntryPoint authenticationEntryPoint;

    public MatridxClientCredentialsTokenEndpointFilter(AuthorizationServerSecurityConfigurer configurer, String path) {
        //this.configurer = configurer;
        setFilterProcessesUrl(path);
    }

    @Override
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        // 把父类的干掉
        super.setAuthenticationEntryPoint(null);
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected AuthenticationManager getAuthenticationManager() {
    	//AuthenticationManager manager =  configurer.and().getSharedObject(AuthenticationManager.class);
    	//采用这个自定义类，主要是为了忽略 在 /oauth/token 之前做的原有的检查，Provider里现在不作检查
    	MatridxDaoAuthenticationProvider provider = new MatridxDaoAuthenticationProvider();
    	IXtyhService xtyhService = (IXtyhService)ServiceFactory.getService("xtyhServiceImpl");//获取方法所在class
    	provider.setUserDetailsService(xtyhService);
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	provider.setPasswordEncoder(passwordEncoder);
    	
    	//默认的ProviderManager 只是创建了DaoAuthenticationProvider,，还是会对密码进行检查，故创建一个自定义，避免固定的检查
        return new MatridxProviderManager(provider);
    }

    @Override
    public void afterPropertiesSet() {
        setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                authenticationEntryPoint.commence(httpServletRequest, httpServletResponse, e);
            }
        });
        setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                // 无操作-仅允许过滤器链继续到令牌端点
            }
        });
    }
}
