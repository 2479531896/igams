package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.wechat.dao.entities.WxcdDto;

public interface IWeChatService {
	
	/**
	 * 创建菜单
	 * @param wxcdDto
	 * @return
	 */
    boolean createMenu(WxcdDto wxcdDto);
}
