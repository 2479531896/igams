package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.BqglDto;
import com.matridx.igams.wechat.dao.entities.BqglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

}
