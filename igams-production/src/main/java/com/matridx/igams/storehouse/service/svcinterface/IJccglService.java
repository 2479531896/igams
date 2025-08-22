package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.JccglDto;
import com.matridx.igams.storehouse.dao.entities.JccglModel;

import java.util.List;

public interface IJccglService extends BaseBasicService<JccglDto, JccglModel>{

	/**
	 * 领料车列表
	 * @param
	 * @return
	 */
    List<JccglDto> getJccDtoList(JccglDto jccglDto);
}
