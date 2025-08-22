package com.matridx.igams.wechat.util;

import com.matridx.springboot.util.xml.BasicXmlReader;
public class PrinterThread extends Thread{
	private String url;
	
	public PrinterThread(String url) {
		this.url=url;
	}
	
	
	@Override
	public void run() {
		try{
			BasicXmlReader.urlConnWebServiceByGet(url,"");
		} catch (Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
