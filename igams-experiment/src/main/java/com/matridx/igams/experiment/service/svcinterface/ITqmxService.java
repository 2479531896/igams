package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.TqmxDto;
import com.matridx.igams.experiment.dao.entities.TqmxModel;

public interface ITqmxService extends BaseBasicService<TqmxDto, TqmxModel>{

	/**
	 * 校验内部编码是否存在
	 */
	List<String> exitnbbh(TqmxDto tqmxDto) throws BusinessException;

	/**
	 * 新增保存提取浓度信息
	 */
	boolean addSaveTqxx(TqmxDto tqmxDto) throws BusinessException;

	/**
	 * 根据内部编号查询提取明细
	 */
	List<TqmxDto> gettqmxByNbbh(TqmxDto tqmxDto);

	/**
	 * 根据提取id获取提取信息
	 */
	List<TqmxDto> getTqmxByTqid(TqmxDto tqmxDto);
	/**
	 * 获取送检信息
	 */
	List<TqmxDto> getInfoByNbbm(TqmxDto tqmxDto);

	/**
	 * 更新提取浓度信息
	 */
	boolean updateTqmx(TqmxDto tqmxDto) throws BusinessException;

	/**
	 * 删除提取明细数据
	 */
	boolean deleteByTqid(TqmxDto tqmxDto);

	/**
	 * 根据送检id获取提取信息
	 */
	List<TqmxDto> getTqxxBySjid(String sjid);

	/**
	 * 通过删除标记删除提取明细数据
	 */
	boolean delByTqid(TqmxDto tqmxDto);

	/**
	 * 根据提取ID和序号获取提取信息
	 */
	TqmxDto getDtoByIdAndXh(TqmxDto tqmxDto);

	/**
	 * 提取明细临时保存
	 */
	boolean temporarySave(TqmxDto tqmxDto);

	/**
	 * 提取明细临时修改
	 */
	boolean updateByIdAndXh(TqmxDto tqmxDto);

	/**
	 * 根据提取ID删除提取明细数据
	 */
	boolean delTqmxDto(TqmxDto tqmxDto);

	/**
	 * 根据内部编码获取检测项目信息
	 */
	TqmxDto getSjDtoByNbbh(TqmxDto tqmxDto);
	/**
	 * {@code @description} 提取列表导出明细
	 */
	List<TqmxDto> getTqmxByTqidByPrint(TqmxDto tqmxDto);
	/**
	 * 获取实验信息
	 */
	List<TqmxDto> getInfoBySyglids(TqmxDto tqmxDto);

	/**
	 * 自动排版获取明细
	 * @param tqmxDto
	 * @return
	 */
	List<TqmxDto> getInfoByZdpb(TqmxDto tqmxDto);

	/**
	 * 获取提取信息自动排版
	 */
	List<TqmxDto> getExtractInfoZdpb(Map<String,Object> map);

	/**
	 * 获取文库信息自动排版
	 * @return
	 */
	List<TqmxDto> getWkInfoListZdpb(Map<String,Object> map);

	Map<String,String> getCountByjcdw(Map<String,Object> map);
}
