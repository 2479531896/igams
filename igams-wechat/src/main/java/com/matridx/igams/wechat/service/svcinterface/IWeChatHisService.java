package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.wechat.dao.entities.SjdwxxDto;

public interface IWeChatHisService {
	/**
	 * 推送
	 * @param sjdwxxDto
	 * @return
	 */
    boolean creatHis(SjdwxxDto sjdwxxDto);
}
