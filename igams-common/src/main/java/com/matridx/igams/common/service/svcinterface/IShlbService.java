package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.dao.entities.ShlbModel;

public interface IShlbService extends BaseBasicService<ShlbDto, ShlbModel>{

	/**
	 * 根据审核类别获取审核类别信息
	 */
	 ShlbDto getShlbxxByShlb(ShlbDto shlbDto) ;
	
	/**
	 * 根据ids查询审核类别
	 */
	 List<ShlbDto> getShlbxxByIds(ShlbDto shlbDto);
	
	/**
	 * 查询审核类别的所有信息
	 */
	 List<ShlbDto> getShlbAllData();
	/*
		获取首页的所有审核类别
	 */
    List<ShlbDto> getShlbForHomePage();
}
