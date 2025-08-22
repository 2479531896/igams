package com.matridx.igams.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.JsjcdwDto;
import com.matridx.igams.web.dao.entities.JsjcdwModel;
import com.matridx.igams.web.dao.entities.XtjsDto;

public interface IJsjcdwService extends BaseBasicService<JsjcdwDto, JsjcdwModel>{

	/**
	 * 新增角色检测单位
	 * @param xtjsDto
	 * @return
	 */
	boolean insertJsjcdw(XtjsDto xtjsDto);
}
