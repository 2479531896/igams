package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WbcxModel;

public interface IWbcxService extends BaseBasicService<WbcxDto, WbcxModel>{

	/**
	 * 新增外部程序
	 * @param wbcxDto
	 * @return
	 */
	boolean addSave(WbcxDto wbcxDto);

	/**
	 * 修改外部程序
	 * @param wbcxDto
	 * @return
	 */
	boolean modSave(WbcxDto wbcxDto);

}
