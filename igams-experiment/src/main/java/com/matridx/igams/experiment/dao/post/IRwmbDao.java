package com.matridx.igams.experiment.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.RwmbDto;
import com.matridx.igams.experiment.dao.entities.RwmbModel;

@Mapper
public interface IRwmbDao extends BaseBasicDao<RwmbDto, RwmbModel>{
	
	/**
	 * 查询当前阶段序号
	 */
	int getrwmbxh(RwmbDto rwmbDto);
	
	/**
	 * 查询所有任务
	 */
	List<RwmbDto> getrwmbbyid(String jdid);
	
	/**
	 * 删除当前任务
	 */
	boolean updatescbj(RwmbDto rwmbDto);
	
	/**
	 * 查询子任务条数
	 */
	int getcount(String rwid);

	/**
	 * 修改任务模板阶段
	 */
	int updateJdByRwmbid(RwmbDto rwmbDto);

	/**
	 * 任务模板重新排序
	 */
	int taskSort(RwmbDto rwmbDto);
}
