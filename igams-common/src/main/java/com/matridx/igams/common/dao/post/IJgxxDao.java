package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.JgxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IJgxxDao extends BaseBasicDao<JgxxDto, JgxxModel>{

	/**
	 * 查询机构信息
	 * 
	 */
	 List<JgxxDto> getJgxxList();

	/**
	 * 获取机构信息列表
	 * jgxxDto
	 * 
	 */
	 List<JgxxDto> getPagedJgxxList(JgxxDto jgxxDto);
	
	/**
	 * 根据机构ID查询机构信息
	 * jgxxDto
	 * 
	 */
	 JgxxDto selectJgxxByJgid(JgxxDto jgxxDto);
	
	/**
	 * 根据机构ID更新机构信息
	 * jgxxDto
	 * 
	 */
	 int updateJgxx(JgxxDto jgxxDto);
	
	/**
	 * 删除机构信息
	 * jgxxDto
	 * 
	 */
	 int deleteJgxx(JgxxDto jgxxDto);
	
	/**
	 * 根据机构名称查询机构信息
	 * jgxxDto
	 * 
	 */
	 JgxxDto getJgxxByJgmc(JgxxDto jgxxDto);
	
	/**
	 * 查询机构列表(除本身)
	 * jgxxDto
	 * 
	 */
	 List<JgxxDto> getPagedOtherJgxxList(JgxxDto jgxxDto);
	
	/**
	 * 通过机构名称模糊查询
	 * jgxxDto
	 * 
	 */
	 List<JgxxDto> selsetJgxxByjgmc(JgxxDto jgxxDto);
	/**
	 * 更具id 更改扩展参数有2
	 * jgxxDto
	 * 
	 */
	 Integer updateKzcs2ByJgid(JgxxDto jgxxDto);

	/**
	 * 查找所有机构的数据，包括删除标记为1的
	 * 
	 */
	 List<JgxxDto> getAllJgxxList();

	/**
	 * 批量更新机构信息
	 * jgxxModList
	 * 
	 */
	boolean updateJgxxList(List<JgxxDto> jgxxModList);
	/**
	 * 删除机构信息
	 * jgxxDto
	 * 
	 */
	boolean deleteByWbcxid(JgxxDto jgxxDto);
	/*
    	通过外部程序获取机构信息
 	*/
	List<JgxxDto> getJgxxByWbcx(JgxxDto jgxxDto);
	/*
		获取机构信息2列表
	 */
	List<JgxxDto> getJgxx2List(JgxxDto jgxxDto);
	/*
		获取机构信息2列表
	 */
	List<JgxxDto> getJgxx2ListAll();

	List<JgxxDto> queryByWbcx(JgxxDto jgxxDto);
	JgxxDto queryByJgxxDto(JgxxDto jgxxDto);
}
