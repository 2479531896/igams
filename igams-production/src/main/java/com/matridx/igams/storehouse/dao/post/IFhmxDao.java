package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.production.dao.entities.WlglDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.FhmxDto;
import com.matridx.igams.storehouse.dao.entities.FhmxModel;

import java.util.List;

@Mapper
public interface IFhmxDao extends BaseBasicDao<FhmxDto, FhmxModel>{

    /**
     * 获取明细数据
     */
	List<FhmxDto> getDtoMxList(String fhid);
    
    /**
	 * 获取明细列表 
	 * @param fhmxDto
	 * @return
	 */
	List<FhmxDto> getFhmxList(FhmxDto fhmxDto);
    
    /**
 	 * 批量新增明细
 	 * @return
 	 */
	int insertList(List<FhmxDto> fhmxList);
    
    /**
 	  *  查找明细信息
 	 * @param fhmxDto
 	 * @return
 	 */
	List<FhmxDto> getDtoAllByFhid(FhmxDto fhmxDto);
    /**
  	  * 批量更新发货明细
  	 * @return
  	 */
	int updateList(List<FhmxDto> fhmxList);
    
    /**
  	  * 分组查询
  	 * @return
  	 */
    List<FhmxDto> getDtoGroupBy(String fhid);

	/**
	 * 分组查询
	 * @param
	 * @return
	 */
	List<WlglDto> getDtoGroupByWlid(String fhid);
	/**
	 * 批量更新退货数量
	 * @param fhmxDtos
	 * @return
	 */
    boolean updateThsls(List<FhmxDto> fhmxDtos);
	/**
	 * 批量更新发货数量
	 * @param fhmxDtos
	 * @return
	 */
	boolean updateFhsls(List<FhmxDto> fhmxDtos);

	/**
	 * 获取明细列表 用于异常
	 * @param fhmxDto
	 * @return
	 */
	List<FhmxDto> getPagedForException(FhmxDto fhmxDto);
	/*
    	通过销售明细id获取发货信息
 	*/
	List<FhmxDto> getFhByXsmx(FhmxDto fhmxDto);
}
