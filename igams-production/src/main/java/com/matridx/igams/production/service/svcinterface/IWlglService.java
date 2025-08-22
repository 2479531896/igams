package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.BmwlDto;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.dao.entities.WlglModel;
import com.matridx.igams.production.dao.entities.WlgllsbDto;

import java.util.List;
import java.util.Map;

public interface IWlglService extends BaseBasicService<WlglDto, WlglModel>{

	/**
	 * 物料申请审核列表
	 */
	List<WlglDto> getPagedAuditList(WlglDto wlglDto);
	
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(WlglDto wlglDto, Map<String, Object> params);

	/**
	 * 通过wlid查询物料表，返回一个wllsbDto
	 */
	WlgllsbDto selectWllsbDtoByWlid(String wlid);
	
	/**
	 * 修改物料
	 */
	int updateWlgl(WlglDto wlglDto)throws BusinessException;
	
	/**
	 * 修改数据的同时 修改U8系统数据
	 */
	boolean updateInventory(WlglDto wlglDto);
	
	/**
	 * 模糊查询物料信息
	 */
	List<WlglDto> selectSameMater(WlglDto wlglDto);
	
	/**
	 * 删除物料管理同时删除审核过程
	 */
	boolean deleteWlgl(WlglDto wlglDto);
	
	/**
	 * 根据查询条件查询物料信息
	 */
	List<WlglDto> selectWl(WlglDto wlglDto);
	
	/**
	 * 根据物料编码获取多个物料信息
	 */
	List<WlglDto> getDtosByIds(WlglDto wlglDto);
	
	/**
	 * 新增或修改物料信息
	 */
	void insertOrUpdateWlgl(WlglDto wlglDto);
	
	/**
	 * 根据物料id获取物料基本信息
	 */
	List<WlglDto> getWlglDtoListByDtos(List<String> list);
	
	/**
	 * 库存维护修改U8数据
	 */
	boolean updateSafeStock(WlglDto wlglDto);
	
	/**
	 * 库存维护修改数据
	 */
	int updateKcwh(WlglDto wlglDto);
	
	/**
	 * 获取前15条物料信息
	 */
	List<WlglDto> getWlgl(WlglDto wlglDto);

	/**
	 * 通过wlbm查找wlid
	 */
	WlglDto getWlidByWlbm(String wlbm);
	/**
	 * 废弃
	 */
	boolean discard(WlglDto wlglDto);
	/**
	 * 单条物料点击查看接口
	 */
	WlglDto queryByWlid(WlglDto wlglDto);
	/**
	 * 根据物料ids获取物料信息
	 */
	List<WlglDto> queryByWllxByWlids(WlglDto wlglDto);
	/**
	 * 根据物料id获取物料信息
	 */
    List<WlglDto> getDtoListByWlid(WlglDto wlglDto);

    boolean depsettingSaveMater(WlglDto wlglDto, BmwlDto bmwlDto);
	/**
	 * 获取选中的数据
	 */
	List<WlglDto> getOrderedDtoList(WlglDto wlglDto);
	/**
	 * 获取最大的货期
	 */
	String selectBiggestHq(String wlid);

	boolean syncWlgl(WlglDto wlglDto)throws BusinessException;
	/*
		同步修改物料
	 */
	void syncUpdateWlgl(WlglDto wlglDto) throws BusinessException;
	/*
		同步删除物料
	 */
	void synclDeteWlgl(WlglDto wlglDto);
	/*
		同步物料库存维护
	 */
	void syncUpdateKcwh(WlglDto wlglDto);

	/**
	 * @Description: 查询个人设置物料信息
	 * @param wlglDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.WlglDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/7/23 16:09
	 */
	List<WlglDto> queryByGrsz(WlglDto wlglDto);

	/**
	 * @Description: 查询物料信息
	 * @param wlglDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.WlglDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/7/24 16:04
	 */
	List<WlglDto> queryByIds(WlglDto wlglDto);
}
