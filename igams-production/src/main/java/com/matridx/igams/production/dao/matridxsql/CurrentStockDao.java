package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.CurrentStockDto;
import com.matridx.igams.production.dao.entities.CurrentStockModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CurrentStockDao extends BaseBasicDao<CurrentStockDto,CurrentStockModel>{
	
	/**
	 * 查询ItemId最大值
	 */
	int getMaxItemId();
	
	/**
	 * 通过物料编码查询
	 */
	List<CurrentStockDto> queryBycInvCode(CurrentStockDto currentStockDto);
	
	/**
	 * 根据autoids查询仓库表的货物信息
	 
	 */
	List<CurrentStockDto> getCurrentStockByAutoIDs(List<String> ids);

	/**
	 * 根据autoid查询仓库表的货物信息
	 */
	CurrentStockDto getCurrentStockByAutoID(CurrentStockDto currentStockDto);

	/**
	 * 批量更新库存
	 */
	int updateList(List<CurrentStockDto> currentStockDtos);

	/**
	 * 批量更新库存
	 */
	int updateIQuantityList(List<CurrentStockDto> currentStockDtos);

	/**
	 * 批量更新库存ByAutoID
	 */
	int updateListByAutoID(List<CurrentStockDto> currentStockDtos);
	
	/**
	 * 更新仓库库存表的库位和数量信息
	 */
	boolean updateKwAndSl(List<CurrentStockDto> currentStockDtos);
	
	/**
	 * 添加仓库库存信息
	 */
	List<String> addCurrentStocks(List<CurrentStockDto> currentStockDtos);
	
	/**
	 *  根据生产批号，物料编码，库位查询库存信息
	 */
	CurrentStockDto getCurrentStocksByDto(CurrentStockDto currentStockDto);
	/**
	 * 通过货物信息更改
	 */
	void updateByHwxx(CurrentStockDto currentStockDto);
	
	/**
	 * 获取所有库存
	 */
	List<CurrentStockDto> getDtoAll();

	List<CurrentStockDto> getDtoAllBySl();
}
