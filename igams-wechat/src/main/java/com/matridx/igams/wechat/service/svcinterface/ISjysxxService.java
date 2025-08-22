package com.matridx.igams.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjysxxDto;
import com.matridx.igams.wechat.dao.entities.SjysxxModel;

public interface ISjysxxService extends BaseBasicService<SjysxxDto, SjysxxModel>{

	/**
	 * 根据送检信息新增医生信息
	 * @param sjxxDto
	 * @return
	 */
	boolean insertBySjxxDto(SjxxDto sjxxDto);
	
	/**
	 * 根据送检医生查询医生信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjysxxDto> selectSjysxxDtoBySjys(SjxxDto sjxxDto);

}
