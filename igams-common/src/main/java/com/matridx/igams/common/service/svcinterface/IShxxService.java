package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.ShxxModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IShxxService extends BaseBasicService<ShxxDto, ShxxModel>{
	/**
	 * 获得全部审核信息，按审核时间倒排序
	 */
	 List<ShxxDto> getShxxOrderByShsj(ShxxDto dto);

	/**
	 * 根据过程ID获取审核信息
	 */
	 List<ShxxDto> getShxxOrderByGcid(ShxxDto shxxDto);

	/**
	 * 查询业务审核过程
	 */
	 List<ShxxDto> getShxxOrderByPass(ShxxDto shxxParam);

	/**
	 * 审核信息修改
	 */
	 boolean auditmodSaveTime(ShxxDto shxxDto);

	/**
	 * 获取最新的审核通过的信息
	 */
	 List<ShxxDto> getPassShxxBestNew(ShxxDto shxxDto);

	/**
	 * 新冠获取提交信息
	 */
	 ShxxDto getPassShxxBestSqr(ShxxDto shxxDto);
	/**
	 * 获取通过信息
	 */
	List<ShxxDto> getShxxByYwidsTG(ShxxDto shxxDto);
	/**
	 * 获取提交信息
	 */
	List<ShxxDto> getShxxByYwidsTj(ShxxDto shxxDto);
	/**
	 * 根据lcxh分组获取审核信息
	 */
	List<ShxxDto> getShxxGroupByLcxh(ShxxDto shxxDto);
	ShxxDto getShxxByYwid(ShxxDto shxxDto);

	List<ShxxDto> getShxxLsit(ShxxDto shxxDto);

	ShxxDto queryShsj(ShxxDto shxxDto);
}
