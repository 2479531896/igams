package com.matridx.igams.storehouse.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.DbmxDto;
import com.matridx.igams.storehouse.dao.entities.DbmxModel;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.post.IDbmxDao;
import com.matridx.igams.storehouse.service.svcinterface.IDbmxService;

@Service
public class DbmxServiceImpl extends BaseBasicServiceImpl<DbmxDto, DbmxModel, IDbmxDao> implements IDbmxService{
	/**
	 * 新增保存调拨明细信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertList(List<DbmxDto> list) {
		// TODO Auto-generated method stub
		return dao.insertList(list);
	}

	/**
	 * 根据调拨id查询调拨信息
	 */
	@Override
	public List<HwxxDto> queryByDbid(String dbid) {
		// TODO Auto-generated method stub
		return dao.queryByDbid(dbid);
	}

	/**
	 * 批量更新调拨明细
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateDbmxDtos(List<DbmxDto> dbmxDtos) {
		int result = dao.updateDbmxDtos(dbmxDtos);
		return result>0;
	}

	/**
	 * 分组查询调拨明细
	 */
	@Override
	public List<DbmxDto> queryGroupBy(String dbid) {
		// TODO Auto-generated method stub
		return dao.queryGroupBy(dbid);
	}

	@Override
	public List<DbmxDto> getDtoListByDbid(String dbid) {
		return dao.getDtoListByDbid(dbid);
	}

}
