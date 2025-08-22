package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.WtxxDto;
import com.matridx.igams.common.dao.entities.WtxxModel;

public interface IWtxxService extends BaseBasicService<WtxxDto, WtxxModel>{
	/**
	 * 获取有效的指定的委托人
	 * @param wtbh	委托编号
	 * @param xtzy 系统资源
	 * @param str 受托人
	 */
	 User getWtr(String wtbh, String xtzy, String str);
}
