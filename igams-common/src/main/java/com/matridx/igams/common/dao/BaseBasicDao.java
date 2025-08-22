package com.matridx.igams.common.dao;

import java.util.List;

public interface BaseBasicDao<D,T> extends BaseDao<T> {
	/**
	 * 插入记录
	 * @param d
	 * @return
	 */
    int insertDto(D d);
	
	/**
	 * 更新记录
	 * @param d
	 * @return
	 */
    int updateDto(D d);
	
	/**
	 * 获取记录
	 * @param d
	 * @return
	 */
    D getDto(D d);
	
	/**
	 * 根据主键获取记录
	 * @param d
	 * @return
	 */
    D getDtoById(String id);
	
	/**
	 * 分页查询
	 * @param t
	 * @return
	 */
    List<D> getPagedDtoList(D t);
	
	/**
	 * 获取列表
	 * @param t
	 * @return
	 */
    List<D> getDtoList(D t);
	
	/**
	 * 根据用户ID获取列表
	 * @param t
	 * @return
	 */
    List<D> getDtoListById(String id);
}
