package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.dao.entities.YhbqDto;
import com.matridx.server.wechat.dao.entities.YhbqModel;

public interface IYhbqService extends BaseBasicService<YhbqDto, YhbqModel>{

	/**
	 * 保存标签下用户信息
	 * @param yhbqDto
	 * @return
	 */
	boolean insertByTags(YhbqDto yhbqDto);

	/**
	 * 批量新增用户标签
	 * @param wxyhDto
	 * @return
	 */
	boolean insertByWxids(WxyhDto wxyhDto);

	/**
	 * 批量删除用户标签
	 * @param wxyhDto
	 * @return
	 */
	boolean deleteByWxids(WxyhDto wxyhDto);

}
