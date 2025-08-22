package com.matridx.igams.wechat.service.svcinterface;

import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjqxDto;
import com.matridx.igams.wechat.dao.entities.SjqxModel;

public interface ISjqxService extends BaseBasicService<SjqxDto, SjqxModel>{

	/**
	 * 查询送检权限信息
	 * @param unionid
	 * @param restTemplate 
	 * @return
	 */
	List<SjqxDto> getSjqxList(String unionid, RestTemplate restTemplate);

}
