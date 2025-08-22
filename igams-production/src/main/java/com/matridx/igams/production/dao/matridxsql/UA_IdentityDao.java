package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.UA_IdentityDto;
import com.matridx.igams.production.dao.entities.UA_IdentityModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UA_IdentityDao extends BaseBasicDao<UA_IdentityDto,UA_IdentityModel>{
	/**
	 * 	获取主副表最大值
	 */
	UA_IdentityDto getMax(UA_IdentityDto uA_IdentityDto);
}
