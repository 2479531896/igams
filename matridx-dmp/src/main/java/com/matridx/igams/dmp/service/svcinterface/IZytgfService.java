package com.matridx.igams.dmp.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.dmp.dao.entities.ZytgfDto;
import com.matridx.igams.dmp.dao.entities.ZytgfModel;

public interface IZytgfService extends BaseBasicService<ZytgfDto, ZytgfModel>{

	/**
	 * 获取资源提供方信息
	 * @return
	 */
	List<ZytgfDto> getZytgfDtoList();

	/**
	 * 新增资源提供方
	 * @param zytgfDto
	 * @return
	 */
	boolean addSaveProvider(ZytgfDto zytgfDto);

}
