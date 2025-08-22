package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.SpgwcyModel;

@Mapper
public interface ISpgwcyDao extends BaseBasicDao<SpgwcyDto, SpgwcyModel>{

	/**
	 * 获取岗位可选成员
	 * spgwcyDto
	 * 
	 */
	 List<SpgwcyDto> getPagedOptionalList(SpgwcyDto spgwcyDto);

	/**
	 * 获取岗位已选成员
	 * spgwcyDto
	 * 
	 */
	 List<SpgwcyDto> getPagedSelectedList(SpgwcyDto spgwcyDto);

	/**
	 * 获取直属主管
	 * 
	 * 
	 */
	 SpgwcyDto getZgByDto(SpgwcyDto spgwcyDto);

	/**
	 * 获取退回用户
	 * spgwcyDto
	 * 
	 */
	 SpgwcyDto getThByDto(SpgwcyDto spgwcyDto);

	/**
	 * 添加岗位成员
	 * spgwcyList
	 * 
	 */
	 boolean toSelected(List<SpgwcyDto> spgwcyList);

	/**
	 * 去除岗位成员
	 * spgwcyList
	 * 
	 */
	 boolean toOptional(List<SpgwcyDto> spgwcyList);

	/**
	 * 通过用户id删除岗位审批
	 * yhid
	 * 
	 */
	 boolean deleteByYhid(String yhid);
	/**
	 * 获取审批岗位成员单位限制
	 * spgwcyDto
	 * 
	 */
    List<SpgwcyDto> getDtoListWithDW(SpgwcyDto spgwcyDto);
}
