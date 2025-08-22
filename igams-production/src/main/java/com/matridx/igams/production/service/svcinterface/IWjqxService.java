package com.matridx.igams.production.service.svcinterface;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.WjglDto;
import com.matridx.igams.production.dao.entities.WjqxDto;
import com.matridx.igams.production.dao.entities.WjqxModel;

public interface IWjqxService extends BaseBasicService<WjqxDto, WjqxModel>{

	/**
	 * 查询已有权限角色
	 */
	List<WjglDto> getSelectedJs(WjglDto wjglDto);

	/**
	 * 查询没有权限角色
	 */
	List<WjglDto> getUnSelectedJs(WjglDto wjglDto);

	/**
	 * 根据文件ID删除权限信息
	 */
	void deleteByWjid(WjglDto wjglDto);

	/**
	 * 插入文件权限信息
	 */
	boolean insertByWjid(WjglDto wjglDto);

	/**
	 * 修改文件权限信息
	 */
	boolean updateByWjid(WjglDto wjglDto);

	/**
	 * 根据文件ID查询有权限用户
	 */
	List<WjglDto> selectXtyhByWjid(WjglDto wjglDto);

	/**
	 * 查询已有学习权限角色
	 */
	List<WjglDto> getStudyJs(WjglDto wjglDto);
	
	/**
	 * 查询已有学习权限角色拼接成字符串
	 */
	String SelectStudyJs(WjglDto wjglDto);

	
	/**
	 * 查询已有查看权限角色
	 */
	List<WjglDto> getViewJs(WjglDto wjglDto);
	
	/**
	 * 查询已有查看权限角色拼接成字符串
	 */
	String SelectViewJs(WjglDto wjglDto);

	/**
	 * 根据文件ID获取文件权限信息
	 */
	List<WjqxDto> selectWjqxlist(WjglDto t_wjglDto);

	/**
	 * 新增文件权限
	 */
	boolean insertByWjqxDtos(List<WjqxDto> wjqxDtos);

	/**
	 * 查询是否有下载权限
	 */
	boolean isDownload(WjglDto wjglDto);

	/**
	 * 获取角色文件权限
	 */
	Map<String, List<WjglDto>> getPermission(WjglDto wjglDto, HttpServletRequest request);

	/**
	 * 获取文件权限人员信息
	 */
	WjglDto getPermissionStr(WjglDto wjglDto, HttpServletRequest request);


	/**
	 * 查询已有下载权限角色
	 */
	List<WjglDto> getDownloadJs(WjglDto wjglDto);

	/**
	 * 查询没有下载权限角色
	 */
	List<WjglDto> getUndownloadJs(WjglDto wjglDto);

	/**
	 * 查询已有下载权限角色拼接成字符串
	 */
	String SelectDownloadJs(WjglDto wjglDto);
	/**
	 * @description 分页查询角色
	 */
    List<WjglDto> getPagedRoles(WjglDto wjglDto);
}
