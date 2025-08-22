package com.matridx.server.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjkjxxDto;
import com.matridx.server.wechat.dao.entities.SjkjxxModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

@Mapper
public interface ISjkjxxDao extends BaseBasicDao<SjkjxxDto, SjkjxxModel>{

	/**
	 * 根据送检信息新增送检快捷信息
	 * @param sjxxDto
	 * @return
	 */
	int inserBySjxxDto(SjxxDto sjxxDto);

}
