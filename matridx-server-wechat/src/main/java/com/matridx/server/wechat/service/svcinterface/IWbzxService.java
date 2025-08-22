package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.WbzxDto;
import com.matridx.server.wechat.dao.entities.WbzxModel;

public interface IWbzxService extends BaseBasicService<WbzxDto, WbzxModel>{

	/**
	 * 根据资讯类型和资讯子类型获取微信资讯列表
	 * @param wbzxDto
	 * @return
	 */
	List<WbzxDto> getWsWechatNewsList(WbzxDto wbzxDto);
}
