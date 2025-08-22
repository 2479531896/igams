package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.web.dao.entities.JszyczbDto;
import com.matridx.igams.web.dao.entities.JszyczbModel;

@Mapper
public interface IJszyczbDao extends BaseBasicDao<JszyczbDto, JszyczbModel>{
	/**
	 * 根据角色ID获取该角色的各菜单权限
	 * @param qxModel
	 * @return
	 */
    List<QxModel> getQxModelList(QxModel qxModel);
	/**
	 * 根据角色ID获取该角色全部菜单权限
	 * @param jsid
	 * @return
	 */
    List<JszyczbDto> getListById(String jsid);

	/**
	 * 获取没有按钮的列表清单权限
	 * @param qxModel
	 * @return
	 */
    List<QxModel> getNoButtonMenu(QxModel qxModel);
}
