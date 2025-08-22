package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.YhqdxxDto;
import com.matridx.igams.web.dao.entities.YhqdxxModel;

@Mapper
public interface IYhqdxxDao extends BaseBasicDao<YhqdxxDto, YhqdxxModel>{

	/**
	 * 查询最后一次签到时间
	 * @return
	 */
	YhqdxxDto selectLastTime();

	/**
	 * 保存签到信息
	 * @param yhqdxxDtos
	 * @return
	 */
	int insertByYhqdxxDtos(List<YhqdxxDto> yhqdxxDtos);

	/**
	 * 按时间范围查询签到信息
	 * @param yhqdxxDto
	 * @return
	 */
	List<YhqdxxDto> selectCheckinInfo(YhqdxxDto yhqdxxDto);

}
