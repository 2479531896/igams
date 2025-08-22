package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.igams.common.dao.entities.SyxxModel;

public interface ISyxxService extends BaseBasicService<SyxxDto, SyxxModel>{

	/**
	 * 根据文件类型查询水印信息
	 */
	SyxxDto getDtoByWjlb(String wjlb);
}
