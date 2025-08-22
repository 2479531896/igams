package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjwzxxDto;
import com.matridx.server.wechat.dao.entities.SjwzxxModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

@Mapper
public interface ISjwzxxDao extends BaseBasicDao<SjwzxxDto, SjwzxxModel>{

	/**
	 * 批量新增送检物种信息
	 * @param sjwzxxDtos
	 * @return
	 */
	boolean insertBysjwzxxDtos(List<SjwzxxDto> sjwzxxDtos);

	/**
	 * 批量删除送检物种信息
	 * @param sjwzxxDto
	 * @return
	 */
	int deleteBysjwzxxDto(SjwzxxDto sjwzxxDto);

	/**
	 * 送检物种类型统计(医生)
	 * @param sjxxDto
	 * @return
	 */
    List<SjwzxxDto> getSpeciesStatis(SjxxDto sjxxDto);
}
