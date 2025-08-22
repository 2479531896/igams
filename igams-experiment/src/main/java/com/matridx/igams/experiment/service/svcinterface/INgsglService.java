package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.NgsglDto;
import com.matridx.igams.experiment.dao.entities.NgsglModel;
import com.matridx.igams.experiment.dao.entities.NgsglWsDto;

public interface INgsglService extends BaseBasicService<NgsglDto, NgsglModel>{

	/**
	 * 接收ngs信息并保存
	 */
	boolean addSaveNgsDtos(List<NgsglWsDto> list);
	
	/**
	 * 根据制备编码和内部编码获取相应信息
	 */
	List<NgsglDto> getDtoByZbbmAndNbbm(NgsglDto ngsglDto);
	
	/**
	 * 根据检测ID获取ngs信息
	 */
	NgsglDto getDtoByJcid(NgsglDto ngsglDto);
	
	/**
	 * 获取所有ngs信息
	 */
	List<NgsglDto> getPageNgsglDto(NgsglDto ngsglDto);
	
	/**
	 * 根据ngsid获取ngs详细信息
	 */
	NgsglDto getNgsByngsid(String ngsid);
	
	/**
	 * 根据keyword获取筛选条件
	 */
	List<NgsglDto> queryBySfcg();
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(NgsglDto ngsglDto,Map<String, Object> params);


}
