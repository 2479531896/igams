package com.matridx.igams.storehouse.dao.post;

import java.util.List;
import java.util.Map;

import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.DhjyDto;
import com.matridx.igams.storehouse.dao.entities.DhjyModel;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IDhjyDao extends BaseBasicDao<DhjyDto, DhjyModel>{
	/**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
	int getCountForSearchExp(DhjyDto dhjyDto);

	/**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
	int getCountForGoodsSearchExp(DhjyDto dhjyDto);
	
	/**
	 * 从数据库分页获取导出数据
	 * @return
	 */
	List<DhjyDto> getListForSearchExp(DhjyDto dhjyDto);
	/**
	 * 从数据库分页获取导出数据
	 * @return
	 */
	List<DhjyDto> getListForGoodsSearchExp(DhjyDto dhjyDto);
	
	/**
	 * 从数据库分页获取导出数据
	 * @return
	 */
	List<DhjyDto> getListForSelectExp(DhjyDto dhjyDto);
	/**
	 * 从数据库分页获取导出数据
	 * @return
	 */
	List<DhjyDto> getListForGoodsSelectExp(DhjyDto dhjyDto);
	/**
	 * 获取检验单号流水号
	 * @return
	 */
	String getInspectionDh();
	/**
	 * 查询检验车是否存在保存还未提交的记录
	 * @param yhid
	 * @param lbcskz1
	 * @return
	 */
	String getSaveRecord(@Param("yhid") String yhid, @Param("lbcskz1") String lbcskz1);
	/**
	 * 将到货检验标记为已删除
	 * @param dhjydto
	 * @return
	 */
	int updatedelflag(DhjyDto dhjydto);

	List<DhjyDto> getPagedAuditDhjy(DhjyDto dhjyDto);

	List<DhjyDto> getAuditListDhjy(List<DhjyDto> dhjyList);
	
	/**
	 * 根据到货检验ids获取到货检验信息（共通页面）
	 * @param dhjyDto
	 * @return
	 */
	List<DhjyDto> getCommonDtoListByDhjyids(DhjyDto dhjyDto);
	
	/**
	 * 根据检验id查找list
	 * @param dhjyid
	 * @return
	 */
	List<DhjyDto> getListByDhjyid(String dhjyid);
	/**
	 * 根据到货检验ids获取采购红冲信息
	 * @param dto
	 * @return
	 */
    List<DhjyDto> getCgHcxxByDhjyids(HwxxDto dto);
		/**
	 * 检验列表审核状态统计
	 * @param year
	 * @return
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
}
