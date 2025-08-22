package com.matridx.springboot.common.speech.audio;

import com.matridx.springboot.common.speech.dao.entities.AudioModel;

import java.util.Map;

public interface IAudio {
	/**
	 * 用户注册
	 * @return
	 */
    AudioModel registerToken();
	
	/**
	 * 发送语音信息并得到反馈
	 * @param token
	 * @param filename
	 */
    Map<String, Object> sendAudio(String token, String filename);
	
	/**
	 * 发送语音信息并得到反馈
	 * @param token
	 * @param filename
	 */
    Map<String, Object> sendAudioByAnsy(String token, String filename);
	
	/**
	 * 根据ID检查翻译进度
	 * @param checkid
	 * @return
	 */
    Map<String, Object> checkProcess(String checkid);
	
	/**
	 * 语音信息翻译中途的信息
	 * @param response
	 */
	//public void callBackProcessing(int index, String response);
	
	/**
	 * 语音信息翻译完成结果
	 * @param index
	 * @param response
	 */
	//public void callBackProcessed(int index,String response);
}
