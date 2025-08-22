package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.DispatchListDto;
import com.matridx.igams.production.dao.entities.DispatchListModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DispatchListDao extends BaseBasicDao<DispatchListDto,DispatchListModel>{
	/**
	 * 通过单号查询
	 * @param 
	 
	 */
    DispatchListDto getDtoByDh(DispatchListDto dispatchListDto);
	
	/**
	 * 通过单号查询
	 * @param 
	 
	 */
    List<DispatchListDto> getcCode(DispatchListDto dispatchListDto);

	DispatchListDto getcDLCode(DispatchListDto dispatchListDto1);

}
