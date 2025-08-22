package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtglModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IHtglDao extends BaseBasicDao<HtglDto, HtglModel>{
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(HtglDto htglDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<HtglDto> getListForSearchExp(HtglDto htglDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<HtglDto> getListForSelectExp(HtglDto htglDto);
	/**
	 * 根据搜索条件获取审核导出条数
	 */
	int getCountForAuditingSearchExp(HtglDto htglDto);

	/**
	 * 从数据库分页获取审核导出数据
	 */
	List<HtglDto> getListForAuditingSearchExp(HtglDto htglDto);

	/**
	 * 从数据库分页获取审核导出数据
	 */
	List<HtglDto> getListForAuditingSelectExp(HtglDto htglDto);

	/**
	 * 合同修改方法(修改页面)
	 */
	int updateContract(HtglDto htglDto);

	/**
	 * 查询合同流水号
	 */
	String getContractSerial(@Param("prefix") String prefix,@Param("num") String num);
	
	/**
	 * 获取审核列表
	 */
	List<HtglDto> getPagedAuditHtgl(HtglDto htglDto);
	
	/**
	 * 审核列表
	 */
	List<HtglDto> getAuditListHtgl(List<HtglDto> list);
	
	/**
	 * 根据钉钉实例ID查询合同信息
	 */
	HtglDto getDtoByDdslid(HtglDto htglDto);

	/**
	 * 根据供应商查询合同
	 */
	List<HtglDto> getDtoListByGys(HtglDto htglDto);

	/**
	 * 根据钉钉ID获取审批人信息
	 */
	HtglDto getSprxxByDdid(String ddid);
	
	/**
	 * 根据审批人用户ID获取审批人角色信息
	 */
	List<HtglDto> getSprjsBySprid(String sprid);
	
	/**
	 * 将钉钉实例id置为null
	 */
	void updateDdslidToNull(HtglDto htglDto);
	
	/**
	 * 生成合同文档
	 */
	Map<String,Object> getContractMap(HtglDto htglDto);
	
	/**
	 * 获取合同明细
	 */
	List<Map<String,String>> getContractListMap(HtglDto htglDto);

	/**
	 * 根据内部编号和ID查询是否重复
	 */
	List<HtglDto> getListByNbbh(HtglDto htglDto);

	/**
	 * 获取请购完成ID
	 */
	List<String> isPurchaseComp(String htid);
	
	/**
	 * 获取请购完成ID
	 */
	boolean outgrowthContract (HtglDto htglDto);
	/**
	 * 获取合同数量
	 */
	List<Map<String, Object>> getContractMapByMon(HtglDto htglDto);

	/**
	 * 查询未完成到货的合同
	 */
	List<HtglDto> getPagedDtoKdhList(HtglDto htglDto);

	/**
	 * 查询未发票维护的合同
	 */
	List<HtglDto> getPageListContract(HtglDto htglDto);

	/**
	 * 更新合同管理付款信息
	 */
	boolean updateFkxxByList(List<HtglDto> htglDtos);
	
	/**
	 * 更新合同已付金额和付款中金额
	 */
	int updateHtglDtos(List<HtglDto> htglDtos);
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
	 * @param htglDto
	 
	 */
	List<Map<String,Object>> getMatterArrivePer(HtglDto htglDto);
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
	 * @description 通过供应商获取未过期的框架合同
	 */
	HtglDto getContractByGysWithWgq(HtglDto htglDto);
	/**
	 * @description 获取未全部发票维护的合同
	 */
	List<HtglDto> getNotAllInvoice(HtglDto htglDto);
	/**
	 * @description 获取未全部付款维护的合同
	 */
	List<HtglDto> getNotAllPay(HtglDto htglDto);
	/**
	 * @description 获取存在库存的合同
	 */
	List<HtglDto> getExistStock(HtglDto htglDto);

	/**
	 * @Description: 获取补充合同信息
	 * @param htglDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.HtglDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/8/27 15:30
	 */
	List<HtglDto> queryByHtid(HtglDto htglDto);

	/**
	 * @Description: 获取补充合同信息
	 * @param htglDto
	 * @return com.matridx.igams.production.dao.entities.HtglDto
	 * @Author: 郭祥杰
	 * @Date: 2024/8/27 15:44
	 */
	HtglDto queryBchtglDto(HtglDto htglDto);

	/**
	 * @Description: 获取补充合同总金额
	 * @param htglDto
	 * @return com.matridx.igams.production.dao.entities.HtglDto
	 * @Author: 郭祥杰
	 * @Date: 2024/8/29 14:56
	 */
	List<HtglDto> queryBchtZjeList(HtglDto htglDto);

	/**
	 * @Description: 获取补充合同金额
	 * @param htglDto
	 * @return com.matridx.igams.production.dao.entities.HtglDto
	 * @Author: 郭祥杰
	 * @Date: 2024/8/29 16:10
	 */
	HtglDto queryBchtZje(HtglDto htglDto);
	List<HtglDto> queryOrderByQgAndHt(HtglDto htglDto);

	List<HtglDto> queryOverTime(HtglDto htglDto);

	List<HtglDto> getPagedOverTimeList(HtglDto htglDto);
	List<Map<String,Object>> getOverTimeTableMap(HtglDto htglDto);
	String getTimeliness(HtglDto htglDto);
	List<HtglDto> getMonthTj(HtglDto htglDto);
	List<HtglDto> getTimeTj(HtglDto htglDto);
	List<HtglDto> getPercentageTj(HtglDto htglDto);
	List<HtglDto> getGrOverTimeTableMap(HtglDto htglDto);
}
