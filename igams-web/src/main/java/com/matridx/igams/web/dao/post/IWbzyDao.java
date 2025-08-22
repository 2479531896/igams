package com.matridx.igams.web.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.WbzyDto;
import com.matridx.igams.web.dao.entities.WbzyModel;

@Mapper
public interface IWbzyDao extends BaseBasicDao<WbzyDto, WbzyModel>{

	/**
	 * 获取小程序菜单权限列表 
	 * @param jsid
	 * @return
	 */
	List<WbzyDto> getMiniMenuTreeList(String jsid);
	
	/**
	 * 根据角色id删除外部资源权限
	 * @param jsid
	 * @return
	 */
	boolean deleteWbzyqx(String jsid);
	
	/**
	 * 批量保存外部资源权限
	 * @param param
	 * @return
	 */
    int batchSaveWbzyqx(Map<String, Object> param);
}
