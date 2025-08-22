package com.matridx.igams.common.scheduled;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matridx.igams.common.dao.entities.DsrwszDto;
import com.matridx.igams.common.factory.ServiceFactory;

public class ScheduleRunnable implements Runnable{

	private final DsrwszDto dsrwszDto;
	
	private final Logger log = LoggerFactory.getLogger(ScheduleRunnable.class);
	
	public ScheduleRunnable(DsrwszDto t_dsrwszDto) {
		dsrwszDto = t_dsrwszDto;
	}
	
	@Override
    public void run() {
		log.error("ScheduleRunnable run");
    	//执行类
    	String classService = dsrwszDto.getZxl();
    	//执行方法
    	String classMethod = dsrwszDto.getZxff();
    	//处理定时任务中的参数cs,注意格式写不对的，以及为null的，增加异常捕获
    	//参数的书写为name=18,height=165
		Map<String, String> map = new HashMap<>();
    	try {
        	if( !StringUtil.isBlank(dsrwszDto.getCs()) ) {
				log.error("ScheduleRunnable run dsrwCS:"+dsrwszDto.getCs());
            	List<String> csstr = Arrays.asList(dsrwszDto.getCs().split(","));
                for (String s : csstr) {
                    String[] str = s.split("=");
                    map.put(str[0], str[1]);
                }
        	}
    	}catch (Exception e) {
    		// TODO Auto-generated catch block
    		log.error("获取参数失败：" + e.getMessage());
		}
    	
		try {
        	
    		Object serviceInstance = ServiceFactory.getService(classService);//获取方法所在class
    		
    		Method getCountMethod;
    		if(StringUtil.isBlank(dsrwszDto.getCs())){
        		//获取方法（定时任务无参数）
    			getCountMethod = serviceInstance.getClass().getMethod(classMethod);//获取计数方法
    			//执行方法
    			getCountMethod.invoke(serviceInstance);
    		}else {
        		//获取方法（定时任务有参数）
    			getCountMethod = serviceInstance.getClass().getMethod(classMethod,Map.class);//获取计数方法
    			//执行方法
    			getCountMethod.invoke(serviceInstance,map);
    		}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("执行定时任务失败：" + e.getMessage());
		}
    }
}
