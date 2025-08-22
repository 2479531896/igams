package com.matridx.las.frame.connect.channel.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.svcinterface.IHttpService;
import com.matridx.las.frame.connect.util.CommonChannelUtil;
import com.matridx.las.frame.connect.util.ConnectUtil;
import com.matridx.springboot.util.base.StringUtil;

import java.lang.reflect.Type;
import java.util.Map;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HttpPcrService extends HttpService implements IHttpService {
	
	@Override
	public boolean sendMessage(FrameModel sendModel,String info) {
		if(StringUtil.isBlank(info) || StringUtil.isBlank(address))
			return false;
		String[] s_spliStrings = info.split(" ");
		if(s_spliStrings.length < 2)
			return false;
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		
		String urlString =  (address.startsWith("http")?"":"http://") + address + (address.endsWith("/")?"":"/") + s_spliStrings[1];
		
		for(int i=2;i<s_spliStrings.length;i++) {
			if(i==2)
				urlString = urlString + "?";
			else
				urlString = urlString + "&";
			urlString = urlString + "pa"+i + "=" + s_spliStrings[i];
			//paramMap.add("pa"+i, s_spliStrings[i]);
		}
		//PCR这边采用的是 http://ip/code?参数 code 为dll方法 
		String resultString = ConnectUtil.sendConnectRequest(urlString ,paramMap,String.class);
		if(resultString==null) {
			return false;
		}
		Type type = new TypeToken<Map<String,String>>() {}.getType();
		Map<String,String> m_result = JSON.parseObject(resultString, type);
		//设置frameid
		// 发送消息之前确认第一次返回的队列是否为空，如果为空，则设置空队列
		String rec_frameId = CommonChannelUtil.getFrameIdFromSend(sendModel);
		
		m_result.put("frameid", rec_frameId);
		//设置返回的协议
		m_result.put("command", sendModel.getCommand());
		
		return super.recvBackCmdByString(m_result);
	}

	@Override
	public Object getChannel() {
		return null;
	}

	@Override
	public boolean init(Map<String, Object> map) {

		return super.init(map);
	}
}
