package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.HwllxxDto;
import com.matridx.igams.storehouse.dao.entities.HwllxxModel;

public interface IHwllxxService extends BaseBasicService<HwllxxDto, HwllxxModel> {

	/**
	 * 根据出库ID查询货物领料信息
	 * @param ckid
	 * @return
	 */
    List<HwllxxDto> getDtoHwllxxListByCkidPrint(String ckid);
	/**
	 * 根据领料ID查询货物领料信息
	 * @param llid
	 * @return
	 */
    List<HwllxxDto> getDtoHwllxxListByLlidPrint(String llid);

	/**
	 * 删除货物领料信息
	 * @param hwllxxDto
	 * @return
	 */
    boolean delDtoListReceiveMaterie(HwllxxDto hwllxxDto);

	/**
	 * 获取领料信息
	 */
    List<HwllxxDto> getHwllInfo(HwllxxDto hwllxxDto, String userId);
	/**
	 * 获取货物领料信息和货物信息
	 * @return
	 */
    List<HwllxxDto> queryHwllxx(String llid, String bj, String dqjs);
	 
	/**
	 * 根据物料分组查询请领总数
	 * @return
	 */
    List<HwllxxDto> queryCkhwGroupByWlid(HwllxxDto hwllxxDto);
	/**
	 *	复制按钮查询明细
	 * @param hwllxxDto
	 * @return
	 */
    List<HwllxxDto> getDtoCopyList(HwllxxDto hwllxxDto);
	
}
