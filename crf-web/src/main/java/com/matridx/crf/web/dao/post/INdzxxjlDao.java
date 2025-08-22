package com.matridx.crf.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzxxjlModel;

@Mapper
public interface INdzxxjlDao extends BaseBasicDao<NdzxxjlDto, NdzxxjlModel>{
	/**
	 * 根据患者id获取报告记录
	 * @param hzid
	 * @return
	 */
	public List<NdzxxjlDto> getNdzByHzid(String hzid);
}
