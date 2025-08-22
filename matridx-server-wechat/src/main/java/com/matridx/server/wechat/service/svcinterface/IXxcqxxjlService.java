package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.XxcqxxjlDto;
import com.matridx.server.wechat.dao.entities.XxcqxxjlModel;

public interface IXxcqxxjlService extends BaseBasicService<XxcqxxjlDto, XxcqxxjlModel>{

	/**
	 * 获取最近3条记录正确数量
	 * @param xxcqxxjlDto
	 * @return
	 */
	List<XxcqxxjlDto> getRecentCorrect(XxcqxxjlDto xxcqxxjlDto);	
	
}
