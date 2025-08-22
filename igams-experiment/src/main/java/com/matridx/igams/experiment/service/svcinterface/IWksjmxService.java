package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.WksjmxDto;
import com.matridx.igams.experiment.dao.entities.WksjmxModel;

public interface IWksjmxService extends BaseBasicService<WksjmxDto, WksjmxModel>{

	/**
	 * 获取文库导出明细信息
	 */
	List<WksjmxDto> getListForWksj(WksjmxDto wksjmxDto);

	/**
	 * 删除已有文库上机明细
	 */
	boolean deleteDtos(WksjmxDto wksjmxDto);

	/**
	 * 批量新增文库上机明细数据
	 */
	boolean insertDtos(List<Map<String,String>> list);
	/**
	 * 根据芯片名获取文库
	 */
	List<WksjmxDto> getWkbmByXpm(WksjmxDto wksjmxDto);
	/**
	 * 批量更新文库测序表
	 */
	boolean updateByWkcxids(WksjmxDto wksjmxDto);

	/**
	 * 批量新增上机明细
	 */
	boolean insertDtoList(List<WksjmxDto> addsjmxList);

	/**
	 * 更新上机时间
	 */
	boolean updateComputerTime(List<WksjmxDto> list);

	/**
	 * 更新预计下机数据量
	 */
	boolean updateWksjmxAfterPooling(List<WksjmxDto> list);
	
	/**
	 * 根据文库上机ID获取文库上机信息，包括文库信息
	 */
	List<WksjmxDto> getWksjDtoList(WksjmxDto wksjmxDto);

	/**
	 * 根据文库上机ID删除文库上机信息
	 */
	boolean deleteByWksjids(WksjmxDto wksjmxDto);

	/**
	 * 根据文库上机明细的文库编码更新复检申请的文库编码
	 * @param wksjmxDto
	 * @return
	 */
	boolean updateSyglByWksjid(WksjmxDto wksjmxDto);
}
