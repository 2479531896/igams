package com.matridx.igams.experiment.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.WksjmxDto;
import com.matridx.igams.experiment.dao.entities.WksjmxModel;
import com.matridx.igams.experiment.dao.post.IWksjmxDao;
import com.matridx.igams.experiment.service.svcinterface.IWksjmxService;
import com.matridx.igams.experiment.util.JDBCUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class WksjmxServiceImpl extends BaseBasicServiceImpl<WksjmxDto, WksjmxModel, IWksjmxDao> implements IWksjmxService{

	/**
	 * 获取文库导出明细信息
	 */
	public List<WksjmxDto> getListForWksj(WksjmxDto wksjmxDto){
		return dao.getListForWksj(wksjmxDto);
	}

	/**
	 * 删除已有文库上机明细
	 */
	public boolean deleteDtos(WksjmxDto wksjmxDto){
		return dao.deleteDtos(wksjmxDto);
	}

	/**
	 * 批量新增文库上机明细数据
	 */
	public boolean insertDtos(List<Map<String,String>> list){
		return dao.insertDtos(list);
	}

	/**
	 * 根据芯片名获取文库
	 */
	public List<WksjmxDto> getWkbmByXpm(WksjmxDto wksjmxDto){
		return dao.getWkbmByXpm(wksjmxDto);
	}
	/**
	 * 批量更新文库测序表
	 */
	public boolean updateByWkcxids(WksjmxDto wksjmxDto){
		return dao.updateByWkcxids(wksjmxDto);
	}

	/**
	 * 批量新增文库上机明细数据
	 */
	@Override
	public boolean insertDtoList(List<WksjmxDto> addsjmxList) {
		return dao.insertDtoList(addsjmxList);
	}
	/**
	 * 更新上机时间
	 */
	public boolean updateComputerTime(List<WksjmxDto> list){
		return dao.updateComputerTime(list);
	}
	/**
	 * 更新预计下机数据量
	 */
	public boolean updateWksjmxAfterPooling(List<WksjmxDto> list){
		return dao.updateWksjmxAfterPooling(list);
	}

	/**
	 * 根据文库上机ID获取文库上机信息，包括文库信息
	 */
	public List<WksjmxDto> getWksjDtoList(WksjmxDto wksjmxDto){
		return dao.getWksjDtoList(wksjmxDto);
	}

	/**
	 *	根据文库上机ID删除文库上机信息
	 * @param wksjmxDto
	 * @return
	 */
	public boolean deleteByWksjids(WksjmxDto wksjmxDto){
		return dao.deleteByWksjids(wksjmxDto);
	}

	/**
	 * 增加一个定时任务，7、12、17点，根据 前一天到当前的的上机明细数据里浓度为-1的标本
	 * ，找生信数据库core_library表other_metrics字段con.lib的值获取文库浓度，进行更新
	 */
	public void getWkndFromSx(){
		WksjmxDto wksjmxDto=new WksjmxDto();
		List<WksjmxDto> wksjmxDtoList=dao.getWksjDtoListByTime(wksjmxDto);
		JDBCUtils jdbcUtils=new JDBCUtils();
		List<Map<String,String>> mapList=jdbcUtils.querylib(wksjmxDtoList);
		if(!CollectionUtils.isEmpty(mapList)){
			dao.updateListFromSx(mapList);
		}
	}

	/**
	 * 根据文库上机明细的文库编码更新复检申请的文库编码
	 * @param wksjmxDto
	 * @return
	 */
	public boolean updateSyglByWksjid(WksjmxDto wksjmxDto){
		return dao.updateSyglByWksjid(wksjmxDto);
	}
}
