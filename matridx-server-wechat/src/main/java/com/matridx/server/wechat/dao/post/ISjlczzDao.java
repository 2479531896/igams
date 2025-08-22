package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjlczzDto;
import com.matridx.server.wechat.dao.entities.SjlczzModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

@Mapper
public interface ISjlczzDao extends BaseBasicDao<SjlczzDto, SjlczzModel>{

	/**
	 * 根据送检信息新增临床症状
	 * @param sjlczzDtos
	 * @return
	 */
	int insertSjlczzDtos(List<SjlczzDto> sjlczzDtos);

	/**
	 * 根据送检ID删除临床症状
	 * @param sjxxDto
	 */
	boolean deleteBySjxx(SjxxDto sjxxDto);
	
	/**
	 * 获取检测项目的String清单
	 * @param sjlczzDto
	 * @return
	 */
    List<String> getStringList(SjlczzDto sjlczzDto);

	/**
	 * 根据送检ID查询送检临床症状
	 * @param sjxxDto
	 * @return
	 */
	List<SjlczzDto> selectLczzBySjid(SjxxDto sjxxDto);
}
