package com.matridx.server.wechat.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjwzxxDto;
import com.matridx.server.wechat.dao.entities.SjwzxxModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

public interface ISjwzxxService extends BaseBasicService<SjwzxxDto, SjwzxxModel>{

	/**
	 * 批量新增送检物种信息
	 * @param sjwzxxDtos
	 * @return
	 */
	boolean insertBysjwzxxDtos(List<SjwzxxDto> sjwzxxDtos);

	/**
	 * 保存本地送检结果至服务器
	 * @param map
	 * @throws BusinessException 
	 */
	void receiveResultInspection(Map<String,Object> map) throws BusinessException;

	/**
	 * 批量删除送检物种信息
	 * @param sjwzxxDto
	 * @return
	 */
	boolean deleteBysjwzxxDto(SjwzxxDto sjwzxxDto);

	/**
	 * 送检物种类型统计(医生)
	 * @param sjxxDto
	 * @return
	 */
    List<SjwzxxDto> getSpeciesStatis(SjxxDto sjxxDto);

}
