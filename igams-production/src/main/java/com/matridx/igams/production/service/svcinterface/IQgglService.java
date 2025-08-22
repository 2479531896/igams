package com.matridx.igams.production.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgglModel;
import com.matridx.igams.production.dao.entities.QgmxDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IQgglService extends BaseBasicService<QgglDto, QgglModel>{
	
	/**
	 * 保存请购单
	 */
	boolean addSaveProdution(QgglDto qgglDto) throws BusinessException;
	
	/**
	 * 修改请购单
	 */
	boolean updateProdution(QgglDto qgglDto,List<QgmxDto> qgmxList)throws BusinessException;
	/**
	 * 根据单据号查询采购单信息(可用于判断采购单号是否重复)
	 */
	QgglDto getDtoByDjh(QgglDto qgglDto);
	
	/**
	 * 获取默认申请人信息
	 */
	QgglDto getMrsqrxxByYhid(QgglDto qgglDto);
	
	/**
	 * 请购列表
	 */
	List<QgglDto> getPagedList(QgglDto qgglDto);
	
	/**
	 * 审核列表
	 */
	List<QgglDto> getPagedAuditQggl(QgglDto qgglDto);
	
	/**
	 * 钉钉审批回调
	 */
	boolean callbackQgglAduit(JSONObject obj, HttpServletRequest request,Map<String,Object> t_map) throws BusinessException;
	
	/**
	 * 删除请购单

	 */
	boolean deleteQggl(QgglDto qgglDto) throws BusinessException;

	/**
	 * 报销请购单
	 */
	boolean baoxiaoQggl(QgglDto qgglDto);

	/**
	 * 高级修改请购信息
	 */
	boolean advancedModPurchase(QgglDto qgglDto, List<QgmxDto> qgmxlist) throws BusinessException;

	/**
	 * 自动生成单据号
	 */
	String generateDjh(QgglDto qgglDto);
	//取消请购更新请购信息和取消请购信息
	//public boolean savePurchaseCancel(QgglDto qgglDto,List<QgmxDto> qgmxlist) throws BusinessException;

	/**
	 *内网查看
	 */
	List<QgglDto> getQgList(QgglDto qgglDto);

	/**
	 * 批量更新请购完成标记
	 */
	boolean updateWcbjs(QgglDto qgglDto);
	
	/**
	 * 根据请购ids获取请购信息
	 */
	List<QgglDto> getDtoListByIds(QgglDto qgglDto);
	
	 /**
	   * 根据钉钉实例ID获取在钉钉进行请购审批的请购单信息
	  */
	 QgglDto getDtoByDdslid(QgglDto qgglDto);
    
    /**
     *	 审核后修改请购信息
	 */
	boolean updateQgglxx(QgglDto qgglDto);
    
    /**
     	* 查询数据生成请购
	 */
	List<Map<String, Object>> getParamForPurchase(QgglDto qgglDto);
    
    /**
     * 处理图纸压缩包

	 */
	Map<String,Object> dealBatchFileSec(QgglDto qgglDto);
    /**
	 * 获取请购单数量
	 */
	List<Map<String,Object>> getRequisitionMapByMon(QgglDto qgglDto);
	List<Map<String, Object>> getRequisitionMapByWeek(QgglDto qgglDto);
	/**
	 * 获取请购物料数量
	 */
	List<Map<String, Object>> getPurchaseMaterialMapByWeek(QgglDto qgglDto);
	List<Map<String, Object>> getPurchaseMaterialMapByMon(QgglDto qgglDto);
	
	/**
	 *根据请购明细id更新完成标记

	 */
	boolean updateByQgmxid(QgglDto qgglDto);

	/**
	 * 行政请购列表数据

	 */
	List<QgglDto> getPagedListAdministration(QgglDto qgglDto);
	/**
	 * 行政请购库存列表数据
	 */
	List<QgglDto> getPagedListAdminStock(QgglDto qgglDto);

	/**
	 * 更新确认标记
	 */
	boolean updateQrbj(QgglDto qgglDto);

	/**
	 * 单条物料点击查看
	 */
	QgglDto queryByQgid(QgglDto qgglDto);
	/**
	 * 根据搜索条件获取审核导出条数
	 */
	int getCountForAuditingSearchExp(QgglDto qgglDto, Map<String, Object> params);
	/**
	 * 根据搜索条件获取审核导出条数
	 */
	int getCountForCgAuditingSearchExp(QgglDto qgglDto, Map<String, Object> params);
	/**
	 * 请购单统计
	 */
	List<QgglDto> getCountStatistics(String year);

	List<QgglDto> getTodeyCount(QgglDto qgglDto);

    List<QgglDto> getTodayList(QgglDto qgglDto);

	/**
	 * 通过入库id查出请购信息

	 */
	List<QgglDto> getPurchasesByRkid(String rkid);
	/**
	 * 批量更新入库状态
	 */
	boolean updateRkztList(List<QgglDto> list);
	/**
	 * 根据请购明细ids获取请购数据
	 */
	List<QgglDto> getPurchaseByQgmxids(List<QgmxDto> qgmxDtos);
	/**
	 * 根据请购明细ids获取入库数量
	 */
	List<QgglDto> getRkslByQgmxids(List<String> ids);
	/**
	 * 根据请购明细ids获取行政入库数量
	 */
	List<QgglDto> getXzRkslByQgmxids(List<String> ids);

	Map<String,Object> queryOverTime(String sftx);

	List<QgglDto> queryOverTimeTable(QgglDto qgglDto);

	boolean updateSftxList(List<QgglDto> list);

	List<QgglDto> getQgOverTimeTable(QgglDto qgglDto);

	boolean updateDispose(QgglDto qgglDto);
	Map<String,Object> getOverTimeTable(String rqstart, String rqend);
}
