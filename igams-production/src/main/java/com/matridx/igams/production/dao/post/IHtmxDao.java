package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.HtmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IHtmxDao extends BaseBasicDao<HtmxDto, HtmxModel>{

	/**
	 * 根据合同ID获取合同明细信息
	 */
	List<HtmxDto> getListByHtid(String htid);

	/**
	 * 根据是否发票维护获取合同明细信息
	 */
	List<HtmxDto> getListForInvoice(HtmxDto htmxDto);


	/**
	 * 批量删除合同明细信息
	 */
	int deleteHtmxDtos(HtmxDto htmxDto);

	/**
	 * 批量新增合同明细信息
	 */
	int insertHtmxDtos(List<HtmxDto> addHtmxDtos);

	/**
	 * 批量修改合同明细信息
	 */
	int updateHtmxDtos(List<HtmxDto> htmxDtos);

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
	int getCountForSearchExp(HtmxDto htmxDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<HtmxDto> getListForSearchExp(HtmxDto htmxDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<HtmxDto> getListForSelectExp(HtmxDto htmxDto);

	/**
	 * 查询数量校验信息(物料)
	 */
	List<HtmxDto> selectCheckWlglInfo(List<HtmxDto> htmxDtos);

	/**
	 * 查询数量校验信息(请购明细)
	 */
	List<HtmxDto> selectCheckQgmxInfo(List<HtmxDto> htmxDtos);

	/**
	 * 新增合同明细请购数量修改
	 */
	void updateQuantityAdd(List<HtmxDto> addHtmxDtos);

	/**
	 * 删除合同明细请购数量修改
	 */
	void updateQuantityDel(List<HtmxDto> delHtmxDtos);

	/**
	 * 获取需要修改的合同明细数量
	 */
	List<HtmxDto> getUpdateHtmxDtos(List<HtmxDto> htmxDtos);

	/**
	 * 新增合同明细请购数量修改(审核后)
	 */
	void updateQuantityAddLast(List<HtmxDto> addHtmxDtos);

	/**
	 * 删除合同明细请购数量修改(审核后)
	 */
	void updateQuantityDelLast(List<HtmxDto> delHtmxDtos);

	/**
	 * 审核通过时修改请购明细数量
	 */
	void updateQuantityComp(List<HtmxDto> htmxDtos);
	
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
	 * 更新合同明细的到货数量
	 */
	int updateHtmxDhsl(HtmxDto htmxDto);
	
	/**
	 * 更新合同明细的到货信息
	 */
	int htmxDtoModDhxx(List<HtmxDto> htmxDtos);
	
    /**
     * 根据合同ids获取合同信息（共通页面）
     */
	List<HtmxDto> getCommonDtoListByHtmxids(HtmxDto htmxDto);

	/**
	 * 根据合同明细ID更新u8mxid
	 */
	void updateU8mxid(HtmxDto htmxDto);

	/**
	 * 批量修改是否发票维护标记
	 */
	void updateSffpwh(HtmxDto htmxDto);

	/**
	 * 根据合同ID获取请购信息
	 */
	List<HtmxDto> getPurchaseDetails(HtmxDto htmxDto);

	/**
	 *
	 */
	HtmxDto queryByHtmx(HtmxDto htmxDto);

	/**
	 * 获取合同明细列表数据
	*/
	List<HtmxDto> getPageListContractDetail(HtmxDto htmxDto);
	/**
	 * 获取合同明细关联生产结构数据
	 */
	List<HtmxDto> getContractDetailWithStructure(HtmxDto htmxDto);
	/*
	* 获取产品结构信息list
	* */
	List<HtmxDto> getCpjgxxs(HtmxDto htmxDto);

	/**
	 * 根据合同物料IDs获取产品结构信息
	 */
	List<HtmxDto> getCpjgByWlids(HtmxDto htmxDto);
	/*
	* 获取产品结构信息
	* */
	HtmxDto getCpjgxx(HtmxDto htmxDto);
	List<HtmxDto> getListByHtidDingTalk(String htid);

    List<HtmxDto> getDhxx(HtmxDto htmxDto);

	boolean updateOpenClose(HtmxDto htmxDto);

    HtmxDto getDtoByHtmxId(String htmxid);

	List<HtmxDto> getHtmxListByHtid(String htid);
	/**
	 * @description 是否初次订购
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
	boolean updateOpenCloses(HtmxDto htmxDto);
	/*
    	获取原合同U8id
 	*/
	List<HtmxDto> getOriginalContractU8(String htid);

	/**
	 * @Description: 查询补充合同明细信息
	 * @param htmxDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.HtmxDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/8/28 15:00
	 */
	List<HtmxDto> queryBchtmxDto(HtmxDto htmxDto);

	List<HtmxDto> getByQgidAndHtid(HtmxDto htmxDto);

	List<HtmxDto> getByQgid(HtmxDto htmxDto);
}
