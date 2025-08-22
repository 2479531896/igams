package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.QtckglDto;
import com.matridx.igams.storehouse.dao.entities.QtckglModel;
import com.matridx.igams.storehouse.dao.post.IQtckglDao;
import com.matridx.igams.storehouse.service.svcinterface.IQtckglService;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class QtckglServiceImpl extends BaseBasicServiceImpl<QtckglDto, QtckglModel, IQtckglDao> implements IQtckglService {

	@Override
	public boolean insertCkgls(List<QtckglDto> qtckglDtos) {
		return dao.insertQtckgls(qtckglDtos) != 0;
	}
}
