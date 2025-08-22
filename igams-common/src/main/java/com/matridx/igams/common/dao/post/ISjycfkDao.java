package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.SjycfkDto;
import com.matridx.igams.common.dao.entities.SjycfkModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjycfkDao extends BaseBasicDao<SjycfkDto, SjycfkModel>{

	/**
	 * 根据异常ID获取送检异常反馈信息
	 * sjycfkDto
	 * 
	 */
	List<SjycfkDto> getDtoByYcid(SjycfkDto sjycfkDto);
	
	/**
	 * 获取该送检信息下的所有异常的评论数
	 * sjycfkDto
	 * 
	 */
	List<SjycfkDto> getExceptionPls(SjycfkDto sjycfkDto);
	
	/**
	 * 小程序获取异常反馈信息，父反馈ID为空 
	 * sjycfkDto
	 * 
	 */
	List<SjycfkDto> getMiniDtoByYcid(SjycfkDto sjycfkDto);
	
	/**
	 * 小程序根据根ID获取异常反馈信息
	 * sjycfkDto
	 * 
	 */
	List<SjycfkDto> getDtosByGid(SjycfkDto sjycfkDto);
	
	/**
	 * 根据fkid查找该评论下的所有子评论
	 * sjycfkDto
	 * 
	 */
	List<SjycfkDto> getZplByFkid(SjycfkDto sjycfkDto);

	void sendDingMessage(SjycfkDto sjycfkDto);
}
