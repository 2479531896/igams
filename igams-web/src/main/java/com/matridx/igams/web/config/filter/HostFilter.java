package com.matridx.igams.web.config.filter;

import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  * 使用注解标注过滤器
 * @WebFilter将一个实现了javax.servlet.Filter接口的类定义为过滤器
 * 属性filterName声明过滤器的名称,可选
 * 属性urlPatterns指定要过滤 的URL模式,也可使用属性value来声明.(指定要过滤的URL模式是必选属性)
 */
@WebFilter(filterName="HostFilter", urlPatterns="/*", asyncSupported = true)
public class HostFilter implements Filter{
	
	//访问主程序端口
	@Value("${matridx.blanklist.host:}")
	private String blankhost;
	
	private String[] blankList = null;
	private boolean isInit = false;
	
	private final Logger log = LoggerFactory.getLogger(HostFilter.class);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        //StringBuffer url = req.getRequestURL();
        //String s = req.getRequestURL().toString();
        // 头攻击检测  过滤主机名
        String requestHost = req.getHeader("host");
        //String requestServerName = req.getServerName();
        //log.error("doFilter:" + req.getRequestURL() + " requestHost:"+requestHost + " blankhost:" + requestServerName);
        //log.error("doFilter2:" + req.getRequestURL() + " requestHost:"+req.getHeader("Host") + " blankhost:" + blankhost);
        if (requestHost != null && !checkBlankList(requestHost)) {
        	res.setStatus(403);
        	res.flushBuffer();
        	log.error("failaccess:" + req.getRequestURL() + " requestHost:"+requestHost + " blankhost:" + blankhost);
            return;
        }
        chain.doFilter(request, response);
	}
	
    //判断主机是否存在白名单中
    private boolean checkBlankList(String host){
    	if(!isInit) {
    		isInit = true;
    		if(StringUtil.isNotBlank(blankhost)) {
                blankList = blankhost.split(",");
            }
    	}
    	if(blankList == null) {
            return true;
        }

		for (String s : blankList) {
            //这里host有时候会包括项目端口号，下面白名单IP可以写成一个集合，看集合是否包含当前访问IP
            if (host.contains(s)) {
                return true;
            }
        }
  
        return false;
    }

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
