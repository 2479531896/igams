package com.matridx.igams.common.service;

import java.util.List;

/**
 * 扩展支持DTO操作的Service基类
 * @author goofus
 *
 * @param <D> DTO
 * @param <T> MODEL
 */
public interface BaseBasicService<D,T> extends BaseService<T> {
	
	/**
	 * 增加记录
	 * @param t
	 * @return
	 */
    boolean insertDto(D t);
	
	/**
	 * 修改记录
	 * @param t
	 * @return
	 */
    boolean updateDto(D t);
	
	/**
	 * 查询单条数据
	 * @param id
	 * @return
	 */
    D getDtoById(String id);
	
	/**
	 * 查询单条数据
	 * @param t
	 * @return
	 */
    D getDto(D t);
	
	/**
	 * 分页查询
	 * @param t
	 * @return
	 */
    List<D> getPagedDtoList(D t);
	
	/**
	 * 无分页查询
	 * @param t
	 * @return
	 */
    List<D> getDtoList(D t);
	
}
