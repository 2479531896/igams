package com.matridx.igams.web.config.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 *  * 使用注解标注过滤器
 * @WebFilter将一个实现了javax.servlet.Filter接口的类定义为过滤器
 * 属性filterName声明过滤器的名称,可选
 * 属性urlPatterns指定要过滤 的URL模式,也可使用属性value来声明.(指定要过滤的URL模式是必选属性)
 */
@WebFilter(filterName="AddressAuthorityFilter", urlPatterns="/*", asyncSupported = true)
public class AddressAuthorityFilter implements Filter{
	
//	private final Logger log = LoggerFactory.getLogger(AddressAuthorityFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        if(securityContext!=null) {
//			// 获取当前认证了的 principal(当事人),或者 request token (令牌)
//			// 如果没有认证，会是 null
//			Authentication authentication = securityContext.getAuthentication();
//			if(authentication!=null) {
//				@SuppressWarnings("unchecked")
//				List<IgamsGrantedAuthority> authorities = (List<IgamsGrantedAuthority>)authentication.getAuthorities();
//
//				if(authorities!=null && authorities.size()>0){
//					IgamsGrantedAuthority authority = authorities.get(0);
//
//					User user = authority.getYhxx();
//				}
//				// 确认url访问路径是否拥有权限
//				//ToDo：需要确认url 和系统按钮权限和菜单权限的匹配。同时要把特殊的 common 等非按钮公用权限排除，允许访问
//				String url = req.getRequestURI();
//
//			}
//        }
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
