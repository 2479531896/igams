package com.matridx.igams.production.dao.matridxsql;


import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.InvPositionSumDto;
import com.matridx.igams.production.dao.entities.InvPositionSumModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvPositionSumDao extends BaseBasicDao<InvPositionSumDto,InvPositionSumModel>{
	/**
	 * 批量插入数据
	 */
	int insertDtos (List<InvPositionSumDto> InvPositionSumDtos);
	
	/**
	 * 批量更新数据
	 */
	int updateDtos (List<InvPositionSumDto> InvPositionSumDtos);
	/**
	 * 根据hwxx更改数据
	 */
	void updateByHwxx(InvPositionSumDto invPositionSumDto);
}
