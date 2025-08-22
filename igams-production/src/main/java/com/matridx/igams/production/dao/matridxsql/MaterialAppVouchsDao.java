package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.MaterialAppVouchsDto;
import com.matridx.igams.production.dao.entities.MaterialAppVouchsModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MaterialAppVouchsDao extends BaseBasicDao<MaterialAppVouchsDto, MaterialAppVouchsModel> {
	/**
	 * 批量新增
	 */
	int insertList(List<MaterialAppVouchsDto> materialAppVouchsDtos);
	
	/**
	 * 批量修改
	 */
	int updateList(List<MaterialAppVouchsDto> materialAppVouchsDtos);
	/**
	 * 根据id 查询
	 */
	MaterialAppVouchsDto getMatersInfo(String autoId);
	/**
	 * 批量修改
	 * @param materialAppVouchsDtos
	 
	 */
	int updateFOutQuantitys(List<MaterialAppVouchsDto> materialAppVouchsDtos);
}
