package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShlcModel;
import com.matridx.igams.common.dao.entities.SpgwDto;

@Mapper
public interface IShlcDao extends BaseBasicDao<ShlcDto, ShlcModel>{

	/**
	 * 根据审核id修改停用时间
	 * shid
	 * 
	 */
	int updateTysjByShid(String shid);

	/**
	 * 新增审核流程
	 * spgwList
	 * 
	 */
	int insertBySpgwList(List<SpgwDto> spgwList);
	/**
	 * 根据审核id查询审核流程
	 * shlcDto
	 * 
	 */
	List<ShlcDto> getDtoListById(ShlcDto shlcDto);

}
