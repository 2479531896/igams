package com.matridx.igams.production.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtglModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IHtglService extends BaseBasicService<HtglDto, HtglModel>{
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(HtglDto htglDto,Map<String, Object> params);
	/**
	 * 根据搜索条件获取审核导出条数
	 */
	int getCountForAuditingSearchExp(HtglDto htglDto,Map<String, Object> params);

	/**
	 * 新增合同保存
	 */
	boolean addSaveContract(HtglDto htglDto) throws BusinessException;

	/**
	 * 修改合同保存
	 */
	boolean modSaveContract(HtglDto htglDto) throws BusinessException;
	
	/**
	 * 合同高级修改保存
	 */
	boolean advancedModSaveContract(HtglDto htglDto) throws BusinessException;

	/**
	 * 生成合同内部编号
	 */
	Map<String, Object> generateContractCode(HtglDto htglDto);
	
	/**
	 * 获取审核列表
	 */
	List<HtglDto> getPagedAuditHtgl(HtglDto htglDto);
	
	/**
	 * 获取合同列表
	 */
	List<HtglDto> getPagedDtoList(HtglDto htglDto);
	
	/**
	 * 查询数据生成合同
	 */
	Map<String,Object> getParamForContract(HtglDto htglDto);
	
	/**
	 * 钉钉合同审批回调
	 */
	boolean callbackHtglAduit(JSONObject obj, HttpServletRequest request,Map<String,Object> t_map) throws BusinessException;

	/**
	 * 判断内部编号是否重复
	 */
	boolean isHtnbbhRepeat(String htnbbh, String htid);

	/**
	 * 合同明细数量校验
	 */
	Map<String, Object> checkQuantity(HtglDto htglDto);
	
	/**
	 * 删除
	 */
	boolean deleteHtgl(HtglDto htglDto) throws BusinessException;
	
	/**
	 * 合同外发盖章
	 */
	boolean OutgrowthContract(HtglDto htglDto);
	
	/**
	 * 根据钉钉实例ID查询合同信息
	 */
	HtglDto getDtoByDdslid(HtglDto htglDto);
	/**
	 * 根据供应商查询合同
	 */
	List<HtglDto> getDtoListByGys(HtglDto htglDto);
	/**
	 * 获取合同数量
	 */
	List<Map<String, Object>> getContractMapByMon(HtglDto htglDto);

	/**
	 * 更新合同数据
	 */
	int updateContract(HtglDto htglDto);

	/**
	 * 根据钉钉iD获取审批人信息
	 */
	HtglDto getSprxxByDdid(String ddid);

	/**
	 * 根据用户id获取角色信息
	 */
	List<HtglDto> getSprjsBySprid(String sprid);

	/**
	 * 查询未完成到货的合同
	 */
	List<HtglDto> getPagedDtoKdhList(HtglDto htglDto);

	/**
	 * 查询未发票维护的合同
	 */
	List<HtglDto> getPageListContract(HtglDto htglDto);

	/**
	 * 检查合同信息是否已完善
	 */
	boolean checkHtMsg(HtglDto htglDto);

	/**
	 * 更新合同管理付款信息
	 */
	boolean updateFkxxByList(List<HtglDto> htglDtos);
	
	/**
	 * 检验是否上传附件
	 */
	boolean isUploadAttachments(HtglDto htglDto);
	
	/**
	 * 更新合同已付金额和付款中金额
	 */
	boolean updateHtglDtos(List<HtglDto> htglDtos);
	/**
	 * 统计订单个数
	 */
    List<Map<String,Object>> getCountOrdersByRq(HtglDto htglDto);
	/**
	 * 请购审核用时
	 */
	Map<String,Object> getPurchaseAuditTimeByRq(HtglDto htglDto);
	/**
	 * 请购审核通过到合同提交平均用时
	 */
	Map<String,Object> getPurchaseAuditToContractSubmit(HtglDto htglDto);
	/**
	 * 合同审核用时
	 */
	Map<String,Object> getContractAuditTimeByRq(HtglDto htglDto);
	/**
	 * 合同付款审核用时
	 */
	Map<String,Object> getContractPayAuditTimeByRq(HtglDto htglDto);
	/**
	 * 按部门统计合同金额
	 */
	List<Map<String,Object>> getContractAmountByRq(HtglDto htglDto);
	/**
	 * 合同单统计
	 */
	List<HtglDto> getCountStatistics(String year);
	/**
	 * 统计合同物料到货及时率
	 */
	List<Map<String,Object>> getMatterArrivePer(HtglDto htglhDto);
	/**
	 * 统计合同分类统计到货及时率
	 */
	List<Map<String,Object>> getContractClassArrivePer(HtglDto htglDto);
	/**
	 * 根据负责人统计到货及时率
	 */
	List<Map<String,Object>> getChargePerArrivePer(HtglDto htglDto);
	/**
	 * 根据供应商统计到货及时率
	 */
	List<Map<String,Object>> getSupplierArrivePer(HtglDto htglDto);
	/**
	 * 获取数量最多的供应商id
	 */
	String getSupplierID();
	/**
	 * 新增保存框架合同
	 */
    boolean addSaveFrameworkContract(HtglDto htglDto) throws BusinessException;
	/**
	 * 修改保存框架合同
	 */
	boolean modFrameworkSaveContract(HtglDto htglDto) throws BusinessException;
	/**
	 * 通过供应商获取未过期的框架合同
	 */
    HtglDto getContractByGysWithWgq(HtglDto htglDto);
	/**
	 * 获取未全部发票维护的合同
	 */
    List<HtglDto> getNotAllInvoice(HtglDto htglDto);
	/**
	 * 获取未全部付款维护的合同
	 */
	List<HtglDto> getNotAllPay(HtglDto htglDto);
	/**
	 * 获取存在库存的合同
	 */
	List<HtglDto> getExistStock(HtglDto htglDto);
	/**
	 * @Description: 获取补充合同信息
	 * @param htglDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.HtglDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/8/27 15:31
	 */
	List<HtglDto> queryByHtid(HtglDto htglDto);

	/**
	 * @Description: 获取补充合同信息
	 * @param htglDto
	 * @return com.matridx.igams.production.dao.entities.HtglDto
	 * @Author: 郭祥杰
	 * @Date: 2024/8/27 15:47
	 */
	HtglDto queryBchtglDto(HtglDto htglDto);

	/**
	 * @Description: 双章合同保存
	 * @param htglDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/11/11 13:16
	 */
	boolean saveSzht(HtglDto htglDto);

	Map<String,Object> queryOverTime(String sftx);

	List<HtglDto> queryOverTimeTable(HtglDto htglDto);

	List<HtglDto> getHtOverTimeTable(HtglDto htglDto);
	Map<String,Object> getOverTimeTableMap(String rqstart, String rqend);

	Map<String,Object> getGrOverTimeTableMap(HtglDto htglDto);
}
