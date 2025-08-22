package com.matridx.igams.web.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.WbzyDto;
import com.matridx.igams.web.dao.entities.WbzyqxDto;
import com.matridx.igams.web.dao.entities.WbzyqxModel;

public interface IWbzyqxService extends BaseBasicService<WbzyqxDto, WbzyqxModel>{

	/**
	 * 根据用户角色查询外部资源权限
	 * @param wbzyDto
	 * @return
	 */
    List<WbzyqxDto> getWbzyqxList(WbzyDto wbzyDto);
	
	/**
	 * 根据用户角色和资源ID查询外部资源权限
	 * @param wbzyDto
	 * @return
	 */
    List<WbzyqxDto> getSecWbzyqxList(WbzyDto wbzyDto);
}
