package com.matridx.igams.production.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.QgcglDto;
import com.matridx.igams.production.dao.entities.QgcglModel;

public interface IQgcglService extends BaseBasicService<QgcglDto, QgcglModel>{

	/**
	 * 查询当前用户请购车数据
	 */
	List<QgcglDto> getQgcList(QgcglDto qgcglDto);
	
	/**
	 * 根据物料编码和用户id查询购物车信息
	 */
	QgcglDto getQgcDtoByWlbmAndYhid(QgcglDto qgcglDto);
	
	/**
	 * 清空请购车
	 */
	void cleanShoppingCar(QgcglDto qgcglDto);
	
	/**
	 * 请购车列表
	 */
	List<QgcglDto> getQgcDtoList(QgcglDto cgcglDto);
}
