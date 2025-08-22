package com.matridx.igams.web.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.WbzyDto;
import com.matridx.igams.web.dao.entities.WbzyModel;

public interface IWbzyService extends BaseBasicService<WbzyDto, WbzyModel>{

	/**
	 * 获取小程序菜单权限列表 
	 * @param jsid
	 * @return
	 */
	List<WbzyDto> getMiniMenuTreeList(String jsid);
	
	/**
	 * 组装菜单列表树
	 *@param wbzyList
	 *@return
	 */
    String installMiniTree(List<WbzyDto> wbzyList);
	
	/**
	 * 保存菜单组数据
	 * @param wbzyDto
	 * @return
	 * @throws BusinessException
	 */
    boolean saveMiniMenuArray(WbzyDto wbzyDto) throws BusinessException;
}
