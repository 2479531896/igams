package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.TransVouchsDto;
import com.matridx.igams.production.dao.entities.TransVouchsModel;


@Mapper
public interface TransVouchsDao extends BaseBasicDao<TransVouchsDto,TransVouchsModel>{

	/**
	 * 添加U8调拨单明细
	 */
	boolean insertTransVouchs(List<TransVouchsDto> transVouchs);
	
	/**
	 * 获取最大的autoID
	 */
	String getMaxAutoID();

	/**
	 * 根据Id修改
	 */
	void updateByid(TransVouchsDto transVouchsDto);
	/**
	 * 根据autoid查找信息
	 */
	TransVouchsDto getInfoByAutoId(TransVouchsDto transVouchsDto);
}
