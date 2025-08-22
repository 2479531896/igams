package com.matridx.server.wechat.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.HbsfbzDto;
import com.matridx.server.wechat.dao.entities.HbsfbzModel;
import com.matridx.server.wechat.dao.post.IHbsfbzDao;
import com.matridx.server.wechat.service.svcinterface.IHbsfbzService;

import java.util.List;

@Service
public class HbsfbzServiceImpl extends BaseBasicServiceImpl<HbsfbzDto, HbsfbzModel, IHbsfbzDao> implements IHbsfbzService{
	
	/**
	 * 获取默认收费标准
	 * @param hbsfbzDto
	 * @return
	 */
	public HbsfbzDto getDefaultDto(HbsfbzDto hbsfbzDto) {
		return dao.getDefaultDto(hbsfbzDto);
	}

	/**
	 * 添加收费标准
	 * @param
	 * @return
	 */
	@Override
	public boolean insertsfbz(List<HbsfbzDto> hbsfbzDto){
		// TODO Auto-generated method stub
		return dao.insertsfbz(hbsfbzDto);
	}
	@Override
	public Boolean updateList(List<HbsfbzDto> list) {
		return dao.updateList(list);
	}

	@Override
	public boolean batchModSfbz(List<HbsfbzDto> list) {
		return dao.batchModSfbz(list);
	}
}
