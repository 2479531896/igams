package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjkjxxDto;
import com.matridx.igams.wechat.dao.entities.SjkjxxModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

public interface ISjkjxxService extends BaseBasicService<SjkjxxDto, SjkjxxModel>{

	/**
	 * 根据送检信息新增送检快捷信息
	 * @param sjxxDto
	 * @return
	 */
	boolean inserBySjxxDto(SjxxDto sjxxDto);

}
