package com.matridx.las.home.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.home.dao.entities.SysszDto;
import com.matridx.las.home.dao.entities.YqxxDto;
import com.matridx.las.home.dao.entities.YqxxModel;

public interface IYqxxService extends BaseBasicService<YqxxDto, YqxxModel>{

	/**
	 * 新增仪器信息
	 * @param yqxxDto
	 * @return
	 */
	boolean addSaveDevice(YqxxDto yqxxDto);

	/**
	 * 根据实验室查询仪器信息
	 * @param sysszDto
	 * @return
	 */
	List<YqxxDto> getListByLab(SysszDto sysszDto);

	/**
	 * 修改仪器信息
	 * @param yqxxDto
	 * @return
	 */
	boolean modSaveDevice(YqxxDto yqxxDto);

}
