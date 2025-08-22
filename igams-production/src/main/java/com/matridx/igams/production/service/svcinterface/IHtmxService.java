package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.HtmxModel;

import java.util.List;
import java.util.Map;

public interface IHtmxService extends BaseBasicService<HtmxDto, HtmxModel>{

	/**
	 * 根据合同ID获取合同明细信息
	 */
	List<HtmxDto> getListByHtid(String htid);

	/**
	 * 更新合同明细信息
	 */
	boolean updateHtmxDtos(List<HtmxDto> htmxDtos, HtglDto htglDto);

	/**
	 * 含税单价统计
	 */
	List<Map<String,Object>> statisticTaxPrice(HtmxDto htmxDto);

	/**
	 * 采购物料周期统计
	 */
	List<Map<String, Object>> statisticCycle(HtmxDto htmxDto);

	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(HtmxDto htmxDto,Map<String, Object> params);

	/**
	 * 批量修改合同明细信息

	 */
	boolean updateHtmxDtos(List<HtmxDto> htmxDtos);

	/**
	 * 批量删除合同明细信息
	 */
	boolean deleteHtmxDtos(HtmxDto htmxDto);

	/**
	 * 批量新增合同明细信息
	 */
	boolean insertHtmxDtos(List<HtmxDto> addHtmxDtos);

	/**
	 * 查询数量校验信息(物料)
	 */
	List<HtmxDto> selectCheckWlglInfo(List<HtmxDto> htmxDtos);

	/**
	 * 查询数量校验信息(请购明细)
	 */
	List<HtmxDto> selectCheckQgmxInfo(List<HtmxDto> htmxDtos);

	/**
	 * 新增合同明细请购数量修改(审核后)
	 */
	boolean updateQuantityAddLast(List<HtmxDto> addHtmxDtos);

	/**
	 * 删除合同明细请购数量修改(审核后)
	 */
	boolean updateQuantityDelLast(List<HtmxDto> delHtmxDtos);

	/**
	 * 获取修改的合同明细信息
	 */
	List<HtmxDto> getUpdateHtmxDtos(List<HtmxDto> htmxDtos);

	/**
	 * 审核通过时修改请购明细数量
	 */
	boolean updateQuantityComp(List<HtmxDto> htmxDtos);
	
	/**
	 * 过程维护（通过ids修改）
	 */
	boolean modSaveMaintenance(HtmxDto htmxDto);
	
	/**
	 * 更新合同信息至请购明细表
	 */
	boolean updateQgmxDtos(List<HtmxDto> list);
	
	/**
	 * 获取合同明细列表
	 */
	List<HtmxDto> getHtmxList(HtmxDto htmxDto);
	/**
	 * 更新合同明细到货数量
	 */
	int  updateHtmxDhsl(HtmxDto htmxDto);
	
	/**
	 * 批量更新合同明细到货信息
	 */
	boolean htmxDtoModDhxx(List<HtmxDto> htmxDtos);
    
    /**
     * 根据合同ids获取合同信息（共通页面）
	 */
	List<HtmxDto> getCommonDtoListByHtmxids(HtmxDto htmxDto);

	/**
	 * 根据合同明细ID更新u8mxid
	 */
	void updateU8mxid(HtmxDto htmxDto);
	/**
	 * 根据是否发票维护获取合同明细信息

	 */
	List<HtmxDto> getListForInvoice(HtmxDto htmxDto);
	/**
	 * 批量修改是否发票维护标记
	 */
	void updateSffpwh(HtmxDto htmxDto);
	/**
	 * 根据合同ID获取请购信息
	 */
	List<HtmxDto> getPurchaseDetails(HtmxDto htmxDto);
	/**
	 * 获取合同明细列表数据
	 */
	List<HtmxDto> getPageListContractDetail(HtmxDto htmxDto);
	/**
	 * 获取合同明细关联生产结构数据
	 */
	List<HtmxDto> getContractDetailWithStructure(HtmxDto htmxDto);
	/**
	 * 根据合同明细ID获取产品结构信息
	 */
    List<HtmxDto> getCpjgxxs(HtmxDto htmxDto);

	/**
	 * 根据合同明细IDs获取产品结构信息
	 */
	List<HtmxDto> getCpjgByWlids(HtmxDto htmxDto);

	List<HtmxDto> getListByHtidDingTalk(String htid);

    boolean openContract(HtmxDto htmxDto) throws BusinessException;

	boolean closeContract(HtmxDto htmxDto) throws BusinessException;

	List<HtmxDto> getDhxx(HtmxDto htmxDto);
	/**
	 * 是否初次订购
	 */
	List<String> getSfccdg(HtmxDto htmxDto);
	/**
	 * @description 根据供应商获取历史合同

	 */
    List<HtmxDto> getPagedHisContract(HtmxDto htmxDto);
	/**
	 * @description 保存框架合同明细

	 */
	boolean insertFrameworks(List<HtmxDto> htmxDtos);
	/**
	 * @description 修改删除标记
	 */
	boolean updateScbj(HtmxDto htmxDto);
	/**
	 * @description 获取合同明细
	 */
	HtmxDto getHisContract(HtmxDto htmxDto);
	/**
	 * @description 获取框架合同明细
	 */
    List<HtmxDto> getFrameworkContractDetail(HtmxDto htmxDto);
	/**
	 * @description 获取物料到货情况
	 */
	List<HtmxDto> getPagedtContractArrivalInfo(HtmxDto htmxDto);
	/**
	 * @description 获取未全部到货的合同
	 */
    List<HtmxDto> getNotAllArrival(HtmxDto htmxDto);
	/*
		获取原合同明细
	 */
	List<HtmxDto> getOriginalContractDetails(HtmxDto htmxDto);
	/*
		批量关闭行
	 */
	boolean closeContracts(HtmxDto htmxDto) throws BusinessException;
	/*
		获取原合同U8id
	 */
	List<HtmxDto> getOriginalContractU8(String htid);

	/**
	 * @Description: 获取补充合同明细信息
	 * @param htmxDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.HtmxDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/8/28 15:04
	 */
	List<HtmxDto> queryBchtmxDto(HtmxDto htmxDto);

	List<HtmxDto> getByQgidAndHtid(HtmxDto htmxDto);

	List<HtmxDto> getByQgid(HtmxDto htmxDto);
}
