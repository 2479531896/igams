package com.matridx.igams.common.file;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;


public class AdjunctTransform extends Thread {
	
	private final Logger logger = LoggerFactory.getLogger(AdjunctTransform.class);
	
	private String pdfFile;
	
	private String docFile;
	
	//private String swfPath;
	
	private FjcfbModel fjModel;
	
	public AdjunctTransform(FjcfbModel fjModel){
		this.fjModel = fjModel;
		//this.swfPath = "transform/";
		if(fjModel == null || StringUtil.isBlank(fjModel.getWjlj()))
			return;
		
		DBEncrypt crypt = new DBEncrypt();
		
		String path = GlobalString.FILE_STOREPATH_PREFIX+crypt.dCode(fjModel.getFwjlj());
		
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		String fileName = crypt.dCode(fjModel.getFwjm());
		this.pdfFile = path + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + ".pdf";
		this.docFile = path + "/" + fileName;
	}
	
	/**
	 * 转换为PDF文件并加水印， 将文件保存在相同路径
	 * @throws IOException 
	 */
	public String transform() {
		String message;

		String fileName = getDocFile();
		File file = new File(fileName);
		if(!file.exists()){
			message = "文件异步转换失败。原因：文件不存在";
			return message;
		}
		//启动openoffice, 以保证能连接上
		/*
		 * TransformerConfig config = new TransformerConfig(); new Transformer(config);
		 * 
		 * //开始执行文档转换 DocTransAndWatermarkformer docTrans = new
		 * DocTransAndWatermarkformer(config);
		 * 
		 * docTrans.setQueueFile(new QueueFile(fileName)); docTrans.setFjModel(fjModel);
		 * docTrans.start();
		 */
		message = "文件异步转换启动。路径为：" + getPdfFile();
		return message;
	}
	
	public void run(){
		if(fjModel == null || StringUtil.isBlank(fjModel.getWjlj()))
			return;
		String retMsg = transform();
		logger.info(retMsg);
	}
	

	public String getPdfFile() {
    	return pdfFile;
    }

	public void setPdfFile(String pdfFile) {
    	this.pdfFile = pdfFile;
    }

	public String getDocFile() {
		return docFile;
	}
	public void setDocFile(String docFile) {
		this.docFile = docFile;
	}

//	public String getSwfPath() {
//		return swfPath;
//	}
//
//	public void setSwfPath(String swfPath) {
//		this.swfPath = swfPath;
//	}

	public FjcfbModel getFjcfbModel() {
    	return fjModel;
    }

	public void setFjcfbModel(FjcfbModel fjModel) {
    	this.fjModel = fjModel;
    }
}
