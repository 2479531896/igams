package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdspglModel;

@Mapper
public interface IDdspglDao extends BaseBasicDao<DdspglDto, DdspglModel>{

	/**
	 * 更新钉钉审批信息
	 */
	int insertOrUpdateDdspgl(DdspglDto ddspglDto);
	
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
