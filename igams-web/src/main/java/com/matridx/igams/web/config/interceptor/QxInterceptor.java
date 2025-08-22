package com.matridx.igams.web.config.interceptor;

import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.security.IgamsGrantedAuthority;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * preHandle=>请求处理前拦截，处理通过返回true，否则返回false不进行处理

　　postHandle=>请求处理后拦截(页面渲染前)，处理通过返回true，否则返回false

　　afterCompletion=>请求处理后拦截，(同上)
 * @author linwu
 *
 */
public class QxInterceptor implements HandlerInterceptor{
	
	private final Logger log = LoggerFactory.getLogger(QxInterceptor.class);
	//@Autowired
    //private TokenStore tokenStore;

	@Autowired
	ICommonService commonService;
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext!=null) {
			// 获取当前认证了的 principal(当事人),或者 request token (令牌)
			// 如果没有认证，会是 null
			Authentication authentication = securityContext.getAuthentication();
			if(authentication!=null) {
				@SuppressWarnings("unchecked")
				List<IgamsGrantedAuthority> authorities = (List<IgamsGrantedAuthority>)authentication.getAuthorities();

				IgamsGrantedAuthority authority = authorities.get(0);

				User user = authority.getYhxx();
				// 确认url访问路径是否拥有权限
				//ToDo：需要确认url 和系统按钮权限和菜单权限的匹配。同时要把特殊的 common 等非按钮公用权限排除，允许访问
				String url = request.getRequestURI();
				boolean result = commonService.checkCommonPower(url);

				if(!result){
					result=commonService.checkWbCzqxExt(request,user,null);
				}
				if(!result) {
                    result=commonService.checkCzqxExt(url,user,null);
                }
				if(!result) {
//					throw new BusinessException("message","请求---"+url+",无访问权限!");
					log.error("请求---"+url+",无访问权限!");
					response.setStatus(999);
					return false;
//					log.error("请求路径为" + url);
				}
			}
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
    		//String name = request.getRemoteUser();
    		// 获取安全上下文对象，就是那个保存在 ThreadLocal 里面的安全上下文对象
    		// 总是不为null(如果不存在，则创建一个authentication属性为null的empty安全上下文对象)
    		SecurityContext securityContext = SecurityContextHolder.getContext();
    		
    		// 获取当前认证了的 principal(当事人),或者 request token (令牌)
    		// 如果没有认证，会是 null
    		Authentication authentication = securityContext.getAuthentication();
    		
    		List<IgamsGrantedAuthority> authorities = (List<IgamsGrantedAuthority>)authentication.getAuthorities();
    		
    		IgamsGrantedAuthority authority = authorities.get(0);
    		
    		List<QxModel> qxDtos = authority.getYhxx().getQxModels();

            List<QxModel> now_jsczDtos = qxDtos.stream()
                    .filter(qxModel -> qxModel.getZyid().equals(request.getParameter("zyid"))).collect(Collectors.toList());

			if(modelAndView!=null) {
                modelAndView.addObject("czdmlist", now_jsczDtos);
            }
    	}
	}
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) {
    	//log.info("---------------视图渲染之后的操作-------------------------0");
	}
}
