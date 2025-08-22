package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.HwllmxDto;
import com.matridx.igams.storehouse.dao.entities.HwllmxModel;

public interface IHwllmxService extends BaseBasicService<HwllmxDto, HwllmxModel>{
	/**
	 * 批量新增
	 * @param 
	 * @return
	 */
    boolean insertHwllmxs(List<HwllmxDto> hwllmxDtos);
	
	/**
	 * 批量更新
	 * @param 
	 * @return
	 */
    boolean updateHwllmxs(List<HwllmxDto> hwllmxDtos);
	
	/**
	 * 根据hwllid查询货物领料明细
	 * @param hwllid
	 * @return
	 */
    List<HwllmxDto> getDtoHwllmxListByHwllid(String hwllid);
	
	/**
	 * 分组获取领料明细信息
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
    boolean updateMxList(List<HwllmxDto> hwllmxDtos);
	
	/**
	 * 根据llid分组获取允许领取数量总数
	 * @param 
	 * @return
	 */
	List<HwllmxDto> getDtoGroupByLlid(String llid);


	/**
	 * 更改请领数量
	 *
	 * @param
	 */
    void updateqlsl(HwllmxDto hwllmxDto);
	
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
    boolean deleteByHwllid(HwllmxDto hwllmxDto);
	
	/**
	 * 根据仓库id分组查询
	 * @param 
	 * @return
	 */
	List<HwllmxDto> groupByCkid(String ids);
	
	/**
	 * 根据llmxid查询
	 * @param 
	 * @return
	 */
	List<HwllmxDto> getDtoHwllmxListByLlid(String llid);


	/**
	 * 根据llmxid查询打印
	 * @param
	 * @return
	 */
	List<HwllmxDto> getDtoHwllmxListByPrint(String llid);
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
	boolean deleteByllid(HwllmxDto hwllmxDto);

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
