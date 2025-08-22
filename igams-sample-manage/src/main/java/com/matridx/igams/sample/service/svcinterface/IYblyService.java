package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.YbglModel;
import com.matridx.igams.sample.dao.entities.YblyDto;
import com.matridx.igams.sample.dao.entities.YblyModel;

public interface IYblyService extends BaseBasicService<YblyDto, YblyModel>{
	/**
	 * 根据页面的入库类型，同时插入标本来源信息和标本管理信息
	 */
    boolean insert(YblyDto yblyDto, YbglModel ybglModel);
	
	/**
	 * 根据页面的入库类型，同时更新标本来源信息和标本管理信息
	 */
    boolean update(YblyDto yblyDto, YbglModel ybglModel);

	/**
	 * 根据标本Ids修改状态
	 */
    boolean updateZtByIds(YblyDto yblyDto);
}
