package com.matridx.igams.storehouse.dao.post;

import java.util.List;

import com.matridx.igams.storehouse.dao.entities.CkglDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.storehouse.dao.entities.CkmxDto;
import com.matridx.igams.storehouse.dao.entities.CkmxModel;
import com.matridx.igams.storehouse.dao.entities.FhmxDto;

@Mapper
public interface ICkmxDao extends BaseBasicDao<CkmxDto, CkmxModel>{
	/**
	 * 添加出库明细信息
	 * 
	 * @param ckmxDtos
	 * @return
	 */
	int insertckmxs(List<CkmxDto> ckmxDtos);
	
	/**
	 * 更新出库明细信息
	 * 
	 * @param ckmxDtos
	 * @return
	 */
	int updateCkmxList(List<CkmxDto> ckmxDtos);
	/**
	 * 获取明细数据
	 */
	List<CkmxDto> getDtoMxList(CkmxDto ckmxDto);

	/**
	 * 获取明细数据
	 */
	List<CkmxDto> getDtoListByHwid(CkmxDto ckmxDto);


	/**
	 * 获取红冲明细数据
	 */
	List<CkmxDto> getHcDtoList(CkmxDto ckmxDto);
	/**
	 * 获取委外明细数据
	 */
	List<CkmxDto> getWWDtoList(CkmxDto ckmxDto);
	/**
	 * 更新出库明细信息
	 * 
	 * @param
	 * @return
	 */
	int updateList(List<FhmxDto> fhmxDtos);
	
	/**
	 * 分组查询
	 * @param
	 * @return
	 */
	List<CkmxDto> getDtoGroupBy(String ckid);
	
	/**
	 * 批量删除
	 * @param
	 * @return
	 */
	int deleteByCkid(CkmxDto ckmxDto);

	/**
	 * 批量删除
	 * @param
	 * @return
	 */
	int deleteByCkmxids(CkmxDto ckmxDto);
	
	/**
	 * 批量更新出库信息
	 * @param
	 * @return
	 */
	int updateCkmxDtos(List<CkmxDto> ckmxDtos);

	/**
	 * 获取明细数据
	 */
	List<CkmxDto> getPrintDtoList(CkmxDto ckmxDto);
	/**
	 * 根据发货list获取货物信息
	 */
	List<CkmxDto> selectCkmxListByList(List<CkglDto> list);

	/**
	 * 获取出库明细列表用于异常
	 */
	List<CkmxDto> getPagedForException(CkmxDto ckmxDto);

}
