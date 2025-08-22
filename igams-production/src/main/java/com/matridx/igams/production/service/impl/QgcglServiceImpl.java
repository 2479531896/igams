package com.matridx.igams.production.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.QgcglDto;
import com.matridx.igams.production.dao.entities.QgcglModel;
import com.matridx.igams.production.dao.post.IQgcglDao;
import com.matridx.igams.production.service.svcinterface.IQgcglService;

@Service
public class QgcglServiceImpl extends BaseBasicServiceImpl<QgcglDto, QgcglModel, IQgcglDao> implements IQgcglService{

	/**
	 * 查询当前用户采购车数据
	 */
	public List<QgcglDto> getQgcList(QgcglDto qgcglDto){
		return dao.getQgcList(qgcglDto);
	}
	
	/**
	 * 根据物料编码和用户id查询购物车信息
	 */
	public QgcglDto getQgcDtoByWlbmAndYhid(QgcglDto qgcglDto) {
		return dao.getQgcDtoByWlbmAndYhid(qgcglDto);
	}
	
	/**
	 * 清空采购车
	 */
	public void cleanShoppingCar(QgcglDto qgcglDto) {
        dao.cleanShoppingCar(qgcglDto);
    }
	
	/**
	 * 请购车列表
	 */
	public List<QgcglDto> getQgcDtoList(QgcglDto qgcglDto){
		return dao.getQgcDtoList(qgcglDto);
	}
}
