package com.matridx.igams.web.service.svcinterface;

import java.util.List;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.entities.XtjsModel;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.YhjsDto;

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
	/**
	 * 获取系统角色列表
	 * @param xtjsList
	 * @param jSONDATA
	 * @return
	 */
    String installTree(List<XtjsDto> xtjsList, String jSONDATA);
	
	/**
	 * 通过角色ID查询到角色下面的用户
	 * @param
	 * @return
	 */
    List<XtyhDto> getyhmByjsid(XtjsDto xtjsDto);
	/**
	 * 修改角色仓库分类
	 * @param xtjsDto
	 * @return
	 */
    boolean updateCkqxByjsid(XtjsDto xtjsDto);
	/**
	 * 修改角色信息
	 * @param xtjsDto
	 * @return
	 */
    boolean updateJsxx(XtjsDto xtjsDto);
	
	/**
	 * Xtjs 查看所属人员机构
	 * @param yhjsDto
	 * @return
	 */
    List<YhjsDto> getAllJgxxByxtjs(YhjsDto yhjsDto);

	/**
	 * 修改角色信息
	 * @param xtjsDto
	 * @return
	 */
    List<XtjsDto> getPagedDtoList(XtjsDto xtjsDto);

	/**
	 * 获取角色列表(小程序)
	 * @param xtjsDto
	 * @return
	 */
    List<XtjsDto> getMiniXtjsList(XtjsDto xtjsDto);

	/**
	 * 复制保存角色信息
	 * @param xtjsDto
	 * @return
	 */
    boolean copySaveRole(XtjsDto xtjsDto);
}
