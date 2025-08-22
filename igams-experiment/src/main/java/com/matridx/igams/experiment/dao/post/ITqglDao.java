package com.matridx.igams.experiment.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.TqglDto;
import com.matridx.igams.experiment.dao.entities.TqglModel;

@Mapper
public interface ITqglDao extends BaseBasicDao<TqglDto, TqglModel>{

	/**
	 * 获取提取管理列表
	 */
	List<TqglDto> getPagedTqglDtoList(TqglDto tqglDto);
	
	/**
	 * 更新提取管理信息
	 */
	boolean updateTqgl(TqglDto tqglDto);
	
	/**
	 * 删除提取管理信息
	 */
	boolean deleteTqgl(TqglDto tqglDto);
	
	/**
	 * 根据名称获取提取管理信息
	 */
	List<TqglDto> getTqglByMc(TqglDto tqglDto);
	
	/**
	 * 获取最大序号
	 */
	int getXh(TqglDto tqglDto);

	List<Map<String,String>> getJsjcdwByjsid(String jsid);
	
	/**
	 * 删除临时保存的提取管理信息
	 */
	boolean deleteLs(String tqid);
}
