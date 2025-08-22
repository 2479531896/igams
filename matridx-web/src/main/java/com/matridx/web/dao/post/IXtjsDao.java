package com.matridx.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.web.dao.entities.XtjsDto;
import com.matridx.web.dao.entities.XtjsModel;

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
}
