package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.IA_SummaryDto;
import com.matridx.igams.production.dao.entities.IA_SummaryModel;
import com.matridx.igams.production.dao.entities.MaterialAppVouchDto;
import com.matridx.igams.production.dao.entities.MaterialAppVouchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IA_SummaryDao extends BaseBasicDao<IA_SummaryDto, IA_SummaryModel>{
	/*
		通过物料编码获取平均单价
	 */
	List<IA_SummaryDto> getPriceForcInvCodes(IA_SummaryDto iaSummaryDto);
}
