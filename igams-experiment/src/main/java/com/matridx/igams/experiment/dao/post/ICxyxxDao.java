package com.matridx.igams.experiment.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.CxyxxDto;
import com.matridx.igams.experiment.dao.entities.CxyxxModel;

@Mapper
public interface ICxyxxDao extends BaseBasicDao<CxyxxDto, CxyxxModel>{

	/**
	 * 新增测序仪信息
	 */
	boolean insertCxyFromOut(List<CxyxxDto> list);

	/**
	 * 更新测序仪信息的时候更新测序仪基础数据scbj
	 * @param list
	 * @return
	 */
	boolean updateCxyJcsj(List<Map<String,String>> list);

	List<CxyxxDto> getDtoByMcOrCxybh(CxyxxDto cxyxxDto);
}
