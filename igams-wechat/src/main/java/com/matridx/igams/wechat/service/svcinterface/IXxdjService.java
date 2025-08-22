package com.matridx.igams.wechat.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.XxdjDto;
import com.matridx.igams.wechat.dao.entities.XxdjModel;

public interface IXxdjService extends BaseBasicService<XxdjDto, XxdjModel>{
	

	/**
	 * 阿里服务器同步登记信息到本地服务器
	 * @param xxdjDto
	 * @return
	 */
    boolean checkXxdjDto(XxdjDto xxdjDto);
}
