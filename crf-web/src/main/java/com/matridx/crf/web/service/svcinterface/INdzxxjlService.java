package com.matridx.crf.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzxxjlModel;

public interface INdzxxjlService extends BaseBasicService<NdzxxjlDto, NdzxxjlModel>{
	/**
	 * 根据患者id获取报告记录
	 * @param hzid
	 * @return
	 */
	public List<NdzxxjlDto> getNdzByHzid(String hzid) ;

}
