package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SCM_ItemDto;
import com.matridx.igams.production.dao.entities.SCM_ItemModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SCM_ItemDao extends BaseBasicDao<SCM_ItemDto,SCM_ItemModel>{
	/**
	 * 查询ItemId最大值
	 */
	int getMaxItemId();

	/**
	 * 根据物料编码查询
	 */
	SCM_ItemDto getDtoBycInvVode(String cInvCode);
}
