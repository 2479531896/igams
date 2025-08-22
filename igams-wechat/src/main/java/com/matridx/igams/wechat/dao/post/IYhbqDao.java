package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.YhbqDto;
import com.matridx.igams.wechat.dao.entities.YhbqModel;

@Mapper
public interface IYhbqDao extends BaseBasicDao<YhbqDto, YhbqModel>{

	/**
	 * 根据标签ID删除用户信息
	 * @param yhbqDto
	 * @return
	 */
	boolean deleteByBqid(YhbqDto yhbqDto);

	/**
	 * 根据标签ID新增用户信息
	 * @param yhbqDto
	 * @return
	 */
	boolean insertByBqid(YhbqDto yhbqDto);

}
