package com.matridx.springboot.common.speech.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.matridx.springboot.common.speech.audio.AliAudio;
import com.matridx.springboot.common.speech.dao.entities.AudioModel;
import com.matridx.springboot.common.speech.audio.BaiduAudio;
import com.matridx.springboot.common.speech.audio.IAudio;
import com.matridx.springboot.common.speech.service.svcinterface.IAudioService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Service
public class AudioServiceImpl implements IAudioService{
	
	/**
	 * 语音文件存放地址
	 */
	@Value("${matridx.audio.savepath:}")
	private String audioPath = null;
	
	/**
	 * keyid
	 */
	@Value("${matridx.audio.accessKeyId:}")
	private String accessKeyId = null;
	
	/**
	 * KeySecret
	 */
	@Value("${matridx.audio.accessKeySecret:}")
	private String accessKeySecret = null;
	
	/**
	 * 程序ID
	 */
	@Value("${matridx.audio.appid:}")
	private String appid = null;
	
	/**
	 * 语音调用类型
	 */
	@Value("${matridx.audio.audiotype:}")
	private String audiotype = null;
	
	/**
	 * 根据录音文件获取转换后的文字
	 * @param is
	 * @return
	 */
	public Map<String, Object> getStringFromAudio(InputStream is){
		
		if(StringUtil.isEmpty(audioPath)) {
			audioPath = "/audio/";
		}
		File file = new File(audioPath);
		if(!file.exists()) {
			file.mkdirs();
		}
		String filename = StringUtil.generateUUID()+".wav";
		//1.先保存音频文件
		saveToFile(is,audioPath+filename);
		
		DBEncrypt dbEncrypt = new DBEncrypt();
		
		IAudio audio =null;
		//2.调用外拨的语音识别接口
		if("ali".equals(audiotype)) {
			audio = new AliAudio(dbEncrypt.dCode(accessKeyId), dbEncrypt.dCode(accessKeySecret), dbEncrypt.dCode(appid));
		}
		else if("baidu".equals(audiotype)) {
			audio = new BaiduAudio(dbEncrypt.dCode(accessKeyId), dbEncrypt.dCode(accessKeySecret), dbEncrypt.dCode(appid));
		}
		
		AudioModel audioModel = audio.registerToken();
		
		Map<String, Object> resultMap = audio.sendAudio(audioModel.getToken(), audioPath+filename);
		/*
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("textString", "测试");
		resultMap.put("status", "success");*/
		
		return resultMap;
	}
	
	/**
	 * 保存音频文件，打算用于后期 python+keras实现自定义语音识别
	 * @param is
	 * @param filePath
	 * @return
	 */
	private boolean saveToFile(InputStream is, String filePath) {
		boolean flag = false;
		byte[] buffer = new byte[4096];
		FileOutputStream fos = null;
		BufferedOutputStream output = null;

		BufferedInputStream input = null;
		try {
			input = new BufferedInputStream(is);

			fos = new FileOutputStream(filePath);
			output = new BufferedOutputStream(fos);
			int n = -1;
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStream(new Closeable[] { 
			is, input, output, fos });
		}

		return flag;
	}

	
	/**
	 * 关闭流
	 * @param stream
	 */
	private void closeStream(Closeable[] streams) {
		if(streams==null || streams.length < 1)
			return;
		for(int i=0;i<streams.length;i++){
			try {
				Closeable stream = streams[i];
				if(null != stream) {
					stream.close();
					stream = null;
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
