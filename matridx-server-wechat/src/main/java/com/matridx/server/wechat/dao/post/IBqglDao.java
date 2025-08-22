package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.BqglDto;
import com.matridx.server.wechat.dao.entities.BqglModel;

@Mapper
public interface IBqglDao extends BaseBasicDao<BqglDto, BqglModel>{

	/**
	 * 查询全部标签
	 * @return
	 */
	List<BqglDto> selectAll();

	/**
	 * 根据标签列表删除信息
	 * @param bqglDtos
	 * @return
	 */
	boolean deleteByBqglDtos(List<BqglDto> bqglDtos);

	/**
	 * 根据标签列表插入信息
	 * @param bqglDtos
	 * @return
	 */
	boolean insertByBqglDtos(List<BqglDto> bqglDtos);
	
	/**
	 * 通过微信公众号查询标签
	 * @param bqglDto
	 * @return
	 */
    List<BqglDto> selectTag(BqglDto bqglDto);
}
