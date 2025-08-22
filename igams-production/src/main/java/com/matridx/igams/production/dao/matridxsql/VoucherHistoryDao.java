package com.matridx.igams.production.dao.matridxsql;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.VoucherHistoryDto;
import com.matridx.igams.production.dao.entities.VoucherHistoryModel;

@Mapper
public interface VoucherHistoryDao extends BaseBasicDao<VoucherHistoryDto,VoucherHistoryModel>{
	/**
	 * 获取流水号
	 */
	VoucherHistoryDto getMaxSerial(VoucherHistoryDto voucherHistoryDto);
}
