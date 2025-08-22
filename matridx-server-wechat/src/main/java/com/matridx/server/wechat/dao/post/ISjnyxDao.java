package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjnyxDto;
import com.matridx.server.wechat.dao.entities.SjnyxModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

@Mapper
public interface ISjnyxDao extends BaseBasicDao<SjnyxDto, SjnyxModel>{

	/**
	 * 批量保存送检耐药性信息
	 * @param sjnyxDtos
	 * @return
	 */
	boolean insertBysjnyxDtos(List<SjnyxDto> sjnyxDtos);

	/**
	 * 根据送检ID查询耐药信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjnyxDto> getNyxBySjid(SjxxDto sjxxDto);
	
	/**
	 * 根据Dto删除耐药信息
	 * @param sjnyxDto
	 * @return
	 */
	int deleteBySjnyxDto(SjnyxDto sjnyxDto);
}
