package com.matridx.web.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 自定义401错误码内容
 * @author linwu
 *
 */
@Component
public class IgamsAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	private final Logger log = LoggerFactory.getLogger(IgamsAuthenticationEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		log.info("Pre-authenticated entry point called,rejecting access. 401错误处理");
		log.info("401错误处理:ip:" +request.getRemoteAddr() + ":" +request.getRequestURI());
		//response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Access Denied.");
		if(isAjaxRequest(request)){
			String errorString = authException.getMessage();
			if(StringUtil.isNotBlank(errorString) && errorString.contains("锁定")) {
				log.error("401错误处理:AjaxRequest:"+errorString);
				response.sendError(452,authException.getMessage());
			}else {
				log.error("401错误处理:AjaxRequest_accessDenied"+(StringUtil.isNotBlank(errorString)?":"+errorString:""));
//				response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
				response.sendRedirect(applicationurl + "/accessDenied");
			}
		}else{
			//如果401错误，则确认有无传递token，主要是方便sessionOut 页面判断是否读取cookie进行再次认证
			String token = request.getParameter("access_token");
			if(StringUtil.isNotBlank(token)) {
				log.error("401错误处理:accessDenied:" + token);
				// 解决因为本地服务为http的，当过期时重定向会变成http的问题，需要从配置文件获取实际地址
				response.sendRedirect(applicationurl + "/accessDenied?access_token="+token);
			}else {
				log.error("401错误处理:blankDenied");
				// 解决因为本地服务为http的，当过期时重定向会变成http的问题，需要从配置文件获取实际地址
				response.sendRedirect(applicationurl + "/accessDenied");
			}
		}
	}

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String ajaxFlag = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(ajaxFlag);
	}

}
