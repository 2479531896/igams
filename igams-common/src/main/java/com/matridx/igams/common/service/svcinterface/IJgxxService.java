package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.JgxxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IJgxxService extends BaseBasicService<JgxxDto, JgxxModel>{

	/**
	 * 查询机构信息
	 */
	 List<JgxxDto> getJgxxList();

	/**
	 * 获取机构信息列表
	 */
	 List<JgxxDto> getPagedJgxxList(JgxxDto jgxxDto);
	
	/**
	 * 根据机构ID查询机构信息
	 */
	 JgxxDto selectJgxxByJgid(JgxxDto jgxxDto);
	
	/**
	 * 根据机构ID更新机构信息
	 */
	 boolean updateJgxx(JgxxDto jgxxDto);
	
	/**
	 * 删除机构信息
	 */
	 boolean deleteJgxx(JgxxDto jgxxDto);
	
	/**
	 * 根据机构名称查询机构信息
	 */
	 JgxxDto getJgxxByJgmc(JgxxDto jgxxDto);
	
	/**
	 * 查询机构列表(除本身)
	 */
	 List<JgxxDto> getPagedOtherJgxxList(JgxxDto jgxxDto);
	
	/**
	 * 通过机构名称模糊查询
	 */
	 List<JgxxDto> selsetJgxxByjgmc(JgxxDto jgxxDto);
//	/**
//	 * 更具id 更改扩展参数有2
//	 * @param jgxxDto
//	 * @return
//	 */
//	 boolean updateKzcs2ByJgid(JgxxDto jgxxDto);

	/**
	 * 查找所有机构的数据，包括删除标记为1的
	 */
	 List<JgxxDto> getAllJgxxList();

	/**
	 * 批量更新机构信息
	 */
	boolean updateJgxxList(List<JgxxDto> jgxxModList);
	/**
	 * 删除机构信息
	 */
	boolean deleteByWbcxid(JgxxDto jgxxDto);
	/*
		通过外部程序获取机构信息
	 */
    List<JgxxDto> getJgxxByWbcx(JgxxDto jgxxDto);
	/*
		递归获取子机构
	 */
	void getJgxxByFjgid(String jgid, List<JgxxDto> jgxxDtos, List<JgxxDto> zjgxxs);
	/*
		获取机构信息2列表
	 */
	List<JgxxDto> getJgxx2List(JgxxDto jgxxDto);

	/*
		获取机构信息2列表
	 */
	List<JgxxDto> getJgxx2ListAll();
	/*
		通过机构ID获取机构的第一级机构ID
	 */
	JgxxDto getFirstLevelJgidByJgxx(JgxxDto jgxxDto, List<JgxxDto> jgxxList);

	List<JgxxDto> queryByWbcx(JgxxDto jgxxDto);

	JgxxDto queryByJgxxDto(JgxxDto jgxxDto);
}
