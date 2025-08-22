package com.matridx.igams.dmp.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.dmp.dao.entities.SjkxxDto;
import com.matridx.igams.dmp.dao.entities.SjkxxModel;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;

public interface ISjkxxService extends BaseBasicService<SjkxxDto, SjkxxModel>{

	/**
	 * 查询数据库信息
	 * @return
	 */
	List<SjkxxDto> getSjkxxDtoList();

	/**
	 * 根据资源信息Dto新增数据库信息
	 * @param zyxxDto
	 * @return
	 */
	boolean insertSjkxxDtoByZyxxDto(ZyxxDto zyxxDto);

}
