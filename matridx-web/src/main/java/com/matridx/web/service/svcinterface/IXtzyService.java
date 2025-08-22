package com.matridx.web.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.web.dao.entities.XtzyDto;
import com.matridx.web.dao.entities.XtzyModel;

public interface IXtzyService extends BaseBasicService<XtzyDto, XtzyModel>{

	
	/**
	 * 根据一级菜单ID获取下级菜单信息
	 * @param xtzyDto
	 * @return
	 */
    List<XtzyDto> getSubMenuByMenuId(XtzyDto xtzyDto);
	
	
	/**
	 * 获取菜单权限列表
	 * @param jsid
	 * @return
	 */
    List<XtzyDto> getMenuTreeList(String jsid);
	
	/**
	 * 组装菜单列表树
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param xtzyList
	 *@param JSONDATA
	 *@return
	 */
    String installTree(List<XtzyDto> xtzyList, String JSONDATA);
	
	/**
	 * 保存菜单数组数据
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param xtzyDto
	 *@return
	 * @throws BusinessException 
	 */
    boolean saveMenuArray(XtzyDto xtzyDto) throws BusinessException;
}
