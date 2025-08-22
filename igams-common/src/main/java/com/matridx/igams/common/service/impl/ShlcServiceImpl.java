package com.matridx.igams.common.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShlcModel;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.post.IShlcDao;
import com.matridx.igams.common.service.svcinterface.IShlcService;

@Service
public class ShlcServiceImpl extends BaseBasicServiceImpl<ShlcDto, ShlcModel, IShlcDao> implements IShlcService{

	/**
	 * 新增审核流程
	 */
	@Override
	public boolean insertBySpgwList(List<SpgwDto> spgwList) {
		int result = dao.insertBySpgwList(spgwList);
		return result > 0;
	}

	/**
	 * 根据审核id修改停用时间
	 */
	@Override
	public boolean updateTysjByShid(String shid) {
		int result = dao.updateTysjByShid(shid);
		return result > 0;
	}

	@Override
	public List<ShlcDto> getDtoListById(ShlcDto shlcDto) {
		return dao.getDtoListById(shlcDto);
	}
}
