package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.YhbqDto;
import com.matridx.igams.wechat.dao.entities.YhbqModel;

public interface IYhbqService extends BaseBasicService<YhbqDto, YhbqModel>{

	/**
	 * 保存标签下用户信息
	 * @param yhbqDto
	 * @return
	 */
	boolean insertByTags(YhbqDto yhbqDto);

}
