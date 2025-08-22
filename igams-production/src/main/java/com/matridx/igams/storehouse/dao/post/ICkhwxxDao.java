package com.matridx.igams.storehouse.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.CkhwxxDto;
import com.matridx.igams.storehouse.dao.entities.CkhwxxModel;

@Mapper
public interface ICkhwxxDao extends BaseBasicDao<CkhwxxDto, CkhwxxModel> {

	/**
	 * 库存列表
	 * 
	 * @param ckhwxxDto
	 * @return
	 */
	List<CkhwxxDto> getPagedDtoStockList(CkhwxxDto ckhwxxDto);
	/**
	 * 安全库存列表
	 *
	 * @param ckhwxxDto
	 * @return
	 */
	List<CkhwxxDto> getPagedDtoSecureStockList(CkhwxxDto ckhwxxDto);
	/**
	 * 获取库存明细
	 * @param ckhwxxDto
	 * @return
	 */
	List<CkhwxxDto> 	getWlxxByWlid(CkhwxxDto ckhwxxDto);

	/**
	 * 根据物料编码、仓库、库位、生产批号获取物料信息
	 * @param
	 * @return
	 */
	List<CkhwxxDto> getDtoByWlbmAndCkdm(CkhwxxDto ckhwxxDto);

	/**
	 * 根据ckhwid查看货物基本信息
	 * @return
	 */
	CkhwxxDto getJbxxByCkhwid(CkhwxxDto ckhwxxDto);
	/**
	 * 根据wlid查看货物基本信息
	 * @return
	 */
	List<CkhwxxDto> getJbxxListByCkhwid(CkhwxxDto ckhwxxDto);
	/**
	 * 根据ckhwid查看进货信息
	 * @param ckhwid
	 * @return
	 */
	List<CkhwxxDto> getJhxxByCkhwid(String ckhwid);
	List<CkhwxxDto> getJhxxList(CkhwxxDto ckhwxxDto);
	List<CkhwxxDto> getJhxxListByWlid(CkhwxxDto ckhwxxDto);
	/**
	 * 更新仓库货物信息
	 * 
	 * @param ckhwxxDtos
	 * @return
	 */
	int updateCkhwxxs(List<CkhwxxDto> ckhwxxDtos);

	/**
	 * 添加仓库货物信息
	 * 
	 * @param ckhwxxDtos
	 * @return
	 */
	int insertCkhwxxs(List<CkhwxxDto> ckhwxxDtos);
	
	/**
	 * 根据输入信息查询物料
	 * @return
	 */
	List<CkhwxxDto> queryWlxx(CkhwxxDto ckhwxxDto);
	
	/**
	 * 根据物料id查询物料
	 * @return
	 */
	CkhwxxDto queryWlxxByWlid(CkhwxxDto ckhwxxDto);

	/**
	 * 根据物料id更新仓库货物信息
	 * 
	 * @param ckhwxxDtos
	 * @return
	 */
	int updateByCkhwid(List<CkhwxxDto> ckhwxxDtos);
	
	/**
	 * 领料列表选中导出
	 * @return
	 */
 	List<CkhwxxDto> getListForSelectExp(CkhwxxDto ckhwxxDto);

    /**
     * 搜索条件分页获取导出信息
     * @return
     */
	List<CkhwxxDto> getListForSearchExp(CkhwxxDto ckhwxxDto);

	/**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
	int getCountForSearchExp(CkhwxxDto ckhwxxDto);

	/**
	 * 领料列表选中导出
	 * @return
	 */
	List<CkhwxxDto> getListForSeruceSelectExp(CkhwxxDto ckhwxxDto);

	/**
	 * 搜索条件分页获取导出信息
	 * @return
	 */
	List<CkhwxxDto> getListForSeruceSearchExp(CkhwxxDto ckhwxxDto);

	/**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
	int getCountForSeruceSearchExp(CkhwxxDto ckhwxxDto);
	
	/**
	 * 批量更新货物数量
	 * 
	 * @return
	 */
	int updateHwSl(List<CkhwxxDto> ckhwxxDtos);

	/**
	 * 更改仓库货物信息
	 *
	 * @param ckhwxxDto
	 */
	void updateCkhwxx(CkhwxxDto ckhwxxDto);

	/**
	 * 得到ckhwBYwlid
	 *
	 * @param wlid
	 * @return
	 */
	CkhwxxDto getCkhwInfo(String wlid);

	/**
	 * 获取用户机构的仓库
	 */
	CkhwxxDto getJsInfo(String userId);


	/**
	 * 根据wlid和ckqx查找数据
	 *
	 * @param ckhwxxDto
	 * @return
	 */
	CkhwxxDto getDtoByWlidAndCkid(CkhwxxDto ckhwxxDto);
	
	/**
	 * 根据wlid和ckqx查找数据
	 *
	 * @param ckhwxxDto
	 * @return
	 */
	List<CkhwxxDto> getDtoListByWlids(CkhwxxDto ckhwxxDto);
	
	/**
	 * 更改仓库货物信息
	 *
	 * @return
	 */
	int updateList(List<CkhwxxDto> ckhwxxDtos);
	
	/**
	 * 更改仓库货物信息
	 *
	 * @return
	 */
	int updateCkhwxxYds(List<CkhwxxDto> ckhwxxDtos);

	/**
	 * 批量更新预定数
	 * @param list
	 * @return
	 */
	int updateYdsList(List<CkhwxxDto> list);
	/**
	 * 根据仓库货物id查询数据
	 * @param ckhwid
	 * @return
	 */
	List<CkhwxxDto> getHwxxtbyCkhwid(String ckhwid);

	/**
	 * 根据仓库货物id查询数据
	 * @param wlid
	 * @return
	 */
	List<CkhwxxDto> getHwxxtbyWlid(String wlid);
	/**
	 * 订订库存列表接口
	 */
	List<CkhwxxDto> getPagedDtoStockListDingTalk(CkhwxxDto ckhwxxDto);
	/*
		通过wlid和仓库id修改仓库货物信息
	 */
    boolean updateByWlidAndCkid(List<CkhwxxDto> ckhwxxDtos);
	
	/**
	 * 获取库存不足的货物
	 */
	List<CkhwxxDto> SelectInsufficientInventory();
	/**
	 * @description 获取最后一次出库记录
	 * @param ckhwxxDto
	 * @return
	 */
	CkhwxxDto getLastCkInfo(CkhwxxDto ckhwxxDto);
	/*
    	获取仓库货物信息分组求
 	*/
	CkhwxxDto queryCkhwxxGroupByWlid(CkhwxxDto ckhwxxDto);
}
