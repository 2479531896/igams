package com.matridx.igams.storehouse.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.HwllxxDto;
import com.matridx.igams.storehouse.dao.entities.HwllxxModel;

@Mapper
public interface IHwllxxDao extends BaseBasicDao<HwllxxDto, HwllxxModel> {
	
	/**
	 * 根据领料ID查询货物领料信息
	 * @param llid
	 * @return
	 */
	List<HwllxxDto> getDtoHwllxxListByLlid(String llid);
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
	 * 添加货物领料信息
	 * 
	 * @return
	 */
	boolean insertHwllxx(List<HwllxxDto> hwllxxDtos);
	
	/**
	 * 更新货物领料信息
	 * 
	 * @return
	 */
	int updateHwllxxDtos(List<HwllxxDto> hwllxxDtos);

	/**
	 * 删除货物领料信息
	 * @param hwllxxDto
	 * @return
	 */
	boolean delDtoListReceiveMaterie(HwllxxDto hwllxxDto);



	/**
	 * 根据物料分组查询请领总数
	 * @return
	 */
	List<HwllxxDto> queryCkhwGroupByWlid(HwllxxDto hwllxxDto);
	
	/**
	 * 根据领料id删除领料信息
	 * @return
	 */
	int deleteByllid(HwllxxDto hwllxxDto);
	
	/**
	 *	根据llid获取货物领料明细信息
	 * @return
	 */
	List<HwllxxDto> getListByLlid(HwllxxDto hwllxxDto);

	/**
	 * 更改请领数量
	 *
	 * @param hwllxxDto
	 */
	void updateqlsl(HwllxxDto hwllxxDto);
	/**
	 *	复制按钮查询明细
	 * @param hwllxxDto
	 * @return
	 */
	List<HwllxxDto> getDtoCopyList(HwllxxDto hwllxxDto);

}
