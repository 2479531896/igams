package com.matridx.web.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.web.dao.entities.YhjgqxDto;
import com.matridx.web.dao.entities.YhjgqxModel;

public interface IYhjgqxService extends BaseBasicService<YhjgqxDto, YhjgqxModel>{

	/**
	 * 查询用户机构权限
	 * @return
	 */
    List<YhjgqxDto> getYhjgqxList();

}
