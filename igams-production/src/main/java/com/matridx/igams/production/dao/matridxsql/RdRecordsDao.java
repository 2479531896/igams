package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.RdRecordsDto;
import com.matridx.igams.production.dao.entities.RdRecordsModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RdRecordsDao extends BaseBasicDao<RdRecordsDto,RdRecordsModel>{
	/**
	 * 获取ID最大值
	 */
	Integer getMax(RdRecordsDto rdRecords01Dto);
	
	/**
	 * 获取货物明细流水
	 */
	Integer getCountByID(String ID);
	
	/**
	 * 批量添加调拨入库单明细
	 */
	boolean insertRd8(List<RdRecordsDto> list);
	
	/**
	 * 获取货物明细流水
	 */
	Integer getCountByIDInRd08(String ID);
	
	/**
	 * 获取货物明细流水
	 */
	Integer getCountByIDInRd09(String ID);
	
	/**
	 * 批量添加调拨出库明细
	 */
	boolean insertRd9(List<RdRecordsDto> list);
	/**
	 * 批量添加借用出库明细
	 */
	boolean insertRds9jy(List<RdRecordsDto> list);
	
	/**
	 * 批量添加入库明细
	 */
	int insertRds(List<RdRecordsDto> list);
	
	/**
	 * 批量添加成品入库明细
	 */
	int insertRds10(List<RdRecordsDto> list);
	
	/**
	 * 批量添加其他入库明细
	 */
	int insertRds08(List<RdRecordsDto> list);

	/**
	 * 批量添加其他入库明细
	 */
	int insertQtRds08(List<RdRecordsDto> list);
	
	/**
	 * 添加U8相关表数据
	 */
	int insertRds09Sub(List<RdRecordsDto> list);

	/**
	 * 修改相关数据
	 */
	void updateRdRecords08Info(RdRecordsDto rdRecordsDto);
	/**
	 * 修改相关数据
	 */
	void updateRdRecords09Info(RdRecordsDto rdRecordsDto);
	/**
	 * 获取rds01
	 */
	RdRecordsDto getRds01(Integer AutoId);
	/**
	 * 获取rds08
	 */
	RdRecordsDto getRds08(Integer AutoId);
	/**
	 * 获取rds08
	 */
	RdRecordsDto getRds09(Integer AutoId);
	
	/**
	 * 批量新增销售出库明细
	 */
	int insertListDtos(List<RdRecordsDto> RdRecordsDtos);
}
