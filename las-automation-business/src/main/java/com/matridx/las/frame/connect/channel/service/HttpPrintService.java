package com.matridx.las.frame.connect.channel.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.util.ConnectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Type;
import java.util.Map;

public class HttpPrintService extends HttpService  {

	private final Logger log = LoggerFactory.getLogger(HttpPrintService.class);

	@Override
	public boolean sendMessage(FrameModel frameModel,String info) {
		try {
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			String url=analysisCommand(info,paramMap);
			String resultString = ConnectUtil.sendConnectRequest(address + url ,paramMap,String.class);
			if(resultString==null) {
				return false;
			}
			Type type = new TypeToken<Map<String,String>>() {}.getType();
			Map<String,String> m_result = JSON.parseObject(resultString, type);
			
			if("success".equals(m_result.get("status")))
				return super.recvBackCmdByModel(frameModel);
			else
				return false;
		}catch (Exception e){
			log.error("打印机请求失败---"+e.getMessage());
			return false;
		}
	}

	@Override
	public Object getChannel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean init(Map<String, Object> map) {
		log.error("HttpPrintService-init:{}",JSON.toJSONString(map));
		super.init(map);
		//if(StringUtil.isNotBlank(confirmflg) && "true".equals(confirmflg))
			//return ConnectUtil.confirmConnect(ztqrdz);
		return true;
	}

	/**
	 * 解析命令
	 * @return
	 */
	public String analysisCommand(String info,MultiValueMap<String, Object> paramMap){
		String[] words = info.split(" ");
		String url = "";
		if(words.length>1 ){//判断是否为打印命令
			url="/" + words[1];
		}
		if(words.length>2){
			//先按照JSON的方式进行调用，出错后采用普通方式调用
			try {
				Type type = new TypeToken<Map<String,String>>() {}.getType();
				Map<String, String> map = JSON.parseObject(words[2], type);
				for(String key : map.keySet()){
					String value = map.get(key);
					paramMap.add(key, value);
				}
			}catch(Exception e) {
				paramMap.add("param", words[2]);
			}
		}
		return url;
	}
}
