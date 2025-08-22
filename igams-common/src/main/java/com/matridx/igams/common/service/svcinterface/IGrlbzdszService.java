package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.GrlbzdszModel;
import com.matridx.igams.common.dao.entities.LbzdszDto;

public interface IGrlbzdszService extends BaseBasicService<GrlbzdszDto, GrlbzdszModel>{
	
	/**
	 * 根据用户信息获取用户已选择的字段信息
	 */
	List<LbzdszDto> getChoseList(GrlbzdszDto grlbzdszDto);
	
	/**
	 * 保存个人选择字段信息
	 */
	boolean SaveChoseList(GrlbzdszDto grlbzdszDto);
}
