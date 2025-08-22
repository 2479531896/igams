package com.matridx.server.wechat.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.util.Base64Util;
import com.matridx.igams.common.util.HttpUtil;
import com.matridx.springboot.util.base.StringUtil;

/**
 * 文字识别
 * @author linwu
 *
 */
public class WordRecognitionUtil {
	
	private static String access_token = null;
	
	private static boolean refreshing = false;
	

	//百度文字识别获取token地址
	private final static String TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
	//百度文字识别地址
	private final static String RECOGNITION_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
	//百度文字识别地址包含
	private final static String RECOGNITION_SITE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/general";
	
	final static Logger log = LoggerFactory.getLogger(WordRecognitionUtil.class);
	
	/**
	 * 根据isRefreshFlg 来判断是更新token 还是，获取token，之所以用同一个方法，是为了同步限制的原因
	 * @param restTemplate
	 * @param appkey
	 * @param secret
	 * @param isRefreshFlg
	 * @return
	 */ 
	public static String getToken(RestTemplate restTemplate,String appkey,String secret,boolean isRefreshFlg){
		if(StringUtil.isNotBlank(appkey)) {
			if(isRefreshFlg) {			
				getNewToken(restTemplate,appkey,secret,isRefreshFlg);
			}else{
				//正在更新中
				if(refreshing) {
					getNewToken(restTemplate,appkey,secret,isRefreshFlg);
				}
			}
		}
		return access_token;
	}
	/**
	 * 获取新的token
	 * @param restTemplate
	 * @param appkey
	 * @param secret
	 * @param isRefreshFlg
	 */
	private synchronized static void getNewToken(RestTemplate restTemplate,String appkey,String secret,boolean isRefreshFlg){
		if(!isRefreshFlg) {
            return;
        }
		try {
			//置token更新标记为true
			refreshing = true;
			//连接服务器，更新token
			String url = new StringBuffer(TOKEN_URL)
					// 1. grant_type为固定参数，官网获取的 API Key
					.append("?grant_type=client_credentials&client_id=").append(appkey)
					// 3. 官网获取的 Secret Key
					.append("&client_secret=").append(secret).toString();
			String result_str = restTemplate.getForObject(url, String.class);
			JSONObject jsonObject = JSONObject.parseObject(result_str);
			//lastGetDate = new Date();
			access_token = jsonObject.getString("access_token");
			//expires_in = accessTokenModel.getExpires_in();
			//更新完成，置token更新标记为false
			log.info("refresh wechat:" + access_token);
			refreshing = false;
		}catch(Exception e) {
			log.error(e.toString());
		}
	}
	
	/**
	 * 获取图片文字信息
	 * @param filePath
	 * @return
	 */
	public static String getWords(String filePath){
		StringBuffer sb = new StringBuffer("");
		try{
			if(StringUtil.isBlank(access_token)) {
				return sb.toString();
			}
			
			JSONArray words = getWordsFromNet(filePath);
			if(words== null) {
                return sb.toString();
            }
			
            for(int i=0; i<words.size(); i++){
            	String word = words.getJSONObject(i).getString("words");
            	sb.append(word);
            }
		}catch(Exception e){
			log.error(e.toString());
		}
		return sb.toString();
	}
	
	/**
	 * 获取图片文字信息
	 * @param filePath
	 * @return
	 */
	public static List<String> getWordList(String filePath){
		List<String> results = new ArrayList<>();
		try{
			if(StringUtil.isBlank(access_token)) {
				return results;
			}
			JSONArray words = getWordsFromNet(filePath);
			if(words== null) {
                return results;
            }
			
            for(int i=0; i<words.size(); i++){
            	String word = words.getJSONObject(i).getString("words");
            	results.add(word);
            }
		}catch(Exception e){
			log.error(e.toString());
		}
		return results;
	}
	
	/**
	 * 获取图片文字信息
	 * @param filePath
	 * @return
	 */
	public static List<Map<String, String>> getWordListBySite(String filePath){
		List<Map<String, String>> results = new ArrayList<>();
		try{
			if(StringUtil.isBlank(access_token)) {
				return results;
			}
			JSONArray words = getWordsSiteFromNet(filePath);
			if(words== null) {
                return results;
            }
			
            for(int i=0; i<words.size(); i++){
            	String word = words.getJSONObject(i).getString("words");
            	JSONObject locations = JSONObject.parseObject(words.getJSONObject(i).getString("location"));
            	Map<String, String> wordMap = new HashMap<>();
            	wordMap.put("word", word);
            	wordMap.put("top", locations.getString("top"));
            	wordMap.put("left", locations.getString("left"));
            	wordMap.put("width", locations.getString("width"));
            	wordMap.put("height", locations.getString("height"));
            	results.add(wordMap);
            }
		}catch(Exception e){
			log.error(e.toString());
		}
		return results;
	}
	
	/**
	 * 从网络上获取图片识别结果
	 * @param filePath
	 * @return
	 */
	private static JSONArray getWordsFromNet(String filePath) {
		try{
			byte[] imgData = FileUtil.readFileByBytes(filePath);
	        String imgStr = Base64Util.encode(imgData);
	        String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
	        /**
	         * {"log_id": 4133456239013677212, "words_result_num": 1, "words_result": [{"location": {"width": 119, "top": 104, "left": 100, "height": 15}, "words": "123456789012"}]}
	         */
	        String result2 = HttpUtil.post(RECOGNITION_URL, access_token, params);
	        JSONObject jsonObject = JSONObject.parseObject(result2);
	        JSONArray words = jsonObject.getJSONArray("words_result");
	        return words;
		}catch(Exception e){
			log.error(e.toString());
			return null;
		}
	}
	
	/**
	 * 从网络上获取图片识别结果
	 * @param filePath
	 * @return
	 */
	public static JSONArray getWordsSiteFromNet(String filePath) {
		try{
			byte[] imgData = FileUtil.readFileByBytes(filePath);
	        String imgStr = Base64Util.encode(imgData);
	        String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
	        /**
	         * {"log_id": 4133456239013677212, "words_result_num": 1, "words_result": [{"location": {"width": 119, "top": 104, "left": 100, "height": 15}, "words": "123456789012"}]}
	         */
	        String result2 = HttpUtil.post(RECOGNITION_SITE_URL, access_token, params);
	        JSONObject jsonObject = JSONObject.parseObject(result2);
	        JSONArray words = jsonObject.getJSONArray("words_result");
	        return words;
		}catch(Exception e){
			log.error(e.toString());
			return null;
		}
	}
}
