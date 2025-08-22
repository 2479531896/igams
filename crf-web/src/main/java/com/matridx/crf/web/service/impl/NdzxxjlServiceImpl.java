package com.matridx.crf.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzxxjlModel;
import com.matridx.crf.web.dao.post.INdzxxjlDao;
import com.matridx.crf.web.service.svcinterface.INdzxxjlService;

@Service
public class NdzxxjlServiceImpl extends BaseBasicServiceImpl<NdzxxjlDto, NdzxxjlModel, INdzxxjlDao> implements INdzxxjlService{
	@Autowired
	INdzxxjlDao iNdzxxjlDao;
	
	
	/**
	 * 根据患者id获取报告记录
	 * @param hzid
	 * @return
	 */
	@Override
	public List<NdzxxjlDto> getNdzByHzid(String hzid) {
		return iNdzxxjlDao.getNdzByHzid(hzid);
	}
}
