package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.IA_ST_UnAccountVouchDto;
import com.matridx.igams.production.dao.entities.IA_ST_UnAccountVouchModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IA_ST_UnAccountVouchDao extends BaseBasicDao<IA_ST_UnAccountVouchDto,IA_ST_UnAccountVouchModel>{
	/**
	 * 批量新增U8记账表(采购入库)
	 */
	int insertAccountV01s(List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos);
	
	/**
	 * 批量新增U8记账表(其他入库，调拨入库)
	 */
	int insertAccountV08s(List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos);
	
	/**
	 * 批量新增U8记账表(其他出库，调拨出库)
	 */
	int insertAccountV09s(List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos);
	
	/**
	 * 批量新增U8记账表(成品入库)
	 */
	int insertAccountV10s(List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos);
	
	/**
	 * 批量新增U8记账表(领料出库)
	 */
	int insertAccountV11s(List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos);
	
	/**
	 * 批量新增U8记账表(销售出库)
	 * @param 
	 
	 */
	int insertAccountV32s(List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos);
}
