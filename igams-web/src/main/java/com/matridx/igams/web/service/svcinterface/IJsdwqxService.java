package com.matridx.igams.web.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.JsdwqxDto;
import com.matridx.igams.web.dao.entities.JsdwqxModel;
import com.matridx.igams.web.dao.entities.XtjsDto;

public interface IJsdwqxService extends BaseBasicService<JsdwqxDto, JsdwqxModel>{

	/**
	 * 查询机构列表(已选)
	 * @param jsdwqxDto
	 * @return
	 */
    List<JsdwqxDto> getPagedYxJgxxList(JsdwqxDto jsdwqxDto);

	/**
	 * 查询机构列表(已选)(不分页)
	 * @param jsdwqxDto
	 * @return
	 */
    List<JsdwqxDto> getYxJgxxList(JsdwqxDto jsdwqxDto);
	
	/**
	 * 添加角色机构
	 * @param jsdwqxDto
	 * @return
	 */
    boolean toSelectedJg(JsdwqxDto jsdwqxDto);
	
	/**
	 * 去除角色机构
	 * @param jsdwqxDto
	 * @return
	 */
    boolean toOptionalJg(JsdwqxDto jsdwqxDto);

	/**
	 * 查询角色单位权限
	 * @param xtjsDto
	 * @return
	 */
    Map<String, List<JsdwqxDto>> getPermission(XtjsDto xtjsDto);

	/**
	 * 添加角色机构权限
	 * @param xtjsDto
	 * @return
	 */
    boolean updateByJsid(XtjsDto xtjsDto);

	/**
	 * 新增角色单位权限
	 * @param xtjsDto
	 * @return
	 */
	boolean insertByJsid(XtjsDto xtjsDto);
}
