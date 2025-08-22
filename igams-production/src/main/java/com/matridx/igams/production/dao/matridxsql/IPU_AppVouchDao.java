package com.matridx.igams.production.dao.matridxsql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.PU_AppVouchDto;
import com.matridx.igams.production.dao.entities.PU_AppVouchModel;
import com.matridx.igams.production.dao.entities.PU_AppVouchsDto;
import com.matridx.igams.production.dao.entities.QgmxDto;


@Mapper
public interface IPU_AppVouchDao extends BaseBasicDao<PU_AppVouchDto,PU_AppVouchModel>{

	/**
	 * 将请购单信息插入sqlserver数据库
	 */
	PU_AppVouchDto insertPUAppVouch(Map<String,Object> map);
	
	/**
	 * 将请购单明细信息插入sqlserver数据库
	 */
	PU_AppVouchsDto insertPUAppVouchs(PU_AppVouchsDto pu_appvouchsDto);
	
	
	/**
	 * 更新U8中请购单信息
	 */
	boolean updatePUAppVouch(PU_AppVouchDto pu_appvouchDto);
	
	/**
	 * 更新U8中请购明细信息
	 */
	boolean updatePUAppVouchs(PU_AppVouchsDto pu_appvouchsDto);

	/**
	 * 批量删除请购明细
	 */
	void deletePUAppVouchs(List<QgmxDto> delQgmxDtos);
	
	/**
	 * 批量更新入库数量
	 */
	int updateIReceivedQTY(List<PU_AppVouchsDto> PU_AppVouchsDtos);
	/**
	 * 将委外请购单信息插入sqlserver数据库
	 */
	PU_AppVouchDto insertPUAppVouchForWW(Map<String, Object> data);
	/**
	 * 将委外请购单明细信息插入sqlserver数据库
	 */
	int insertPUAppVouchsListForWW(List<PU_AppVouchsDto> pu_appVouchsDtos);
	/**
	 * 批量更新合同引用数量
	 */
    int updateIReceivedQTYAndIReceivedNum(List<PU_AppVouchsDto> pu_appVouchsDtos);
}
