package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjgzbyDto;
import com.matridx.igams.wechat.dao.entities.SjgzbyModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjgzbyDao extends BaseBasicDao<SjgzbyDto, SjgzbyModel>{

	/**
	 * 根据送检ID删除病原
	 * @param sjxxDto
	 */
	int deleteBySjxx(SjxxDto sjxxDto);

	/**
	 * 根据页面的输入列表登录数据库
	 * @param sjgzbyDtos
	 * @return
	 */
    int insertSjgzbyDtos(List<SjgzbyDto> sjgzbyDtos);
	

	/**
	 * 根据页面的输入列表登录数据库
	 * @param sjgzbyDtos
	 * @return
	 */
    int insertSjgzby(List<SjgzbyDto> sjgzbyDtos);

	/**
	 * 根据送检id查询关注病原
	 * @param sjid
	 * @return
	 */
    List<String> getGzby(String sjid);
	

}
