package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.BfglDto;
import com.matridx.igams.wechat.dao.entities.BfglModel;
import com.matridx.igams.wechat.dao.post.IToolUtilDao;
import com.matridx.igams.wechat.service.svcinterface.IToolUtilService;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.excel.ExcelHelper;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ToolUtilServiceImpl extends BaseBasicServiceImpl<BfglDto, BfglModel, IToolUtilDao> implements IToolUtilService{

	private Logger log = LoggerFactory.getLogger(ToolUtilServiceImpl.class);
	/**
	 * 根据患者姓名查询报告
	 * @param filePath
	 * @return
	 */
	public boolean getPdfReport(String filePath) {
		try {
			ExcelHelper eh = new ExcelHelper();
			List<Map<String, String>> rowsList = eh.readExcel(filePath);

			File file ;
	    	DBEncrypt dbEncrypt = new DBEncrypt();
			for(int i=0;i< rowsList.size();i++)
			{
		    	String hzxm = rowsList.get(i).get("1");
		    	Map<String, String> paraMap = new HashMap<>();
		    	paraMap.put("hzxm", hzxm);
		    	List<Map<String, String>> result= dao.getReportByHzxm(paraMap);
			     
			    if(result != null && result.size() > 0)
			    {
			    	for(int j=0;j<result.size();j++) {
			    		if(result.get(j)!=null) {
					    	String wjlj = result.get(j).get("wjlj");
					    	String realWjljString = dbEncrypt.dCode(wjlj);
					    	//realWjljString = "E:/Logs" + realWjljString.substring(realWjljString.lastIndexOf("/UP"));
					    	file = new File(realWjljString);
					    	if(!file.exists()) {
					    		log.error("lwjerror:file not exits:" + result.get(j).get("wjm") + " " + realWjljString );
					    	}
					    	FileUtils.copyFile(file, new File("/home/centos/pdf/" + result.get(j).get("wjm")));
			    		}else {
			    			System.out.println("null:" + j+":" + hzxm);
			    			log.error("lwjerror:null:" + j+":" + hzxm);
			    		}
			    	}
			    }
		    }
			return true;
			
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return false;
		}
	}
	
}
