package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.entities.XtjsModel;
import com.matridx.igams.web.dao.entities.XtyhDto;

@Mapper
public interface IXtjsDao extends BaseBasicDao<XtjsDto, XtjsModel>{
	/**
	 * 获取系统角色列表同时根据用户ID标识用户已有的角色信息
	 * @param xtjsDto
	 * @return
	 */
    List<XtjsDto> getAllJsAndChecked(XtjsDto xtjsDto);
	
	/**
	 * 获取除自己以外的角色列表
	 * @param xtjsModel
	 * @return
	 */
    List<XtjsModel> getModelListExceptSelf(XtjsModel xtjsModel);
	
	/**
	 * 通过角色ID查询到角色下面的用户
	 * @param xtjsDto
	 * @return
	 */
    List<XtyhDto> getyhmByjsid(XtjsDto xtjsDto);
	/**
	 * 修改角色仓库分类
	 * @param xtjsDto
	 * @return
	 */
    int updateCkqxByjsid(XtjsDto xtjsDto);

	/**
	 * 获取角色列表(小程序)
	 * @param xtjsDto
	 * @return
	 */
    List<XtjsDto> getMiniXtjsList(XtjsDto xtjsDto);
}
