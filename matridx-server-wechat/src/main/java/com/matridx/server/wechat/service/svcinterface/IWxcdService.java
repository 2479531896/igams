package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.WxcdDto;
import com.matridx.server.wechat.dao.entities.WxcdModel;

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

	/**
	 * 推送微信菜单至微信
	 * @param wxcdDto
	 * @return
	 */
	boolean createMenu(WxcdDto wxcdDto);

	/**
	 * 上传永久素材(图片)
	 * @param xtszDto
	 * @param wbcxdm
	 * @return
	 */
	boolean uploadSaveMaterial(XtszDto xtszDto, String wbcxdm);

}
