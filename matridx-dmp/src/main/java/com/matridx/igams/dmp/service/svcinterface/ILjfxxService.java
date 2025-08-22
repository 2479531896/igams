package com.matridx.igams.dmp.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.dmp.dao.entities.LjfxxDto;
import com.matridx.igams.dmp.dao.entities.LjfxxModel;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;

public interface ILjfxxService extends BaseBasicService<LjfxxDto, LjfxxModel>{

	/**
	 * 根据资源信息Dto新增连接方信息
	 * @param zyxxDto
	 * @return
	 */
	boolean insertLjfxxDtoByZyxxDto(ZyxxDto zyxxDto);

}
