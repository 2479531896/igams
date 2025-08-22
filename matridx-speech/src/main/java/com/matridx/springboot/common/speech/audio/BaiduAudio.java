package com.matridx.springboot.common.speech.audio;

import java.util.HashMap;
import java.util.Map;

import com.matridx.springboot.common.speech.dao.entities.AudioModel;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.aip.speech.AipSpeech;


public class BaiduAudio implements IAudio{
	//https://ai.aliyun.com/nls
	private static final Logger log = LoggerFactory.getLogger(BaiduAudio.class);
	
	private String keyId;
	
	private String keySecret;
	
	private String appKey;
	/**
	 * 根据AccessKeyId和AccessKeySecret生成token
	 */
	public BaiduAudio(String accessKeyId,String accessKeySecret,String appid) {
		
		this.keyId = accessKeyId;
		
		this.keySecret = accessKeySecret;
		
		this.appKey = appid;
	}

	/**
	 * 用户注册
	 * @return
	 */
	public AudioModel registerToken() {
		
		AudioModel resultModel = new AudioModel();
		
		return resultModel;
	}
	
	/**
	 * 发送语音信息并得到反馈
	 * @param ins
	 */
	public Map<String, Object> sendAudio(String token,String filename){
		Map<String, Object> result = new HashMap<String, Object>();
		try {

			// 初始化一个AipSpeech
	        AipSpeech client = new AipSpeech(appKey, keyId, keySecret);
			
	        // 可选：设置网络连接参数
	        client.setConnectionTimeoutInMillis(2000);
	        client.setSocketTimeoutInMillis(60000);
			
			// 对本地语音文件进行识别
		    JSONObject asrRes = client.asr(filename, "wav", 16000, null);
		    
		    int errcode = (int)asrRes.get("err_no");

		    if(errcode==0) {
				result.put("textString", asrRes.getJSONArray("result").get(0));
				result.put("status", "success");
		    }else {
		    	result.put("message", (String)asrRes.get("err_msg"));
				result.put("status", "fail");
		    }
			
		} catch (Exception e) {
			log.error(e.getMessage());
			result.put("status", "fail");
			result.put("message", e.getMessage());
		}
		return result;
	}
	
	/**
	 * 发送语音信息并得到反馈
	 * @param ins
	 */
	public Map<String, Object> sendAudioByAnsy(String token,String filename){
		Map<String, Object> result = new HashMap<String, Object>();
		
		return result;
	}
	
	/**
	 * 根据ID检查翻译进度
	 * @param checkid
	 * @return
	 */
	public Map<String, Object> checkProcess(String checkid){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		return result;
	}
}
