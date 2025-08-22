
  package com.matridx.igams.production.dao.matridxsql;
  
  import java.util.List;

import org.apache.ibatis.annotations.Mapper;
  
  import com.matridx.igams.common.dao.BaseBasicDao; 
  import com.matridx.igams.production.dao.entities.PO_PodetailsDto; 
  import com.matridx.igams.production.dao.entities.PO_PodetailsModel;
  
  @Mapper
  public interface PO_PodetailsDao extends BaseBasicDao<PO_PodetailsDto,PO_PodetailsModel>{
	/**
	 * 获取ID最大值
	 */
	Integer getMax(PO_PodetailsDto pO_PodetailsDto);

	/**
	 * 获取序号最大值
	 */
	Integer getXhMax(PO_PodetailsDto pO_PodetailsDto);
	
	/**
	 * 批量更新入库信息
	 */
	int updateRkxx(List<PO_PodetailsDto> pO_PodetailsDtos);

	int updatePo(PO_PodetailsDto po_podetailsDto);
		/*
			修改多
		 */
      int updatePos(PO_PodetailsDto poPodetailsDto);
  }
 