package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.warehouse.dao.entities.GdzcglDto;

import java.util.List;
import java.util.Map;

public interface IRdRecordService {
	/**
	 * 到货新增U8做入库
	 */
	Map<String, Object> addU8RkData(DhxxDto dhxxDto,List<DhxxDto> dhxxDtos,boolean flag) throws BusinessException ;

	/**
	 * 采狗红字U8处理
	 */
	Map<String, Object> pendingU8Data(DhxxDto dhxxDto,List<DhxxDto> dhxxDtos) throws BusinessException ;
	
	/**
	 * 检验退回U8做调拨
	 */
	Map<String, Object> addU8DBData(DhjyDto dhjyDto) throws BusinessException ;
	
	/**
	 * 领料新增U8做领料
	 */
	Map<String, Object> addU8LlData(LlglDto llglDto) throws BusinessException ;

	/**
	 * 借用U8操作
	 */
	Map<String, Object> addU8JyData(JcjyglDto jcjyglDto) throws BusinessException ;

	/**
	 * 归还U8操作
	 */
	Map<String, Object> addU8GhData(JcghglDto jcghglDto) throws BusinessException ;
	
	/**
	 * 出库新增U8做出库
	 */
	Map<String, Object> addU8CkData(LlglDto llglDto) throws BusinessException ;
	
	/**
	 * 入库审核处理U8数据
	 */
	Map<String,Object> dealRecordData(RkglDto rkglDto,List<HwxxDto> hwxxs_lb) throws BusinessException;
	
	/**
	 * 成品入库审核处理U8数据(OA到货新增)
	 */
	Map<String,Object> addU8CprkData(DhxxDto dhxxDto, List<DhxxDto> dhxxDtos,boolean flag) throws BusinessException;
	
	/**
	 * 新增记账记录
	 */
	boolean addAccountVs(List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos, String type);

	/**
	 * 新增U8数据
	 */
	boolean addBatchPs(List<HwxxDto> hwxxDtos);
	
	/**
	 * 入库新增U8做入库
	 */
	Map<String, Object> addU8RkDataRk(RkglDto rkglDto) throws BusinessException ;
	
	/**
	 * 到货做u8修改
	 */
	void updateDhxxupdate(DhxxDto dhxxDto);
	/**
	 * 检验列表
	 */
	void ggthsl(List<HwxxDto> hwxxlist, List<HwxxDto> hwxxDtos, double sumSaveQYL, double sumQYL);
	void pdQyl(List<HwxxDto> hwxxlist, List<HwxxDto> hwxxDtos, double sumSaveQYL, double sumQYL);
	/**
	 * 领料列表高级修改
	 */
	void updateRkglUpdate(RkglDto rkglDto, RkglDto rkglDto_h);
	/**
	 * 领料列表高级修改
	 */
	void updateLlglUpdate(LlglDto llglDto, LlglDto llglDto_h);
	
	/**
	 * 调拨新增U8调拨
	 */
	Map<String, Object> greqtU8DbData(DbglDto dbglDto) throws BusinessException ;

	/**
	 * 成品入库审核处理U8数据(OA入库新增)
	 */
	Map<String,Object> addU8CprkDataByRk(RkglDto rkglDto) throws BusinessException;
	
	/**
	 * 发货处理U8数据(OA发货)
	 */
	Map<String,Object> addU8DataByDeliver(FhglDto fhglDto) throws BusinessException;

	/**
	 * 产品结构新增U8操作
	 */
	Map<String,Object> addU8DataCpjg(CpjgDto cpjgDto, List<CpjgmxDto> cpjgmxDtos) throws BusinessException;

	/**
	 * 产品结构删除U8操作
	 */
	void delU8DataCpjg(CpjgDto cpjgDto, List<CpjgmxDto> cpjgmxDtos) throws BusinessException;


	/**
	 * 退货u8操作
	 */
	Map<String, Object> thsave(FhglDto fhglDto, List<FhmxDto> fhmxDtosLater, List<FhmxDto> fhmxDtosBefor) throws BusinessException;

	/**
	 * 请购新增U8操作
	 */
	Map<String,Object> addPurchaseToU8(Map<String, Object> data, List<QgmxDto> qgmxlist, QgglDto qgglDto_t) throws BusinessException;

	/**
	 * 请购修改U8操作
	 */
	Map<String,Object> updatePurchaseToU8(List<QgmxDto> qgmxList, QgglDto qgglDto, List<QgmxDto> insertList, List<QgmxDto> delQgmxDtos) throws BusinessException;

	/**
	 * 取消请购U8操作
	 */
	Map<String,Object> cancelPurchaseToU8(QgglDto qgglDto, List<QgmxDto> qgmxList) throws BusinessException;
	
	/**
	 * 领料出库红冲单
	 */
	Map<String,Object> redInkOffsetOfDeliveryOrder(RdRecordDto rdRecordDto) throws BusinessException;

	/**
	 * 出库红字处理U8数据
	 */
	Map<String,Object> addU8DataByOutDepot(CkglDto ckglDto) throws BusinessException;

	/**
	 * 销售订单
	 */
	Map<String, Object> addU8SaleData(XsglDto xsglDto,List<XsmxDto> dtoList) throws BusinessException;

	/**
	 * 新增U8固定资产数据
	 */
	boolean addU8Asset(GdzcglDto gdzcglDto) throws BusinessException;

	//新增客户管理同步四张表数据
	boolean addCustomerAndCustomer_extradefineAndSa_invoicecustomersAndTc_tmp_duplication(KhglDto khglDto) throws BusinessException;

	//修改客户管理同步四张表
	boolean updateCustomerAndCustomer_extradefineAndSa_invoicecustomersAndTc_tmp_duplication(KhglDto khglDto) throws BusinessException;
	/**
	 * 委外请购新增U8操作
	 */
	Map<String, Object> addPurchaseToU8ForWW(Map<String, Object> map, List<QgmxDto> qgmxlist, QgglDto qgglDto_t) throws BusinessException;

	/*
	* 合同审核通过同步u8
	* */
    Map<String, Object> addU8ContractData(HtglDto htglDto_t, List<HtmxDto> htmxDtos, QgglDto qgglDto_h, User operator,HtglDto htglDto) throws BusinessException;
	/*
	 * 委外合同审核通过同步u8
	 * */
	Map<String, Object> addU8ContractDataForWW(HtglDto htglDto_t, List<HtmxDto> htmxDtos, QgglDto qgglDto_h, User operator, HtglDto htglDto)throws BusinessException;
	/**
	 * 新增U8 报销数据
	 */
	Map<String, Object> addU8Reimburse(DdbxglDto ddbxglDto, List<DdbxmxDto> list);

    boolean updatePO_PomainAndPO_Podetails(HtmxDto htmxDto) throws BusinessException;

	boolean updateOM_MOMainAndOM_MODetails(HtmxDto htmxDto) throws BusinessException;
	/**
	 * 新增U8 退货数据
	 */
	Map<String, Object> addU8ThData(ThglDto thglDto, List<ThmxDto> dtoList) throws BusinessException;
	/**
	 *入账判断
	 */
	boolean determine_Entry(String rq);
}
