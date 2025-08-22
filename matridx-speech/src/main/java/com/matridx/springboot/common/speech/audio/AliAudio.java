package com.matridx.springboot.common.speech.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.matridx.springboot.common.speech.dao.entities.AudioModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizer;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizerListener;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizerResponse;
public class AliAudio implements IAudio{
	//https://ai.aliyun.com/nls
	private static final Logger log = LoggerFactory.getLogger(AliAudio.class);
	
	private String keyId;
	
	private String keySecret;
	
	private String appKey;
	
	private Map<String, String> audioTextMap = new HashMap<>();
	/**
	 * 根据AccessKeyId和AccessKeySecret生成token
	 */
	public AliAudio(String accessKeyId,String accessKeySecret,String appid) {
		
		this.keyId = accessKeyId;
		
		this.keySecret = accessKeySecret;
		
		this.appKey = appid;
	}
	
	/**
	 * 用户注册
	 * @return
	 */
	public AudioModel registerToken() {
		
		AudioModel resultModel = null;
		
		try {
			AccessToken accessToken = AccessToken.apply(this.keyId, this.keySecret);
			
			if(accessToken!=null) {
				resultModel = new AudioModel();
				resultModel.setToken(accessToken.getToken());
				resultModel.setExpireTime(accessToken.getExpireTime());
			}
		}catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
		}
		
		return resultModel;
	}
	
	private SpeechRecognizerListener getRecognizerListener(final String inputName,CountDownLatch latch) {
		SpeechRecognizerListener listener = new SpeechRecognizerListener() {
			// 识别出中间结果.服务端识别出一个字或词时会返回此消息.仅当setEnableIntermediateResult(true)时,才会有此类消息返回
			@Override
			public void onRecognitionResultChanged(SpeechRecognizerResponse response) {
				// 状态码 20000000 表示识别成功
				if(response.getStatus()==20000000) {
					audioTextMap.put("audio_"+inputName, response.getRecognizedText());
					audioTextMap.put("audio_"+inputName+"_result", "false");
				}
				
				// 事件名称 RecognitionResultChanged
				System.out.println("input stream: " + inputName +
						", name: " + response.getName() +
						// 状态码 20000000 表示识别成功
						", status: " + response.getStatus() +
						// 一句话识别的中间结果
						", result: " + response.getRecognizedText());
			}

			//识别完毕
			@Override
			public void onRecognitionCompleted(SpeechRecognizerResponse response) {
				// 状态码 20000000 表示识别成功
				if(response.getStatus()==20000000) {
					audioTextMap.put("audio_"+inputName, response.getRecognizedText());
					audioTextMap.put("audio_"+inputName+"_result", "true");
				}
				System.out.println("input stream: " + inputName +
                        ", name: " + response.getName() +
                        // 状态码 20000000 表示识别成功
                        ", status: " + response.getStatus() +
                        // 一句话识别的完整结果
                        ", result: " + response.getRecognizedText());
				latch.countDown();
			}
		};
		return listener;
	}
	
	/**
	 * 语音线程
	 * @author linwu
	 *
	 */
	public class AliAudioTask implements Runnable{
		
		private String appKey;
		private NlsClient client;
		private CountDownLatch latch;
		private String audioFile;
		private boolean isHalfwayReturn;

		public AliAudioTask(String appKey, NlsClient client, CountDownLatch latch, String audioFile,boolean isMidReturn) {
			this.appKey = appKey;
			this.client = client;
			this.latch = latch;
			this.audioFile = audioFile;
			this.isHalfwayReturn = isMidReturn;
		}
		
		@Override
		public void run() {
			SpeechRecognizer recognizer = null;
			try {
				File file  = new File(audioFile);
				String audioFileName = file.getName();
				
				recognizer = new SpeechRecognizer(client, getRecognizerListener(audioFileName,latch));

				audioTextMap.put("audio_"+audioFileName+"_result", "false");
				
				recognizer.setAppKey(appKey);
				// 设置音频编码格式
				recognizer.setFormat(InputFormatEnum.PCM);
				// 设置音频采样率
				recognizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_8K);
				// 设置是否返回中间识别结果
				recognizer.setEnableIntermediateResult(isHalfwayReturn);

				// Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
				recognizer.start();
				InputStream ins = new FileInputStream(file);
				// Step3 语音数据来自声音文件用此方法,控制发送速率;若语音来自实时录音,不需控制发送速率直接调用 recognizer.sent(ins)即可
				recognizer.send(ins);
				// Step4 通知服务端语音数据发送完毕,等待服务端处理完成
				recognizer.stop();
				
			}catch (Exception e) {
				log.error(e.getMessage());
			} finally {
				// Step5 关闭连接
				if (null != recognizer) {
					recognizer.close();
				}
			}
		}
	}
	
	/**
	 * 发送语音信息并得到反馈
	 * @param ins
	 */
	public Map<String, Object> sendAudio(String token,String filename){
		NlsClient client = new NlsClient(token);
		CountDownLatch latch = new CountDownLatch(1);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			AliAudioTask task = new AliAudioTask(appKey, client, latch, filename,false);
			new Thread(task).start();
			latch.await();
			
			File file  = new File(filename);
			String audioFileName = file.getName();
			String textString = audioTextMap.get("audio_"+audioFileName);
			
			result.put("textString", textString);
			result.put("status", "success");
		} catch (Exception e) {
			log.error(e.getMessage());
			result.put("status", "fail");
			result.put("message", e.getMessage());
		} finally {
			client.shutdown();
		}
		return result;
	}
	
	/**
	 * 发送语音信息并得到反馈
	 * @param ins
	 */
	public Map<String, Object> sendAudioByAnsy(String token,String filename){
		NlsClient client = new NlsClient(token);
		CountDownLatch latch = new CountDownLatch(1);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			AliAudioTask task = new AliAudioTask(appKey, client, latch, filename,true);
			new Thread(task).start();

			File file  = new File(filename);
			String audioFileName = file.getName();
			
			result.put("status", "success");
			result.put("checkid", audioFileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.shutdown();
		}
		return result;
	}
	
	/**
	 * 根据ID检查翻译进度
	 * @param checkid
	 * @return
	 */
	public Map<String, Object> checkProcess(String checkid){
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		String textResult = audioTextMap.get("audio_"+checkid+"_result");
		String textString = audioTextMap.get("audio_"+checkid);
		
		result.put("status", "success");
		result.put("textResult", textResult);
		result.put("textString", textString);
		
		return result;
	}
}
