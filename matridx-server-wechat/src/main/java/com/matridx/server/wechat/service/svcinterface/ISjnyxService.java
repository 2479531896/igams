package com.matridx.server.wechat.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjnyxDto;
import com.matridx.server.wechat.dao.entities.SjnyxModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

public interface ISjnyxService extends BaseBasicService<SjnyxDto, SjnyxModel>{

	/**
	 * 保存本地送检耐药性至服务器
	 * @param map
	 * @throws BusinessException 
	 */
	void receiveResistanceInspection(Map<String,Object> map) throws BusinessException;

	/**
	 * 根据送检ID查询耐药信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjnyxDto> getNyxBySjid(SjxxDto sjxxDto);

	/**
	 * 根据Dto删除耐药信息
	 * @param sjnyxDto
	 * @return
	 */
	boolean deleteBySjnyxDto(SjnyxDto sjnyxDto);
}
