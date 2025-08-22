package com.matridx.web.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.web.dao.entities.XtjsDto;
import com.matridx.web.dao.entities.XtjsModel;

public interface IXtjsService extends BaseBasicService<XtjsDto, XtjsModel>{
	/**
	 * 获取系统角色列表同时根据用户ID标识用户已有的角色信息
	 * @param yhid
	 * @return
	 */
    List<XtjsDto> getAllJsAndChecked(String yhid);
	
	/**
	 * 获取除自己以外的角色列表
	 * @param xtjsModel
	 * @return
	 */
    List<XtjsModel> getModelListExceptSelf(XtjsModel xtjsModel);
}
