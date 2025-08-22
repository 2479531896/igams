package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.RwmbDto;
import com.matridx.igams.experiment.dao.entities.RwmbModel;

public interface IRwmbService extends BaseBasicService<RwmbDto, RwmbModel>{
	/**
	 * 删除当前任务
	 */
	boolean updatescbj(RwmbDto rwmbDto);

	/**
	 * 任务模板重新排序
	 */
	boolean taskSort(RwmbDto rwmbDto);

	/**
	 * 新增模板任务
	 */
	boolean addSaveTask(RwmbDto rwmbDto);

	/**
	 * 更新模板任务
	 */
	boolean updateTaskTemplate(RwmbDto rwmbDto);
	
}
