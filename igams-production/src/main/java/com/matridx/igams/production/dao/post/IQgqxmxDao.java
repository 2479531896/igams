package com.matridx.igams.production.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.QgqxglDto;
import com.matridx.igams.production.dao.entities.QgqxmxDto;
import com.matridx.igams.production.dao.entities.QgqxmxModel;

@Mapper
public interface IQgqxmxDao extends BaseBasicDao<QgqxmxDto, QgqxmxModel>{

	/**
	 * 新增请购取消明细
	 */
	boolean insertDtoList(List<QgqxmxDto> list);
	
	/**
	 * 请购取消明细列表
	 */
	List<QgqxmxDto> getQgqxmxList(QgqxmxDto qgqxmxDto);

	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(QgqxmxDto qgqxmxDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<QgqxmxDto> getListForSearchExp(QgqxmxDto qgqxmxDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<QgqxmxDto> getListForSelectExp(QgqxmxDto qgqxmxDto);

	/**
	 * 取消请购页面明细列表
	 */
	List<QgmxDto> getQxqgmxList(QgmxDto qgmxDto);
	
	/**
	 * 取消请购审核通过更新请购明细sl，sysl字段
	 */
	boolean updateQgmxByList(QgqxglDto qgqxglDto);
	
	/**
	 * 更新请购取消明细数据
	 */
	boolean updateDtoList(List<QgqxmxDto> list);
	
	/**
	 * 获取请购明细的以及相应的取消数量信息
	 */
	List<QgqxmxDto> getQgqxmxCancelList(QgqxmxDto qgqxmxDto);
}
