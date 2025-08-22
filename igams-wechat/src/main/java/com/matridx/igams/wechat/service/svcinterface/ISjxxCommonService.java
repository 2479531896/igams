package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.List;
import java.util.Map;

public interface ISjxxCommonService {

	/**
	 * 发送消息公用方法
	 * @param sjxxDto
	 * @param map
	 * @return
	 * @throws BusinessException 
	 */
	boolean sendMessage(SjxxDto sjxxDto, Map<String, Object> map) throws BusinessException;
	
	/**
	 * 获取设置X项目的伙伴信息
	 * @param sjxxDto
	 * @return
	 */
    SjhbxxDto getHbDtoFromId(SjxxDto sjxxDto);

    void sendMessageThread(SjxxDto t_sjxxDto, Map<String, Object> map);

	List<SjhbxxDto> getHbxzDtoByHbmc(String hbmc);
}
