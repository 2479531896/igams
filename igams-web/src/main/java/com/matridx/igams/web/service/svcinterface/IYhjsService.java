package com.matridx.igams.web.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.web.dao.entities.YhjsModel;

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
	
	/**
	 * 根据Ids查询角色里已选用户
	 * @return
	 */
    List<YhjsDto> getYxyhList(XtyhDto xtyhDto);
	
	/**
	 * 通过jsid查询下属用户
	 * @param yhjsDto
	 * @return
	 */
    List<YhjsDto> selectYhidByJsid(YhjsDto yhjsDto);
	
	/**
	 * 用户查看页面查询所有
	 * @param yhjsDto
	 * @return
	 */
    List<YhjsDto> getAllByYhid(YhjsDto yhjsDto);
	
	/**
	 * 根据用户ID查询权限信息
	 * @param yhid
	 * @return
	 */
    List<YhjsDto> getDtoListById(String yhid);
}
