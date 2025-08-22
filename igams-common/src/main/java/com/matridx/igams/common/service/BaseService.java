package com.matridx.igams.common.service;

import java.util.List;
import java.util.Map;

/**
 * 类名称：通用Service接口
 * 创建时间:2012-5-3
 * 修改人：ZhenFei.Cao
 * 修改时间：2012-8-2
 */
public interface BaseService<T> {
	/**
	 * 增加记录
	 * @param t
	 * @return
	 */
    boolean insert(T t);
	
	/**
	 * 批量插入
	 * @param map
	 * @return
	 */
    boolean batchInsert(List<T> list);
	
	/**
	 * 修改记录
	 * @param t
	 * @return
	 */
    boolean update(T t);
	
	/**
	 * 根据信息删除数据库的记录
	 * @param t
	 * @return
	 */
    boolean delete(T t);
	
	/**
	 * 根据主键删除数据库的信息
	 * @param t
	 * @return
	 */
    boolean deleteById(String id);
	
	/**
	 * 批量删除
	 * @param map
	 * @return
	 */
    boolean batchDeleteByMap(Map<String, Object> map);
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 */
    boolean batchDelete(List<String> list);
	
	/**
	 * 查询单条数据
	 * @param id
	 * @return
	 */
    T getModelById(String id);
	
	/**
	 * 查询单条数据
	 * @param t
	 * @return
	 */
    T getModel(T t);

	
	/**
	 * 分页查询
	 * @param t
	 * @return
	 */
    List<T> getPagedList(T t);
	
	
	/**
	 * 无分页查询
	 * @param t
	 * @return
	 */
    List<T> getModelList(T t);
	
	/**
	 * 统计记录数
	 * @param t
	 * @return
	 */
    int getCount(T t);
	
}
