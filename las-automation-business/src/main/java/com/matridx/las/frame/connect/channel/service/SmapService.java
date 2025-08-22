package com.matridx.las.frame.connect.channel.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.reflect.TypeToken;
import com.matridx.las.frame.connect.channel.command.FrameModel;
import com.matridx.las.frame.connect.svcinterface.IHttpService;
import com.matridx.las.frame.connect.util.ConnectUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Type;
import java.util.Map;

public class SmapService extends BaseService implements IHttpService {

	@Override
	public boolean sendMessage(FrameModel frameModel,String info) {
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		String url=analysisCommand(info,paramMap);
		ConnectUtil.sendConnectRequest(url,paramMap,Map.class);
		return true;
	}

	@Override
	public Object getChannel() {
		return null;
	}

	@Override
	public boolean init(Map<String, Object> map) {
		String ztqrdz = String.valueOf(map.get("ztqrdz"));
		if(StringUtil.isNotObjectBank(map.get("confirmflg"))&&Boolean.parseBoolean(String.valueOf(map.get("confirmflg"))))
			return ConnectUtil.confirmConnect(ztqrdz);
		return true;
	}

	public String analysisCommand(String info,MultiValueMap<String, Object> paramMap){
		String[] words = info.split(" ");
		String url = "";
		if(words.length>1 && "GYB".equals(words[1])){//判断是否为获取申请列表
			url="/ws/getApplyByCode";
		}
		if(words.length>2){
			Type type = new TypeToken<Map<String,String>>() {}.getType();
			Map<String, String> map = JSON.parseObject(words[2], type);
			for(String key : map.keySet()){
				String value = map.get(key);
				paramMap.add(key, value);
			}
		}
		return url;
	}
}
