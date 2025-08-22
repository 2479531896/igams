package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.DispatchListsDto;
import com.matridx.igams.production.dao.entities.DispatchListsModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DispatchListsDao extends BaseBasicDao<DispatchListsDto,DispatchListsModel>{
	/**
	 * 批量新增
	 * @param 
	 
	 */
	int insertListDtos(List<DispatchListsDto> dispatchListsDto);

	int updateListDtos(List<DispatchListsDto> updispatchListsDtos);
}
