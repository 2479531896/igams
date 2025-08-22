package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwModel;

@Mapper
public interface ISpgwDao extends BaseBasicDao<SpgwDto, SpgwModel>{

	/**
	 * 获取审核流程可选岗位
	 * spgwDto
	 * 
	 */
	List<SpgwDto> getPagedOptionalList(SpgwDto spgwDto);
	
	/**
	 * 获取审核流程已选岗位
	 * spgwDto
	 * 
	 */
	List<SpgwDto> getPagedSelectedList(SpgwDto spgwDto);

	/**
	 * 根据ids获取审批岗位信息
	 * spgwDto
	 * 
	 */
	List<SpgwDto> getSpgwByIds(SpgwDto spgwDto);
	/**
	 * 批量新增审批岗位信息
	 * spgwDtos
	 *  boolean
	 */
	boolean insertSpgwDtos(List<SpgwDto> spgwDtos);
}
