package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.RwrqDto;
import com.matridx.igams.experiment.dao.entities.RwrqModel;

public interface IRwrqService extends BaseBasicService<RwrqDto, RwrqModel>{

	/**
	 * 根据任务ID以及项目阶段ID查询该任务的日期信息
	 */
	RwrqDto getRqxxByRwidAndXmjdid(RwrqDto rwrqDto);
	/**
	 * 根据日期ID进行插入或删除
	 */
	boolean insertOrUpdateRwrq(List<RwrqDto> rwrqDtos);
	/**
	 * 修改项目任务计划日期
	 */
	boolean modSaveTaskTime(RwrqDto rwrqDto);
	
		/**
	 * 根据任务ID以及项目阶段ID查询该任务的日期信息
	 */
	int modRqxxByRwidAndXmjdid(RwrqDto rwrqDto);

	/**
	 * 查询任务进度统计数据
	 */
	List<RwrqDto> getProjectProgress(RwrqDto rwrqDto);
	/**
	 * 修改阶段分数
	 */
    boolean updateJdfss(List<RwrqDto> list);
}
