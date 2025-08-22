package com.matridx.igams.web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.WbzyDto;
import com.matridx.igams.web.dao.entities.WbzyqxDto;
import com.matridx.igams.web.dao.entities.WbzyqxModel;
import com.matridx.igams.web.dao.post.IWbzyqxDao;
import com.matridx.igams.web.service.svcinterface.IWbzyqxService;

@Service
public class WbzyqxServiceImpl extends BaseBasicServiceImpl<WbzyqxDto, WbzyqxModel, IWbzyqxDao> implements IWbzyqxService{

	/**
	 * 根据用户角色查询外部资源权限
	 * @param wbzyDto
	 * @return
	 */
	public List<WbzyqxDto> getWbzyqxList(WbzyDto wbzyDto){
		return dao.getWbzyqxList(wbzyDto);
	}
	
	/**
	 * 根据用户角色和资源ID查询外部资源权限
	 * @param wbzyDto
	 * @return
	 */
	public List<WbzyqxDto> getSecWbzyqxList(WbzyDto wbzyDto){
		return dao.getSecWbzyqxList(wbzyDto);
	}
}
