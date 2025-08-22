package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgglModel;
import com.matridx.igams.production.dao.entities.QgmxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IQgglDao extends BaseBasicDao<QgglDto, QgglModel>{
	
	/**
	 * 获取请购管理审核ID列表
	 */
	List<QgglDto> getPagedAuditQggl(QgglDto qgglDto);
	
	/**
	 * 审核列表
	 */
	List<QgglDto> getAuditListQggl(List<QgglDto> list);

	/**
	 * 根据单据号查询采购单信息(可用于判断采购单号是否重复)
	 */
	QgglDto getDtoByDjh(QgglDto qgglDto);
	
	/**
	 * 获取默认申请人信息
	 */
	QgglDto getMrsqrxxByYhid(QgglDto qgglDto);
	
	/**
	 * 获取申请部门信息
	 */
	QgglDto getSqbmxx(QgglDto qgglDto);
	
	/**
	 * 根据钉钉实例ID获取在钉钉进行请购审批的请购单信息
	 */
	QgglDto getDtoByDdslid(QgglDto qgglDto);
	
	/**
	 * 获取钉钉审批人信息
	 */
	QgglDto getSprxxByDdid(String ddid);
	
	/**
	 * 根据审批人用户ID获取角色信息
	 */
	List<QgglDto> getSprjsBySprid(String sprid);
	
	/**
	 * 设置钉钉实例ID为null
	 */
	void updateDdslidToNull(QgglDto qgglDto);
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(QgglDto qgglDto);

	/**
	 * 从数据库分页获取导出数据
	 */
	List<QgglDto> getListForSearchExp(QgglDto qgglDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<QgglDto> getListForSelectExp(QgglDto qgglDto);

	/**
	 * 查询已有流水号
	 */
	String getDjhSerial(String prefix);
	/**
	 *内网查看
	 */
	List<QgglDto> getQgList(QgglDto qgglDto);

	/**
	 * 批量更新请购完成标记
	 */
	int updateWcbjs(QgglDto qgglDto);
	
	/**
	 * 根据请购ids获取请购信息
	 */
	List<QgglDto> getDtoListByIds(QgglDto qgglDto);
	
	/**
	 * 生成请购文档
	 */
	Map<String,Object> getPurchaseMap(QgglDto qgglDto);
	
	/**
	 * 获取请购明细
	 */
	List<Map<String,Object>> getPurchaseListMap(QgglDto qgglDto);

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
	 *根据物料分组查询项目大类项目编码
	 */
	List<QgglDto> queryByWlid(QgglDto qgglDto);
	
	/**
	 *根据请购明细id更新完成标记
	 */
	int updateByQgmxid(QgglDto qgglDto);



	/**
	 * 行政请购列表数据
	 */
	List<QgglDto> getPagedListAdministration(QgglDto qgglDto);

	/**
	 * 行政请购库存列表数据
	 */
	List<QgglDto> getPagedListAdminStock(QgglDto qgglDto);

	/**
	 * 报销请购单
	 */
	boolean baoxiaoQggl(QgglDto qgglDto);

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
	int getCountForAuditingSearchExp(QgglDto qgglDto);

	/**
	 * 从数据库分页获取审核导出数据
	 */
	List<QgglDto> getListForAuditingSearchExp(QgglDto qgglDto);

	/**
	 * 从数据库分页获取审核导出数据
	 */
	List<QgglDto> getListForAuditingSelectExp(QgglDto qgglDto);
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
	int getCountForCgAuditingSearchExp(QgglDto qgglDto);

	List<QgglDto> getListForCgAuditingSearchExp(QgglDto qgglDto);

	List<QgglDto> getListForCgAuditingSelectExp(QgglDto qgglDto);
	List<String> selectXqSpSlIds(QgglDto qgglDto);

	List<QgglDto> queryOverTime(QgglDto qgglDto);

	boolean updateSftxList(List<QgglDto> list);

	List<QgglDto> getPagedOverTimeList(QgglDto qgglDto);

	int updateDispose(QgglDto qgglDto);
	List<Map<String, Object>> getOverTimeTable(QgglDto qgglDto);
	String getTimeliness(QgglDto qgglDto);
	List<QgglDto> getMonthTj(QgglDto qgglDto);
	List<QgglDto> getTimeTj(QgglDto qgglDto);
	List<QgglDto> getPercentageTj(QgglDto qgglDto);
}
