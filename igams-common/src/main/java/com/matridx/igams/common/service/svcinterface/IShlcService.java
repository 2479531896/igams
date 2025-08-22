package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShlcModel;
import com.matridx.igams.common.dao.entities.SpgwDto;

public interface IShlcService extends BaseBasicService<ShlcDto, ShlcModel>{

	/**
	 * 新增审核流程
	 */
	boolean insertBySpgwList(List<SpgwDto> spgwList);

	/**
	 * 根据审核id修改停用时间
	 */
	boolean updateTysjByShid(String shid);
	/**
	 * 根据审核id查询审核流程
	 */
    List<ShlcDto> getDtoListById(ShlcDto shlcDto);
}
