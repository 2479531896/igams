package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdspglModel;

public interface IDdspglService extends BaseBasicService<DdspglDto, DdspglModel>{

	/**
	 * 更新钉钉审批信息
	 */
	DdspglDto insertInfo(JSONObject obj);

	
	/**
	 * 更新钉钉审批处理结果
	 */
	boolean updatecljg(DdspglDto ddspglDto);
	
	/**
	 * 根据类型，钉钉实例ID批量更新钉钉审批管理信息
	 */
	boolean updateAll(DdspglDto ddspglDto);
	
	/**
	 * 获取完成状态的钉钉审批管理事件
	 */
	DdspglDto getFinishInstenceSpgl(DdspglDto ddspglDto);
}
