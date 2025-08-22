package com.matridx.igams.research.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.research.dao.entities.YfglDto;
import com.matridx.igams.research.dao.entities.YfglModel;

public interface IYfglService extends BaseBasicService<YfglDto, YfglModel>{

	/**
	 * 新增研发信息
	 */
	boolean addSaveResearch(YfglDto yfglDto);

	/**
	 * 根据研发ID查询附件表信息
	 */
	List<FjcfbDto> selectFjcfbDtoByYfid(String yfid);

}
