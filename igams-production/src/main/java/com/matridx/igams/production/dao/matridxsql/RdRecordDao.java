package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.RdRecordDto;
import com.matridx.igams.production.dao.entities.RdRecordModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RdRecordDao extends BaseBasicDao<RdRecordDto,RdRecordModel>{
	/**
	 * 获取ID最大值
	 */
	Integer getMax(RdRecordDto rdRecord01Dto);

	/**
	 * 查找流水号
	 */
	String getMaxCsysbarcode(String prefix);
	
	/**
	 * 根据单号查询调拨入库单
	 */
	List<RdRecordDto> getDtoListFromRd8(RdRecordDto rdRecordDto);
	
	/**
	 * 根据单号查询调拨出库单
	 */
	List<RdRecordDto> getDtoListFromRd9(RdRecordDto rdRecordDto);
	/**
	 * 根据单号查询调拨出库单
	 */
	List<RdRecordDto> get09DtoList(RdRecordDto rdRecordDto);


	/**
	 * 查询rd08Info
	 */
	List<RdRecordDto> get08DtoList(RdRecordDto rdRecordDto);
	
	/**
	 * 添加调拨出库单
	 */
	boolean insertRd09(RdRecordDto rdRecordDto);

	/**
	 * 添加借用出库单
	 */
	boolean insertRd09jy(RdRecordDto rdRecordDto);
	
	/**
	 * 添加调拨入库单
	 */
	boolean insertRd08(RdRecordDto rdRecordDto);
	
	/**
	 * 自动生成U8cCode
	 */
	String getcCodeSerialInRd8(String prefix);
	
	/**
	 * 自动生成U8cCode
	 */
	String getcCodeSerialInRd9(String prefix);
	
	/**
	 * 成品入库
	 */
    int insertRd10(RdRecordDto rdRecordDto);
	
	/**
	 * 检验当前入库单号是否存在
	 */
	List<RdRecordDto> getRdDtoList(RdRecordDto rdRecordDto);

	/**
	 * 查询出库单号
	 */
	List<RdRecordDto> getU8Ckdh(RdRecordDto rdRecordDto);
	
	/**
	 * 批量新增其他入库
	 */
	int insertQtrk08(RdRecordDto rdRecordDto);
	/**
	 * 修改相关数据
	 */
	void updateRd09(RdRecordDto rdRecordDto);
	
	/**
	 * 获取单号
	 */
	List<RdRecordDto> getcCodeRd(RdRecordDto rdRecordDto);
	
	/**
	 * 判断单号是否存在
	 */
	RdRecordDto queryByCode(RdRecordDto rdRecordDto);
	
	/**
	 * 批量新增销售出库
	 */
	int insertList(List<RdRecordDto> rdRecordDtos);


    int insertDtos(List<RdRecordDto> rdRecordDtos);

}
