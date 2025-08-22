package com.matridx.igams.experiment.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.RwrqDto;
import com.matridx.igams.experiment.dao.entities.RwrqModel;

@Mapper
public interface IRwrqDao extends BaseBasicDao<RwrqDto, RwrqModel>{

	/**
	 * 根据任务ID以及项目阶段ID查询该任务的日期信息
	 */
	RwrqDto getRqxxByRwidAndXmjdid(RwrqDto rwrqDto);
	/**
	 * 根据日期ID进行插入或删除
	 */
	int insertOrUpdateRwrq(List<RwrqDto> rwrqDtos);

	/**
	 *
	 * 根据用户部门查询数据
	 */
	List<RwrqDto> getInfoListBySj(RwrqDto rwrqDto);


	/**
	 *
	 * 根据用户部门查询数据
	 */
	List<RwrqDto> getInfoListByJh(RwrqDto rwrqDto);
	
	/**
	 * 根据任务ID以及项目阶段ID查询该任务的日期信息
	 */
	int modRqxxByRwidAndXmjdid(RwrqDto rwrqDto);
	/**
	 * 根据日期ID进行插入或删除(计划日期修改)
	 */
	int insertOrUpdateJhrq(List<RwrqDto> rwrqDtos);
	/**
	 * 修改阶段分数
	 */
	boolean updateJdfss(List<RwrqDto> list);
}
