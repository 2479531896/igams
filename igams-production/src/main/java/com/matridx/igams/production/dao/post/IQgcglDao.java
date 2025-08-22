package com.matridx.igams.production.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.QgcglDto;
import com.matridx.igams.production.dao.entities.QgcglModel;

@Mapper
public interface IQgcglDao extends BaseBasicDao<QgcglDto, QgcglModel>{

	/**
	 * 查询当前用户采购车数据
	 */
	List<QgcglDto> getQgcList(QgcglDto cgcglDto);
	
	/**
	 * 根据物料编码和用户id查询购物车信息
	 */
	QgcglDto getQgcDtoByWlbmAndYhid(QgcglDto cgcglDto);
	
	/**
	 * 清空采购车
	 */
	void cleanShoppingCar(QgcglDto cgcglDto);
	
	/**
	 * 请购车列表
	 */
	List<QgcglDto> getQgcDtoList(QgcglDto cgcglDto);
}
