package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjqxDto;
import com.matridx.server.wechat.dao.entities.SjqxModel;

public interface ISjqxService extends BaseBasicService<SjqxDto, SjqxModel>{

	/**
	 * 新增送检权限
	 * @param sjqxDto
	 * @return
	 */
	boolean addSaveAudit(SjqxDto sjqxDto);

}
