package com.matridx.igams.experiment.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.TqmxDto;
import com.matridx.igams.experiment.dao.entities.TqmxModel;

@Mapper
public interface ITqmxDao extends BaseBasicDao<TqmxDto, TqmxModel>{
	
	/**
	 * 根据内部编号获取送检id
	 */
	List<TqmxDto> getSjxxByNbbh(TqmxDto tqmxDto);
	/**
	 * 根据内部编号获取复检信息
	 */
	List<TqmxDto> getFjxxByNbbms(TqmxDto tqmxDto);
	/**
	 * 根据内部编号获取送检信息
	 */
	List<TqmxDto> getSjxxByNbbms(TqmxDto tqmxDto);
	
	/**
	 * 新增提取明细
	 */
	boolean insertTqmx(List<TqmxDto> list);
	
	/**
	 * 根据内部编号查询提取明细
	 */
	List<TqmxDto> gettqmxByNbbh(TqmxDto tqmxDto);
	
	/**
	 * 根据提取id获取提取信息
	 */
	List<TqmxDto> getTqmxByTqid(TqmxDto tqmxDto);
	
	/**
	 * 根据提取id删除提取明细数据
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
	 * 提取明细临时新增
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
	 * 获取送检信息
	 */
	List<TqmxDto> getInfoByNbbm(TqmxDto tqmxDto);

	/**
	 * 根据内部编码获取对应信息
	 */
	List<TqmxDto> getSjsyglByNbbms(TqmxDto tqmxDto);
	/**
	 * {@code @description} 提取列表导出明细
	 */
	List<TqmxDto> getTqmxByTqidByPrint(TqmxDto tqmxDto);
	/**
	 * 获取实验信息
	 */
	List<TqmxDto> getInfoBySyglids(TqmxDto tqmxDto);
	/**
	 * 更新实验管理实验日期
	 */
	boolean updateSyList(List<TqmxDto> list);
	/**
	 * 更新送检管理实验日期
	 */
	void updateSjList(List<TqmxDto> list);
	/**
	 * 更新复检管理实验日期
	 */
	void updateFjList(List<TqmxDto> list);

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
