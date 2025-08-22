package com.matridx.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.web.dao.entities.JszyczbDto;
import com.matridx.web.dao.entities.JszyczbModel;

@Mapper
public interface IJszyczbDao extends BaseBasicDao<JszyczbDto, JszyczbModel>{
	/**
	 * 根据角色ID获取该角色的各菜单权限
	 * @param qxModel
	 * @return
	 */
    List<QxModel> getQxModelList(QxModel qxModel);
}
