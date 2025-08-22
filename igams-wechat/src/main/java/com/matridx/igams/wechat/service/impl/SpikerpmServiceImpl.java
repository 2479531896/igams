package com.matridx.igams.wechat.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SpikerpmDto;
import com.matridx.igams.wechat.dao.entities.SpikerpmModel;
import com.matridx.igams.wechat.dao.post.ISpikerpmDao;
import com.matridx.igams.wechat.service.svcinterface.ISpikerpmService;

@Service
public class SpikerpmServiceImpl extends BaseBasicServiceImpl<SpikerpmDto, SpikerpmModel, ISpikerpmDao> implements ISpikerpmService{

	/**
	 * 根据检测项目和标本类型获取阈值信息
	 * @param spikerpmDto
	 * @return
	 */
	@Override
	public SpikerpmDto getDtoByJcxmAndYblx(SpikerpmDto spikerpmDto) {
		return dao.getDtoByJcxmAndYblx(spikerpmDto);
	}
}
