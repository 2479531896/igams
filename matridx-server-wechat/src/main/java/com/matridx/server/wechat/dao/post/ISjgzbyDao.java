package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjgzbyDto;
import com.matridx.server.wechat.dao.entities.SjgzbyModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;

@Mapper
public interface ISjgzbyDao extends BaseBasicDao<SjgzbyDto, SjgzbyModel>{
	/**
	 * 根据页面的输入列表登录数据库
	 * @param sjgzbyDtos
	 * @return
	 */
    int insertSjgzbyDtos(List<SjgzbyDto> sjgzbyDtos);
	
	/**
	 * 根据送检ID删除检测项目
	 * @param sjxxDto
	 */
	int deleteBySjxx(SjxxDto sjxxDto);
	
	/**
	 * 获取检测项目的String清单
	 * @param sjgzbyDto
	 * @return
	 */
    List<String> getStringList(SjgzbyDto sjgzbyDto);
}
