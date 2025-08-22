package com.matridx.igams.common.factory;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 服务工厂（用于取spring容器中定义的bean）
 * @author Administrator
 * 
 */
@Component
public class ServiceFactory implements ApplicationContextAware{
	private ServiceFactory() {

	}

	public static ApplicationContext applicationContext = null;

	
	/**
	 * 常用于知道配置的服务名取服务
	 * 
	 * @param svrCode
	 * @return
	 */
	public static Object getService(String svrCode) {
		
		if (applicationContext != null){
			return applicationContext.getBean(svrCode);
		}
		
		return null;

	}

	/**
	 * 用于按规范命名的服务：通过类的类型取服务
	 * 
	 * @param svrCode
	 * @return
	 */
	public static<T> T getService(Class<T> c) {
		return applicationContext.getBean(c);

	}

	/**
	 * 取数据源
	 * 
	 * @return
	 */
	public static DataSource getDataSource(String svrCode) {
		return (DataSource) getService(svrCode);

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
		if(ServiceFactory.applicationContext == null){  
			ServiceFactory.applicationContext  = applicationContext;  
        }
	}

}
