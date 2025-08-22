package com.matridx.igams.sample.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.YblyDto;
import com.matridx.igams.sample.dao.entities.YblyModel;

@Mapper
public interface IYblyDao extends BaseBasicDao<YblyDto, YblyModel>{

	/**
	 * 根据标本Ids修改状态
	 */
	int updateZtByIds(YblyDto yblyDto);

	/**
	 * 根据列表查询信息
	 */
	List<YblyDto> selectByDtos(List<YblyDto> yblyDtos);

}
