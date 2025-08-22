package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.RdRecordsDto;
import com.matridx.igams.production.dao.entities.RdRecordsModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RdRecords11Dao extends BaseBasicDao<RdRecordsDto,RdRecordsModel>{
	
	/**
	 * 批量更新出库明细
	 */
	int insertList(List<RdRecordsDto> list);
	/**
	 * 更具auotoid查
	 */
	RdRecordsDto getDtoByAutoId(RdRecordsDto rdRecordsDto);

	/**
	 * 批量更新出库红冲明细
	 */
	int insertCKHCList(List<RdRecordsDto> list);
	/**
	 * 批量更新委外出库明细
	 */
	int insertWWCKList(List<RdRecordsDto> list);
}
