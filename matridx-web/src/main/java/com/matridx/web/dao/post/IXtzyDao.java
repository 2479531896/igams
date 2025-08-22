package com.matridx.web.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.web.dao.entities.XtzyDto;
import com.matridx.web.dao.entities.XtzyModel;

@Mapper
public interface IXtzyDao extends BaseBasicDao<XtzyDto, XtzyModel>{
	/**
	 * 根据菜单ID获取子菜单信息
	 * @param xtzyDto
	 * @return
	 */
    List<XtzyDto> getDtoListByMenuId(XtzyDto xtzyDto);
	
	/**
	 * 获取菜单权限列表
	 * @param jsid
	 * @return
	 */
    List<XtzyDto> getMenuTreeList(String jsid);
	
	/**
	 * 删除系统资源权限
	 *@描述：
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param jsid
	 */
    void deleteXtzyqx(String jsid);

	/**
	 * 批量保存系统资源
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param param
	 *@return
	 */
    int batchSaveXtzyqx(Map<String, Object> param);
	/**
	 * 批量保存系统资源
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param list
	 *@return
	 */
    int insertXtzyqxList(List<Map<String, String>> list);
}
