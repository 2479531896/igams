package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjbgsmDto;
import com.matridx.server.wechat.dao.entities.SjbgsmModel;

public interface ISjbgsmService extends BaseBasicService<SjbgsmDto, SjbgsmModel>{

	/**
	 * 保存送检报告说明
	 * @param sjbgsmDto
	 * @throws BusinessException 
	 */
	void receiveCommentInspection(SjbgsmDto sjbgsmDto) throws BusinessException;
	
	/**
	 * 根据送检id查询送检报告说明信息
	 * @param sjbgsmdto
	 * @return
	 */
	List<SjbgsmDto> selectSjbgBySjid(SjbgsmDto sjbgsmdto);

	/**
	 * 根据Dto删除信息
	 * @param sjbgsmDto
	 * @return
	 */
	boolean deleteBySjbgsmDto(SjbgsmDto sjbgsmDto);

}
