package com.matridx.igams.web.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.JsgnqxDto;
import com.matridx.igams.web.dao.entities.JsgnqxModel;
import com.matridx.igams.web.dao.entities.XtjsDto;

public interface IJsgnqxService extends BaseBasicService<JsgnqxDto, JsgnqxModel>{
	/**
	 * list添加角色个人权限
	 * @param xtjsDto
	 * @return
	 */
    boolean insertJsgnqx(XtjsDto xtjsDto);
	
	/**
	 * 查询当前角色的权限
	 * @param jsid
	 * @return
	 */
    List<JsgnqxDto> getListById(String jsid);
}
