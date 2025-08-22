package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.JsdwqxDto;
import com.matridx.igams.web.dao.entities.JsdwqxModel;
import com.matridx.igams.web.dao.entities.XtjsDto;

@Mapper
public interface IJsdwqxDao extends BaseBasicDao<JsdwqxDto, JsdwqxModel>{

	/**
	 * 查询机构列表(已选)
	 * @param jsdwqxDto
	 * @return
	 */
    List<JsdwqxDto> getPagedYxJgxxList(JsdwqxDto jsdwqxDto);
	/**
	 * 查询机构列表(已选)(不分页)
	 * @param jsdwqxDto
	 * @return
	 */
    List<JsdwqxDto> getYxJgxxList(JsdwqxDto jsdwqxDto);
	
	/**
	 * 添加角色机构
	 * @param jsdwqxDto
	 * @return
	 */
    boolean toSelectedJg(JsdwqxDto jsdwqxDto);
	
	/**
	 * 去除角色机构
	 * @param jsdwqxDto
	 * @return
	 */
    boolean toOptionalJg(JsdwqxDto jsdwqxDto);

	/**
	 * 查询角色单位权限
	 * @param xtjsDto
	 * @return
	 */
    List<JsdwqxDto> getPermission(XtjsDto xtjsDto);

	/**
	 * 根据角色ID删除角色机构权限信息
	 * @param xtjsDto
	 * @return
	 */
    int deleteByJsid(XtjsDto xtjsDto);

	/**
	 * 新增角色机构权限
	 * @param jsdwqxList
	 * @return
	 */
    boolean insertByJsid(List<JsdwqxDto> jsdwqxList);
}
