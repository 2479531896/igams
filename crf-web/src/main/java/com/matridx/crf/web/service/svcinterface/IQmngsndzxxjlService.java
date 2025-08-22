package com.matridx.crf.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.Map;

import com.matridx.crf.web.dao.entities.QmngsndzxxjlDto;
import com.matridx.crf.web.dao.entities.QmngsndzxxjlModel;

public interface IQmngsndzxxjlService extends BaseBasicService<QmngsndzxxjlDto, QmngsndzxxjlModel>{
	/**
	 * 获取患者记录
	 * 
	 * @return
	 */
	public Map<String,Object> getHzjl(String hzid);
}
