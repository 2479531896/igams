package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.TransVouchDto;
import com.matridx.igams.production.dao.entities.TransVouchModel;


@Mapper
public interface TransVouchDao extends BaseBasicDao<TransVouchDto,TransVouchModel>{

	/**
	 * 保存U8调拨单
	 */
	boolean insertTransVouch(TransVouchDto transVouchDto);
	
	String getMaxID();
	
	TransVouchDto getDtoBycTVCode(TransVouchDto transVouchDto);

	void updateByKey(TransVouchDto transVouchDto);
	
	/**
	 * 根据单号查询
	 */
	List<TransVouchDto> getcTVCode(TransVouchDto transVouchDto);
}
