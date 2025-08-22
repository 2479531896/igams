package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.DbmxDto;
import com.matridx.igams.storehouse.dao.entities.DbmxModel;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;

public interface IDbmxService extends BaseBasicService<DbmxDto, DbmxModel>{
	/**
	 * 新增保存调拨明细信息
	 * @param list
	 * @return
	 */
	boolean insertList(List<DbmxDto> list);
	
	/**
	 * 根据调拨id查询调拨信息
	 * @return
	 */
	List<HwxxDto> queryByDbid(String dbid);
	
	/**
	 * 批量更新调拨明细
	 * @return
	 */
	boolean updateDbmxDtos(List<DbmxDto> dbmxDtos);
	
	/**
	 * 分组查询调拨明细
	 * @return
	 */
	List<DbmxDto> queryGroupBy(String dbid);

	/**
	 * 查询调拨明细
	 * @param
	 * @return
	 */
	List<DbmxDto> getDtoListByDbid(String dbid);
}
