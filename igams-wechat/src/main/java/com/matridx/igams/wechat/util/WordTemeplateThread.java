package com.matridx.igams.wechat.util;

import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;

import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;

public class WordTemeplateThread extends Thread{
	private Map<String,Object> map;
	
	private IFjcfbService fjcfbService;
	
	private AmqpTemplate amqpTempl;
	
	private String DOC_OK;
	
	private String FTP_URL;
	
	private ISjxxService sjxxService;
	
	public WordTemeplateThread(Map<String, Object> map,IFjcfbService fjcfbService,AmqpTemplate amqpTempl,String DOC_OK,String FTP_URL,ISjxxService sjxxService){
		this.map = map;
		this.fjcfbService = fjcfbService;
		this.amqpTempl = amqpTempl;
		this.DOC_OK = DOC_OK;
		this.FTP_URL=FTP_URL;
		this.sjxxService=sjxxService;
	}


	@Override
	public void run() {
			try{
				//ReportWordExport.ExportWord(sjwzxxList, map,fileName,sjbgsmDto);
				WordUtil wordUtil = new WordUtil();
				wordUtil.ExportWord(map,fjcfbService,amqpTempl,DOC_OK,FTP_URL,sjxxService);
				
			} catch (Exception e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
