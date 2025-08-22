package com.matridx.igams.common.config;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServiceConfig {
    /**
     * 注入servlet  bean name不能dispatcherServlet 否则会覆盖dispatcherServlet
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean(name = "cxfServlet")
    public ServletRegistrationBean cxfServlet() {
    	return new ServletRegistrationBean(new CXFServlet(),"/webservice/*");
	}
}
