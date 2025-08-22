package com.matridx.web.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.web.dao.entities.XtyhDto;
import com.matridx.web.dao.entities.YhjsDto;
import com.matridx.web.dao.entities.YhjsModel;

public interface IYhjsService extends BaseBasicService<YhjsDto, YhjsModel>{

	
	/**
	 * 获取可选用户
	 *@param xtyhDto
	 *@return
	 */
    List<XtyhDto> getPagedOptionalList(XtyhDto xtyhDto);
	
	/**
	 * 获取已选用户
	 *@param xtyhDto
	 *@return
	 */
    List<XtyhDto> getPagedSelectedList(XtyhDto xtyhDto);
	
	/**
	 * 添加用户
	 *@param yhjsDto
	 *@return
	 */
    boolean toSelected(YhjsDto yhjsDto);
	
	/**
	 * 去除用户
	 *@param yhjsDto
	 *@return
	 */
    boolean toOptional(YhjsDto yhjsDto);
}
