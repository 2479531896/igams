package com.matridx.igams.bioinformation.service.impl;


import com.matridx.igams.bioinformation.dao.entities.WklcznDto;
import com.matridx.igams.bioinformation.dao.entities.WklcznModel;
import com.matridx.igams.bioinformation.dao.post.IWklcznDao;
import com.matridx.igams.bioinformation.service.svcinterface.IWklcznService;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WklcznServiceImpl extends BaseBasicServiceImpl<WklcznDto, WklcznModel, IWklcznDao> implements IWklcznService {

	/**
	 * 批量新增
	 */
	public boolean insertList(List<WklcznDto> list){
		return dao.insertList(list);
	}
}
