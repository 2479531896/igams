package com.matridx.igams.common.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.dao.entities.QxModel;

/**
 * preHandle=>请求处理前拦截，处理通过返回true，否则返回false不进行处理
 * 
 * postHandle=>请求处理后拦截(页面渲染前)，处理通过返回true，否则返回false
 * 
 * afterCompletion=>请求处理后拦截，(同上)
 * 
 * 这里主要为了拦截 PageList的请求，去主服务器获取信息
 * @author linwu
 *
 */
public class QxCloudInterceptor implements HandlerInterceptor{
	
	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	ICommonService commonService;
	
	@Value("${matridx.prefix.igamsweb:}")
	private String webPrefix;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	private final Logger log = LoggerFactory.getLogger(QxCloudInterceptor.class);
	//@Autowired
    //private TokenStore tokenStore;

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
    	//log.error("---------------------开始进入云请求地址拦截----------------------------");
		// 确认url访问路径是否拥有权限
		//ToDo：需要确认url 和系统按钮权限和菜单权限的匹配。同时要把特殊的 common 等非按钮公用权限排除，允许访问
		String url = request.getRequestURI();
		//转发主站确认是否有权限
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("access_token", request.getParameter("access_token"));
		paramMap.add("url", url);
		paramMap.add("urlPrefix", urlPrefix);
		String t_webPrefix = webPrefix.replace("/", "");
		boolean result = commonService.checkCommonPower(url);
		if(!result) {
			try {
				result = restTemplate.postForObject("http://"+ t_webPrefix + "/common/common/checkRseourcePower", paramMap, Boolean.class);
			}catch(Exception e) {
				log.error("权限校验发生异常：" + e.getMessage());
			}
		}
		if(!result) {
			log.error("权限校验结果为" + false);
			log.error("请求路径为" + url + ",urlPrefix为" + urlPrefix);
			throw new BusinessException("message","请求---"+urlPrefix+url+",无访问权限!");		
		}
		return true;
	}

    @SuppressWarnings("unchecked")
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) {
    	//log.info("--------------处理请求完成后视图渲染之前的处理操作---------------");
    	String path = request.getServletPath();
    	//如果为列表页面，则从上下文中获取登录用户信息，并把操作按钮放到页面上去
    	if(path.contains("/pageList")){

    		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
    		
    		paramMap.add("zyid", request.getParameter("zyid"));
			paramMap.add("access_token",request.getParameter("access_token"));
    		
			String t_webPrefix = webPrefix.replace("/", "");
    		List<QxModel> now_jsczDtos =restTemplate.postForObject("http://"+ t_webPrefix + "/common/common/getBtnsByMenuId", paramMap, List.class);
    		
    		if(modelAndView!=null)
    			modelAndView.addObject("czdmlist", now_jsczDtos);
    	}
    	if(modelAndView!=null)
    		modelAndView.addObject("urlPrefix", urlPrefix);
	}
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) {
    	//log.info("---------------视图渲染之后的操作-------------------------0");
	}
}
