package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.LsbDto;
import com.matridx.igams.wechat.dao.entities.LsbModel;

public interface ILsbService extends BaseBasicService<LsbDto, LsbModel>{

	/**
	 * 增加记录
	 * @return
	 */
    boolean getInfoFromSite();
}
