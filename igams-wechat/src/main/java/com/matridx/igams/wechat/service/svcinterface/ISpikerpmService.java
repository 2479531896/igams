package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SpikerpmDto;
import com.matridx.igams.wechat.dao.entities.SpikerpmModel;

public interface ISpikerpmService extends BaseBasicService<SpikerpmDto, SpikerpmModel>{

	/**
	 * 根据检测项目和标本类型获取阈值信息
	 * @param spikerpmDto
	 * @return
	 */
    SpikerpmDto getDtoByJcxmAndYblx(SpikerpmDto spikerpmDto);
}
