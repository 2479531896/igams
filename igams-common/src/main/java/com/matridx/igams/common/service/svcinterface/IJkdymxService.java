package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.JkdymxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IJkdymxService extends BaseBasicService<JkdymxDto, JkdymxModel>{

	/**
	 * 新增接口调用信息
	 */
	boolean insertJkdymxDto(JkdymxDto jkdymxDto);

	/**
	 * 根据业务ID查询调用信息(报告)
	 */
	List<JkdymxDto> getListByYwid(String ybbh);
	/**
	 * 查询调用信息List
	 */
	List<JkdymxDto> getPageJkdymxDtoList(JkdymxDto jkdymxDto);
	List<String> getSearchItems(String key);

	/**
	 * 查询调用信息(报告)
	 * @param jkdymxDto
	 * @return
	 */
    List<JkdymxDto> selectReportInfo(JkdymxDto jkdymxDto);
}
