package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjdwxxDto;
import com.matridx.server.wechat.dao.entities.SjdwxxModel;

@Mapper
public interface ISjdwxxDao extends BaseBasicDao<SjdwxxDto, SjdwxxModel>{

	/**
	 * 查询送检单位列表
	 * @return
	 */
	List<SjdwxxDto> selectSjdwList();
	     
	/**
	* 查询送检单位
	* @return
	*/
    List<SjdwxxDto> getSjdwxx();
	/**
	* 查询所有送检单位
	* @return
	*/
    List<SjdwxxDto> getAllSjdwxx();
	 
	/**
	* 删除送检单位信息
	* @return
	*/
    boolean delSjdwxx();
		
	/**
	*  添加历史送检单位
	* @param sjdwxxDto
	* @return
	*/
    boolean insertSjdwxx(SjdwxxDto sjdwxxDto);

}
