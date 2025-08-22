package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.QtckglDto;
import com.matridx.igams.storehouse.dao.entities.QtckglModel;

import java.util.List;

public interface IQtckglService extends BaseBasicService<QtckglDto, QtckglModel>{

	/**
	 *  批量插入数据
	 * @param
	 * @return
	 */
	boolean insertCkgls(List<QtckglDto> qtckglDtos);
}
