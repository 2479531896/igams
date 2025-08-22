package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjzmjgDto;
import com.matridx.server.wechat.dao.entities.SjzmjgModel;

public interface ISjzmjgService extends BaseBasicService<SjzmjgDto, SjzmjgModel>{

	/**
	 * list新增送检自免结果
	 * @param list
	 * @return
	 */
    boolean insertSjzmjg(List<SjzmjgDto> list);
	
	/**
	 * 根据送检ID删除送检自免结果
	 * @param sjzmjgDto
	 * @return
	 */
    boolean deleteSjzmjg(SjzmjgDto sjzmjgDto);

	/**
	 * 同步修改送检自免结果
	 * @param sjzmjgDtos
	 * @throws BusinessException 
	 */
    void receiveModSelfresult(List<SjzmjgDto> sjzmjgDtos) throws BusinessException;
}
