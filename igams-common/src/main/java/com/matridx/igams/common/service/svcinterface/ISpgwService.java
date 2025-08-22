package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwModel;

public interface ISpgwService extends BaseBasicService<SpgwDto, SpgwModel>{

	/**
	 * 获取审核流程可选岗位
	 */
	List<SpgwDto> getPagedOptionalList(SpgwDto spgwDto);
	
	/**
	 * 获取审核流程已选岗位
	 */
	List<SpgwDto> getPagedSelectedList(SpgwDto spgwDto);

	/**
	 * 更新审核流程
	 */
	boolean updateProcess(SpgwDto spgwDto);

	/**
	 * 根据ids获取审批岗位信息
	 */
	List<SpgwDto> getSpgwByIds(SpgwDto spgwDto);
	/**
	 * 批量新增审批岗位信息
	 */
	boolean insertSpgwDtos(List<SpgwDto> spgwDtos);
}
