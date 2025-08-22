package com.matridx.igams.bioinformation.service.impl;


import com.matridx.igams.bioinformation.dao.entities.WkysmxDto;
import com.matridx.igams.bioinformation.dao.entities.WkysmxModel;
import com.matridx.igams.bioinformation.dao.post.IWkysmxDao;
import com.matridx.igams.bioinformation.service.svcinterface.IWkysmxService;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WkysmxServiceImpl extends BaseBasicServiceImpl<WkysmxDto, WkysmxModel, IWkysmxDao> implements IWkysmxService {

	/**
	 * 批量新增
	 */
	public boolean insertList(List<WkysmxDto> list){
		return dao.insertList(list);
	}

	@Override
	public List<WkysmxDto> getList(WkysmxDto wkysmxDto) {
		return dao.getList(wkysmxDto);
	}

}
