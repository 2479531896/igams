package com.matridx.las.home.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.home.dao.entities.SysszDto;
import com.matridx.las.home.dao.entities.SysszModel;

public interface ISysszService extends BaseBasicService<SysszDto, SysszModel>{

	/**
	 * 若实验室不存在则创建
	 * @return
	 */
	public SysszDto isExistLab();

	/**
	 * 修改实验室信息
	 * @param sysszDto
	 * @return
	 */
	public boolean labConfig(SysszDto sysszDto);

}
