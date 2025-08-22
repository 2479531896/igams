package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.AA_BatchPropertyDto;
import com.matridx.igams.production.dao.entities.AA_BatchPropertyModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AA_BatchPropertyDao extends BaseBasicDao<AA_BatchPropertyDto,AA_BatchPropertyModel>{
	/**
	 * 批量新增
	 */
	int insertBatchPs(List<AA_BatchPropertyDto> aA_BatchPropertyDtos);
	
	/**
	 * 根据物料编码追溯号查询
	 */
	List<AA_BatchPropertyDto> queryByInvAndBat(List<AA_BatchPropertyDto> aA_BatchPropertyDtos);
}
