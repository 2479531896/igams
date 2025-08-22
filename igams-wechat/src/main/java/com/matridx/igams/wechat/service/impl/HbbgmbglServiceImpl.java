package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.HbbgmbglDto;
import com.matridx.igams.wechat.dao.entities.HbbgmbglModel;
import com.matridx.igams.wechat.dao.post.IHbbgmbglDao;
import com.matridx.igams.wechat.service.svcinterface.IHbbgmbglService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HbbgmbglServiceImpl extends BaseBasicServiceImpl<HbbgmbglDto, HbbgmbglModel, IHbbgmbglDao> implements IHbbgmbglService {
	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	public boolean insertList(List<HbbgmbglDto> list){
		return dao.insertList(list);
	}

}
