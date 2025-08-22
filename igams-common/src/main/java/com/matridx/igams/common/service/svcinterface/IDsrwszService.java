package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.LscxszDto;
import com.matridx.igams.common.service.BaseBasicService;

import com.matridx.igams.common.dao.entities.DsrwszDto;
import com.matridx.igams.common.dao.entities.DsrwszModel;

import java.util.List;

public interface IDsrwszService extends BaseBasicService<DsrwszDto, DsrwszModel>{

	
	/**
	 * 根据任务id获取定时任务信息
	 */
	DsrwszDto selectDsrwxxByRwid(DsrwszDto dsrwszdto);
	
	/**
	 * 更新定时任务信息
	 */
	boolean updateDsxx(DsrwszDto dsrwszdto);
	
	/**
	 * 通过id删除定时任务信息
	 */
	boolean deleteByRwid(DsrwszDto dsrwszdto);

	/**
	 * 通过执行类和执行方法
	 */
    DsrwszDto getDsrwByZxlAndZxff(DsrwszDto dsrwszDto);
	/**
	 * 通过执行类和执行方法更新书
	 */
	boolean updateByClassAndName(DsrwszDto dsrwszDto);


	/**
	 * 获取查询区分不是LIMIT和STATISTICS的查询
	 */
	boolean queryByRwid(DsrwszDto dsrwszDto);

	/**
	 * 获取限制的查询结果
	 *
	 * dsrwszDto
	 *
	 */
	List<DsrwszDto> getDsrwListByLimt(DsrwszDto dsrwszDto);
}
