package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.RdRecordDto;
import com.matridx.igams.production.dao.entities.RdRecordModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RdRecord11Dao extends BaseBasicDao<RdRecordDto,RdRecordModel>{
	/**
	 * 批量新增出库
	 */
	int insertList(List<RdRecordDto> list);
	
	/**
	 * 查询出库
	 */
	List<RdRecordDto> queryRd(RdRecordDto rdRecordDto);
}
