package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.CkglDto;
import com.matridx.igams.storehouse.dao.entities.CkmxDto;
import com.matridx.igams.storehouse.dao.entities.CkmxModel;
import com.matridx.igams.storehouse.dao.entities.FhmxDto;


public interface ICkmxService extends BaseBasicService<CkmxDto, CkmxModel>{
	/**
	 * 添加出库明细信息
	 * 
	 * @param ckmxDtos
	 * @return
	 */
    boolean insertckmxs(List<CkmxDto> ckmxDtos);


	/**
	 * 查询U8出库单号
	 * @param
	 * @return
	 */
	List<String> getU8ckdh(CkmxDto ckmxDto);
	
	
	/**
	 * 更新出库明细信息
	 * 
	 * @param ckmxDtos
	 * @return
	 */
    boolean updateCkmxList(List<CkmxDto> ckmxDtos);

	/**
	 * 批量删除
	 * @param
	 * @return
	 */
    Boolean deleteByCkmxids(CkmxDto ckmxDto);

	/**
	 * 获取明细数据
	 */
    List<CkmxDto> getDtoMxList(CkmxDto ckmxDto);

	/**
	 * 获取红冲明细数据
	 */
    List<CkmxDto> getHcDtoList(CkmxDto ckmxDto);
	
	/**
	 * 更新出库明细信息
	 * 
	 * @return
	 */
    boolean updateList(List<FhmxDto> fhmxDtos);
	
	/**
	 * 分组查询
	 * @return
	 */
    List<CkmxDto> getDtoGroupBy(String ckid);
	
	/**
	 * 批量删除
	 * @return
	 */
    boolean deleteByCkid(CkmxDto ckmxDto);
	
	/**
	 * 批量更新
	 * @param ckmxDtos
	 * @return
	 */
    boolean updateCkmxDtos(List<CkmxDto> ckmxDtos);
	/**
	 * 获取明细数据
	 */
    List<CkmxDto> getPrintDtoList(CkmxDto ckmxDto);
	/**
	 * 获取委外明细数据
	 */
    List<CkmxDto> getWWDtoList(CkmxDto ckmxDto);
	/**
	 * 根据发货list获取货物信息
	 */
	List<CkmxDto> selectCkmxListByList(List<CkglDto> list);
	/**
	 * 获取出库明细列表用于异常
	 */
	List<CkmxDto> getPagedForException(CkmxDto ckmxDto);
}
