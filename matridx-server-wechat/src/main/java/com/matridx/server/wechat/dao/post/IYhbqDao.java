package com.matridx.server.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.dao.entities.YhbqDto;
import com.matridx.server.wechat.dao.entities.YhbqModel;

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

	/**
	 * 根据用户ID和标签ID新增用户标签
	 * @param wxyhDto
	 * @return
	 */
	int insertByWxids(WxyhDto wxyhDto);

	/**
	 * 根据用户ID和标签ID删除用户标签
	 * @param wxyhDto
	 * @return
	 */
	int deleteByWxids(WxyhDto wxyhDto);

}
