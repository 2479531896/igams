package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.XxpzDto;
import com.matridx.igams.wechat.dao.entities.XxpzModel;

import java.util.List;

public interface IXxpzService extends BaseBasicService<XxpzDto, XxpzModel>{

	/**
	 * 获取信息配置
	 * @param xxpzDto
	 * @return
	 */
    List<XxpzDto> getXxpzList(XxpzDto xxpzDto);

}
