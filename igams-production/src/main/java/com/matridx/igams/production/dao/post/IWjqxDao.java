package com.matridx.igams.production.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.WjglDto;
import com.matridx.igams.production.dao.entities.WjqxDto;
import com.matridx.igams.production.dao.entities.WjqxModel;

@Mapper
public interface IWjqxDao extends BaseBasicDao<WjqxDto, WjqxModel>{

	/**
	 * 查询已有权限角色
	 */
	List<WjglDto> getSelectedJs(WjglDto wjglDto);

	/**
	 * 查询没有学习、查看权限角色
	 */
	List<WjglDto> getUnSelectedJs(WjglDto wjglDto);

	/**
	 * 根据文件ID删除权限信息
	 */
	int deleteByWjid(WjglDto wjglDto);

	/**
	 * 根据文件ID插入权限信息
	 */
	boolean insertByWjid(List<WjqxDto> wjqxList);

	/**
	 * 根据文件ID查询有权限用户
	 */
	List<WjglDto> selectXtyhByWjid(WjglDto wjglDto);

	/**
	 * 查询已有学习权限角色
	 */
	List<WjglDto> getStudyJs(WjglDto wjglDto);

	/**
	 * 查询已有查看权限角色
	 */
	List<WjglDto> getViewJs(WjglDto wjglDto);

	/**
	 * 根据文件ID获取文件权限信息
	 */
	List<WjqxDto> selectWjqxlist(WjglDto t_wjglDto);

	/**
	 * 查询角色下载权限
	 */
	int getXzCountByJs(WjglDto wjglDto);

	/**
	 * 查询角色文件权限信息
	 */
	List<WjglDto> getPermission(WjglDto wjglDto);

	/**
	 * 查询已有下载权限角色
	 */
	List<WjglDto> getDownloadJs(WjglDto wjglDto);

	/**
	 * 查询没有下载权限角色
	 */
	List<WjglDto> getUndownloadJs(WjglDto wjglDto);
	/**
	 * @description 分页查询角色
	 */
	List<WjglDto> getPagedRoles(WjglDto wjglDto);
}
