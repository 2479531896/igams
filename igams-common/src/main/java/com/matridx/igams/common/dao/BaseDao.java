package com.matridx.igams.common.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao<T> {
	/**
	 * 往数据库插入信息
	 * @param t 插入的信息
	 * @return
	 */
    int insert(T t);
	
	/**
	 * 批量插入数据
	 * @param list
	 * @return
	 */
    int batchInsert(List<T> list);
	
	/**
	 * 根据主键对数据库的信息进行更新
	 * @param t
	 * @return
	 */
    int update(T t);
	
	/**
	 * 根据信息删除数据库的记录
	 * @param t
	 * @return
	 */
    int delete(T t);
	
	/**
	 * 根据主键删除数据库的信息
	 * @param t
	 * @return
	 */
    int deleteById(String id);
	
	/**
	 * 根据list删除记录
	 * @param list
	 * @return
	 */
    int batchDelete(List<String> list);
	
	/**
	 * 批量删除
	 * @param map
	 * @return
	 */
    int batchDeleteByMap(Map<String, Object> map);
	
	/**
	 * 根据主键获取记录
	 * @param id
	 * @return
	 */
    T getModelById(String id);
	
	/**
	 * 根据参数获取记录
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
	 * 获取所有信息
	 * @param t
	 * @return
	 */
    List<T> getModelList(T t);
	
	/**
	 * 获取记录数
	 * @param t
	 * @return
	 */
    int getCount(T t);
}
