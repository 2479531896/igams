package com.matridx.igams.common.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.GzpzDto;
import com.matridx.igams.common.dao.entities.GzpzModel;
import com.matridx.igams.common.dao.post.IGzpzDao;
import com.matridx.igams.common.service.svcinterface.IGzpzService;
import com.matridx.igams.common.util.RedisXmlConfig;

@Service
public class GzpzServiceImpl extends BaseBasicServiceImpl<GzpzDto, GzpzModel, IGzpzDao> implements IGzpzService{

	@Autowired
	RedisXmlConfig redisXmlConfig;
	/**
	 * 查询单条数据
	 */
	@Override
	public GzpzModel getModelById(String id){
		//return dao.getModelById(id);
		Map<String, String> taskConfig = redisXmlConfig.getTaskConfig(id);
		
		if(taskConfig == null)
			return null;
		
		GzpzModel gzpzModel = new GzpzModel();
		
		gzpzModel.setHdl(taskConfig.get("class-service"));
		gzpzModel.setHdmx(taskConfig.get("class-entity"));
		gzpzModel.setGzlx(taskConfig.get("id"));
		gzpzModel.setLxmc(taskConfig.get("name"));
		gzpzModel.setYmlj(taskConfig.get("page-url"));
		
		return gzpzModel;
	}
}
