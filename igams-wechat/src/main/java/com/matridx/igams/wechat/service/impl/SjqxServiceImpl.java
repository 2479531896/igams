package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjqxDto;
import com.matridx.igams.wechat.dao.entities.SjqxModel;
import com.matridx.igams.wechat.dao.post.ISjqxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjqxService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SjqxServiceImpl extends BaseBasicServiceImpl<SjqxDto, SjqxModel, ISjqxDao> implements ISjqxService{

	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	
	/**
	 * 查询送检权限信息
	 * @param unionid
	 * @return
	 */
	@Override
	public List<SjqxDto> getSjqxList(String unionid, RestTemplate restTemplate) {
		// TODO Auto-generated method stub
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("unionid", unionid);
		restTemplate = new RestTemplate();
		String str = restTemplate.postForObject(menuurl + "/wechat/getSjqxList", paramMap, String.class);
		if (str != null){
            return JSONObject.parseArray(str, SjqxDto.class);
		}
		return null;
	}

}
