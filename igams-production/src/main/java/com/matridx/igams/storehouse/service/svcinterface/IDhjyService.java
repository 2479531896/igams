package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.DhjyDto;
import com.matridx.igams.storehouse.dao.entities.DhjyModel;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

public interface IDhjyService extends BaseBasicService<DhjyDto, DhjyModel>{
	/**
	 * 获取到货检验列表 
	 * @param dhjyDto
	 * @return
	 */
	List<DhjyDto> getPagedDtoList(DhjyDto dhjyDto);
	/**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
    int getCountForSearchExp(DhjyDto dhjyDto, Map<String, Object> params);

	/**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
	int getCountForGoodsSearchExp(DhjyDto dhjyDto, Map<String, Object> params);
	/**
	 * 获取检验单号里的流水号（当前日期+3位序号）
	 * @return
	 */
    String getInspectionDh();
	/**
	 * 新增检验信息
	 * @param dhjydto 到货检验主数据
	 * @param hwxxJson 操作的货物信息
	 * @param delids 移除的数据
	 * @param xgry 操作人员
	 * @return
	 */
	Map<String, Object> addInspection(DhjyDto dhjydto, String hwxxJson,String delids,String xgry) throws BusinessException;
	/**
	 * 高级修改保存
	 * @param dhjydto 到货检验主数据
	 * @param hwxxJson 操作的货物信息
	 * @param delids 移除的数据
	 * @param xgry 操作人员
	 * @return
	 */
	Map<String, Object> advancedModSaveInspection(DhjyDto dhjydto, String hwxxJson,String delids,String xgry) throws BusinessException;
	/**
	 * 查询检验车是否有保存记录
	 * @param yhid
	 * @param lbcskz1
	 * @return
	 */
	DhjyDto getSaveRecord(String yhid, String lbcskz1);
	/**
	 * 删除到货检验信息
	 * @param dhjydto
	 * @return
	 */
	Map<String, Object> delInspectionGoods(DhjyDto dhjydto);
	/**
	 * 废弃到货检验数据
	 * @param dhjydto
	 * @return
	 */
	Map<String, Object> abandonedInspectionGoods(DhjyDto dhjydto);
	/**
	 * 获取到货检验审核信息
	 * @param dhjyDto
	 * @return
	 */
	List<DhjyDto> getPagedAuditDhjy(DhjyDto dhjyDto);
	
	/**
	 * 	自动生成领料单
	 * @param dhjydto 到货检验主数据
	 * @param hwxxJson 操作的货物信息
	 * @return
	 */
	Map<String, Object> addLlxx(DhjyDto dhjydto, String hwxxJson);
	
	/**
	 * 根据到货检验ids获取到货检验信息（共通页面）
	 * @param dhjyDto
	 * @return
	 */
    List<DhjyDto> getCommonDtoListByDhjyids(DhjyDto dhjyDto);
	/**
	 * 检验列表审核状态统计
	 */
    List<DhjyDto> getAuditStatistics(String year);
	/**
	 * 获取统计年份
	 */
	List<DhjyDto> getYearGroup();
	/**
	 * @description 获取检验物料列表
	 */
	List<DhjyDto> getPagedWlDtoList(DhjyDto dhjyDto);
	/*
		获取检验结果统计
	 */
	List<Map<String,Object>> getInspectionResultStatistics(String year);
	/*
		获取检验物料类别占比
	 */
	List<Map<String,Object>> getInspectionCategoryProportion(String rqstart, String rqend);

	boolean backdropSaveInspectionGoods(HwxxDto hwxxDto);
}
