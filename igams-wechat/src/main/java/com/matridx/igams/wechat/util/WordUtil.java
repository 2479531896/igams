package com.matridx.igams.wechat.util;

import java.io.IOException;
import java.util.Map;
import org.springframework.amqp.core.AmqpTemplate;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;

public class WordUtil{
	/**
	 * 填充文档方法
	 * @param map	//送检信息参数
	 * @throws IOException 
	 */
	public boolean ExportWord(Map<String, Object> map,IFjcfbService fjcfbService,AmqpTemplate amqpTempl,String DOC_OK,String FTP_URL,ISjxxService sjxxService){
		String templateFilePath = (String)map.get("templPath");
//		if(templateFilePath.endsWith(".doc")) {
//
//
//		}else
		if(templateFilePath.endsWith(".docx")){
			//生成正式的报告
			XWPFWordUtil wordUtil = new XWPFWordUtil();
			return wordUtil.GenerateReport(map,fjcfbService,amqpTempl,DOC_OK,FTP_URL,sjxxService);
		}
		return true;
	}
}
