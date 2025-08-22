package com.matridx.web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.web.dao.entities.YhssjgDto;
import com.matridx.web.dao.entities.YhssjgModel;
import com.matridx.web.dao.post.IYhssjgDao;
import com.matridx.web.service.svcinterface.IYhssjgService;

@Service
public class YhssjgServiceImpl extends BaseBasicServiceImpl<YhssjgDto, YhssjgModel, IYhssjgDao> implements IYhssjgService{

	@Override
	public List<YhssjgDto> getYhssjgList() {
		return dao.getYhssjgList();
	}

}
