package com.matridx.igams.production.dao.gmsql;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.PU_AppVouchDto;
import com.matridx.igams.production.dao.entities.PU_AppVouchModel;
import com.matridx.igams.production.dao.entities.PU_AppVouchsDto;


@Mapper
public interface IGMAppVouchDao extends BaseBasicDao<PU_AppVouchDto,PU_AppVouchModel>{

	/**
	 * 将请购单信息插入sqlserver数据库

     */
    PU_AppVouchDto insertPUAppVouch(Map<String, Object> map);
	
	/**
	 * 将请购单明细信息插入sqlserver数据库

     */
    PU_AppVouchsDto insertPUAppVouchs(PU_AppVouchsDto pu_appvouchsDto);
	
	
	/**
	 * 更新U8中请购单信息
	 * @param pu_appvouchDto
	 
	 */
    boolean updatePUAppVouch(PU_AppVouchDto pu_appvouchDto);
	
	/**
	 * 更新U8中请购明细信息
	 * @param pu_appvouchsDto
	 
	 */
    boolean updatePUAppVouchs(PU_AppVouchsDto pu_appvouchsDto);

}
