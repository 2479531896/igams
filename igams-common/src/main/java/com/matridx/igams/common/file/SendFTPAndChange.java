package com.matridx.igams.common.file;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.ftp.FtpUtil;

public class SendFTPAndChange extends Thread {
	
	private final Logger logger = LoggerFactory.getLogger(SendFTPAndChange.class);
	
	private FjcfbModel fjModel;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	
	public SendFTPAndChange(FjcfbModel fjModel,AmqpTemplate amqpTempl){
		this.fjModel = fjModel;
		this.amqpTempl = amqpTempl;
	}
	
	/**
	 * 线程主处理
	 */
	@Override
	public void run() {
		if(fjModel == null || StringUtil.isBlank(fjModel.getFwjlj()))
			return;
		try {
			//如未达到转换次数上限
			if(!AttachHelper.isOutOfTimes(fjModel)){
				//文件上传ftp服务器
				boolean result = addDocToFtp();
//				Logger.getLogger().out(retMsg);
				if(result){
					//插入信息到mq
					String retMqMsg = sendMessagetoMQ();
					logger.info(retMqMsg);
				}
			}else{
				logger.error(" ---------> 转换失败，且该文件失败次数已达系统转换次数上限！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将word上传到ftp服务器
	 * @throws Exception 
	 */
	public boolean addDocToFtp() throws Exception{
		
		IFjcfbService fjcfbService = ServiceFactory.getService(IFjcfbService.class);
		
		String ftpPath = fjcfbService.getFtpPath();
        String ftpUrl = fjcfbService.getFtpUrl();
        String ftpUser = new DBEncrypt().dCode(fjcfbService.getFtpUser());
        String ftpPassword = new DBEncrypt().dCode(fjcfbService.getFtpPD());
        int port = Integer.parseInt(fjcfbService.getFtpPort());
        
      	DBEncrypt crypt = new DBEncrypt();
      	
        String path = GlobalString.FILE_STOREPATH_PREFIX+crypt.dCode(fjModel.getFwjlj());
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		String fileName = crypt.dCode(fjModel.getFwjm());
		//更新数据库的信息
		FjcfbModel updateFj = new FjcfbModel();
		updateFj.setFjid(fjModel.getFjid());
		
		if(fileName.endsWith(".doc")||fileName.endsWith(".docx")||fileName.endsWith(".pdf")){
			logger.info(" ---------> 文件异步上传开始");
			File upfile = new File(path+"/"+fileName);
			if(!upfile.exists()){
				logger.error("--------->文件异步上传失败。原因：文件不存在");
				//更新附件表转换次数操作
				fjcfbService.updateFjljFail(updateFj);
				return false;
			}
			//文件上传到ftp
			FTPClient ftp = FtpUtil.connect(ftpPath, ftpUrl, port, ftpUser, ftpPassword);
			FtpUtil.upload(upfile,ftp);
			logger.info("--------->文件异步上传ftp成功");
			//更新附件表转换次数操作
			fjcfbService.updateFjljFail(updateFj);
			return true;
		}else{
			//非doc、docx文件直接算是转换完成的
			updateFj.setZhbj("1");
			fjcfbService.update(updateFj);
		}
		return false;
	}
	
	/**
	 * 向mq发送消息（插入队列）
	 */
	public String sendMessagetoMQ(){
		String message;
		DBEncrypt crypt = new DBEncrypt();
		Map<String, String> map = new HashMap<>();
		map.put("wordName", crypt.dCode(fjModel.getFwjm()));
		map.put("fjid", fjModel.getFjid());
		map.put("barcodeStr", fjModel.getBarcodeStr());
		amqpTempl.convertAndSend("doc2pdf_exchange","mq.tran.w2p",JSONObject.toJSON(map));
		
		message = "--------->文件信息上传MQ启动";
		return message;
	}

	public FjcfbModel getFjcfbModel() {
		return fjModel;
	}

	public void setFjcfbModel(FjcfbModel fjModel) {
		this.fjModel = fjModel;
	}

	public AmqpTemplate getAmqpTempl() {
		return amqpTempl;
	}

	public void setAmqpTempl(AmqpTemplate amqpTempl) {
		this.amqpTempl = amqpTempl;
	}

}
