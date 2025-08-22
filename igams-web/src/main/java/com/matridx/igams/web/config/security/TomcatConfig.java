package com.matridx.igams.web.config.security;

import com.matridx.springboot.util.base.StringUtil;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用于添加对http的支持
 * @author lwj
 *
 */
@ConditionalOnProperty(value="server.http.port", matchIfMissing = false)
@Configuration
public class TomcatConfig {
	@Value("${server.http.port:}")
    private String httpPort;
	
//	@Value("${server.port:}")
//    private String httpsPort;

    /**
	 * 不安全的HTTP方法 漏洞 
	 * @return
	 */
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){
        		@Override
        		protected void postProcessContext(Context context) {
        			SecurityConstraint securityConstraint = new SecurityConstraint();
        	        securityConstraint.setUserConstraint("CONFIDENTIAL");
        	        SecurityCollection collection = new SecurityCollection();
        	        collection.addPattern("/*");
        	        collection.addMethod("HEAD");
        	        collection.addMethod("PUT");
        	        collection.addMethod("DELETE");
        	        collection.addMethod("TRACE");
        	        collection.addMethod("COPY");
        	        collection.addMethod("SEARCH");
        	        collection.addMethod("PROPFIND");
        	        securityConstraint.addCollection(collection);
        	        context.addConstraint(securityConstraint);
        		}
        };
        Connector connector = createStandardConnector();
        if(connector == null)
        	return null;
        tomcat.addAdditionalTomcatConnectors(connector); // 添加http
        
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        if(StringUtil.isBlank(httpPort))
        	return null;
        connector.setPort(Integer.parseInt(httpPort));
        return connector;
    }
    
}
