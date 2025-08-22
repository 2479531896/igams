package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.CwglDto;
import com.matridx.igams.wechat.dao.entities.CwglModel;

import java.util.List;

public interface ICwglService extends BaseBasicService<CwglDto, CwglModel>{

	/**
	 * 错误列表
	 */
    List<CwglDto> getPagedDtoList(CwglDto cwglDto);
	
	/**
	 * 标本量申请审核列表
	 * @param cwglDto
	 * @return
	 */
    List<CwglDto> getPagedAuditList(CwglDto cwglDto);
}
