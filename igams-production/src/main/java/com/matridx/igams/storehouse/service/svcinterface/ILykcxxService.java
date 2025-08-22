package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.LykcxxDto;
import com.matridx.igams.storehouse.dao.entities.LykcxxModel;

import java.util.List;

public interface ILykcxxService extends BaseBasicService<LykcxxDto, LykcxxModel>{

	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
    boolean insertList(List<LykcxxDto> list);

	/**
	 * 批量修改
	 * @param list
	 * @return
	 */
    boolean updateList(List<LykcxxDto> list);
	/**
	 * 修改库存量
	 * @param lykcxxDto
	 * @return
	 */
    boolean updateKcl(LykcxxDto lykcxxDto);
	/**
	 * 修改库存量s
	 * @param list
	 * @return
	 */
    boolean updateKclList(List<LykcxxDto> list);

	boolean updateKclListByBj(List<LykcxxDto> updateList);
	/*
		修改上次观察日期
	 */
    boolean updateScgcrq(LykcxxDto lykcxxDto);
}
