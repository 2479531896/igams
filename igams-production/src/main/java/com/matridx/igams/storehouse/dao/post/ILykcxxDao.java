package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.LykcxxDto;
import com.matridx.igams.storehouse.dao.entities.LykcxxModel;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ILykcxxDao extends BaseBasicDao<LykcxxDto, LykcxxModel>{

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
	/**
	 * 观察记录
	 */
	List<LykcxxDto> getDtolistGcSampleStock(LykcxxDto lykcxxDto);
	/**
	 * 取样记录
	 *
	 */
	List<LykcxxDto> getDtolistQySampleStock(LykcxxDto lykcxxDto);

    boolean updateKclListByBj(List<LykcxxDto> updateList);
	/*
		修改上次观察日期
	 */
    boolean updateScgcrq(LykcxxDto lykcxxDto);
}
