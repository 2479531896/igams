package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjlcznDto;
import com.matridx.server.wechat.dao.entities.SjlcznModel;

public interface ISjlcznService extends BaseBasicService<SjlcznDto, SjlcznModel>{

	/**
	 * 添加送检临床指南(MQ同步)
	 * @param sjlcznDto
	 * @return
	 */
    boolean insertSjlczn(SjlcznDto sjlcznDto);
}
