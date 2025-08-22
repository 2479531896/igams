package com.matridx.igams.storehouse.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.HwllmxDto;
import com.matridx.igams.storehouse.dao.entities.HwllmxModel;

@Mapper
public interface IHwllmxDao extends BaseBasicDao<HwllmxDto, HwllmxModel>{
	/**
	 * 获取最大序号
	 * @param 
	 * @return
	 */
	Integer getMaxXh();
	
	/**
	 * 批量新增
	 * @param 
	 * @return
	 */
	int insertHwllmxs(List<HwllmxDto> hwllmxDtos);
	
	/**
	 * 批量更新
	 * @param 
	 * @return
	 */
	int updateHwllmxs(List<HwllmxDto> hwllmxDtos);
	
	/**
	 * 根据hwllid查询货物领料明细
	 * @param hwllid
	 * @return
	 */
	List<HwllmxDto> getDtoHwllmxListByHwllid(String hwllid);
	
	/**
	 * 分组查询领料明细
	 * @param hwllmxDto
	 * @return
	 */
	List<HwllmxDto> getDtoHwllmxGroupByHwid(HwllmxDto hwllmxDto);
	
	/**
	 * 根据领料id获取领料明细
	 * @return
	 */
	List<HwllmxDto> getDtoByLlid(HwllmxDto hwllmxDto);
	
	/**
	 * 批量更新
	 * @param 
	 * @return
	 */
	int updateMxList(List<HwllmxDto> hwllmxDtos);

	/**
	 * 更改请领数量
	 *
	 * @param
	 */
	void updateqlsl(HwllmxDto hwllmxDto);

	
	/**
	 * 根据llid分组获取允许领取数量总数
	 * @param 
	 * @return
	 */
	List<HwllmxDto> getDtoGroupByLlid(String llid);
	
	/**
	 * 根据hwllid获取货物领料明细
	 * @param 
	 * @return
	 */
	List<HwllmxDto> queryByHwllid(HwllmxDto hwllmxDto);
	
	/**
	 * 根据hwllid删除货物领料明细
	 * @param 
	 * @return
	 */
	int deleteByHwllid(HwllmxDto hwllmxDto);
	
	/**
	 * 根据仓库id分组查询
	 * @param 
	 * @return
	 */
	List<HwllmxDto> groupByCkid(HwllmxDto hwllmxDto);
	
	/**
	 * 根据llmxid查询
	 * @param 
	 * @return
	 */
	List<HwllmxDto> getDtoHwllmxListByLlid(String llid);
	
	/**
	 * 获取领料明细id
	 * @param 
	 * @return
	 */
	HwllmxDto queryHwllmxs(HwllmxDto hwllmxDto);
	
	/**
	 * 批量删除
	 * @param 
	 * @return
	 */
	int deleteByllid(HwllmxDto hwllmxDto);

    List<HwllmxDto> getDtoHwllmxListByPrint(String llid);
	/**
	 * 获取货物领料明细
	 * @return
	 */
	List<HwllmxDto> getDtoHwllmxByLlid(String llid);
	
	List<HwllmxDto> getSumGroup(String llid);

	List<HwllmxDto> getScphAndSlByHwllid(String hwllid);
	/**
	 * 根据hwllxx分组获取允许领取数量总数
	 * @param
	 * @return
	 */
	List<HwllmxDto> getDtoGroupByLlxx(String llid);
}
