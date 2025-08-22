package com.matridx.igams.research.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.research.dao.entities.YfglDto;
import com.matridx.igams.research.dao.entities.YfglModel;

@Mapper
public interface IYfglDao extends BaseBasicDao<YfglDto, YfglModel>{

	/**
	 * 根据研发ID查询附件表信息
	 */
	List<FjcfbDto> selectFjcfbDtoByYfid(String yfid);

}
