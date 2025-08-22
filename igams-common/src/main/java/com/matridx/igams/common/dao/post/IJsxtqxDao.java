package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.JsxtqxDto;
import com.matridx.igams.common.dao.entities.JsxtqxModel;

@Mapper
public interface IJsxtqxDao extends BaseBasicDao<JsxtqxDto, JsxtqxModel>{

	/**
	 * 根据角色ID获取该角色的系统权限
	 */
	List<JsxtqxDto> getQxModelList(JsxtqxDto qxDto);
	/**
	 * 通过jsid查询系统类型集合
	 * @param list
	 * @return
	 */
	boolean insertJsxtqx(List<JsxtqxDto> list);
}
