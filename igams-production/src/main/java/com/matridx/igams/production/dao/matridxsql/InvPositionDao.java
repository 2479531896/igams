package com.matridx.igams.production.dao.matridxsql;


import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.InvPositionDto;
import com.matridx.igams.production.dao.entities.InvPositionModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvPositionDao extends BaseBasicDao<InvPositionDto,InvPositionModel>{
	/**
	 * 批量插入数据
	 */
	int insertDtos (List<InvPositionDto> InvPositionDtos);

	/**
	 * 通过RdsID更改
	 */
	void updateByRdsID(InvPositionDto invPositionDto);
	/**
	 * 通过RdsID更改
	 */
	void updateByRDIDAndRDSID(InvPositionDto invPositionDto);
}
