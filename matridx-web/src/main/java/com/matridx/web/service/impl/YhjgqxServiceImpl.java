package com.matridx.web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.web.dao.entities.YhjgqxDto;
import com.matridx.web.dao.entities.YhjgqxModel;
import com.matridx.web.dao.post.IYhjgqxDao;
import com.matridx.web.service.svcinterface.IYhjgqxService;

@Service
public class YhjgqxServiceImpl extends BaseBasicServiceImpl<YhjgqxDto, YhjgqxModel, IYhjgqxDao> implements IYhjgqxService{

	@Override
	public List<YhjgqxDto> getYhjgqxList() {
		return dao.getYhjgqxList();
	}

}
