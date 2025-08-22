package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.JsgnqxDto;
import com.matridx.igams.web.dao.entities.JsgnqxModel;

@Mapper
public interface IJsgnqxDao extends BaseBasicDao<JsgnqxDto, JsgnqxModel>{
	/**
	 * list添加角色个人权限
	 * @param list
	 * @return
	 */
    boolean insertJsgnqx(List<JsgnqxDto> list);
	
	/**
	 * 查询当前角色的权限
	 * @param jsid
	 * @return
	 */
    List<JsgnqxDto> getListById(String jsid);
}
