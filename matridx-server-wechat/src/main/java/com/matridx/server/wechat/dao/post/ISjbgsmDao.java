package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjbgsmDto;
import com.matridx.server.wechat.dao.entities.SjbgsmModel;

@Mapper
public interface ISjbgsmDao extends BaseBasicDao<SjbgsmDto, SjbgsmModel>{

	/**
	 * 根据送检id查询送检报告说明信息
	 * @param sjbgsmdto
	 * @return
	 */
	List<SjbgsmDto> selectSjbgBySjid(SjbgsmDto sjbgsmdto);
	
	/**
	 * 根据Dto删除信息
	 * @param sjbgsmDto
	 * @return
	 */
	int deleteBySjbgsmDto(SjbgsmDto sjbgsmDto);
}
