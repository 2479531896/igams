package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.WxcdDto;
import com.matridx.igams.wechat.dao.entities.WxcdModel;

import java.util.List;

public interface IWxcdService extends BaseBasicService<WxcdDto, WxcdModel>{

	/**
	 * 查询微信菜单信息
	 * @return
	 */
	List<WxcdDto> getWxcdTreeList(WxcdDto wxcdDto);

	/**
	 * 组装微信菜单列表树
	 * @param wxcdList
	 * @param jSONDATA
	 * @return
	 */
	String installTree(List<WxcdDto> wxcdList, String jSONDATA);

	/**
	 * 修改菜单信息
	 * @param wxcdDto
	 * @return
	 */
	boolean modSaveWechatMenu(WxcdDto wxcdDto);

	/**
	 * 新增菜单信息
	 * @param wxcdDto
	 * @return
	 */
	boolean addSaveWechatMenu(WxcdDto wxcdDto);

	/**
	 * 删除菜单及子菜单
	 * @param wxcdDto
	 * @return
	 */
	boolean deleteByCdid(WxcdDto wxcdDto);

}
