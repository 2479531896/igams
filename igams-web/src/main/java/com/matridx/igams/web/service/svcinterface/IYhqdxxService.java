package com.matridx.igams.web.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.YhqdxxDto;
import com.matridx.igams.web.dao.entities.YhqdxxModel;

public interface IYhqdxxService extends BaseBasicService<YhqdxxDto, YhqdxxModel>{

	/**
	 * 更新钉钉签到信息
	 * @param yhqdxxDto
	 * @param user
	 * @return
	 */
    boolean updateCheckinRecord(YhqdxxDto yhqdxxDto, User user);

	/**
	 * 更新钉钉签到信息并下载
	 * @param yhqdxxDto
	 * @param user
	 * @return
	 */
    String saveCheckinRecord(YhqdxxDto yhqdxxDto, User user);

}
