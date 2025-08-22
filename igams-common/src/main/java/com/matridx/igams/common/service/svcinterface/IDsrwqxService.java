package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.DsrwqxDto;
import com.matridx.igams.common.dao.entities.DsrwqxModel;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IDsrwqxService extends BaseBasicService<DsrwqxDto, DsrwqxModel>{
	/**
	 * 获取可选用户
	 */
	List<UserDto> getPagedOptionalList(UserDto userDto);

	/**
	 * 获取可选角色
	 */
	List<UserDto> getPagedOptionalRoleList(UserDto userDto);

	/**
	 * 获取已选用户
	 */
	List<UserDto> getPagedSelectedList(UserDto userDto);

	/**
	 * 添加用户
	 */
	boolean toSelected(DsrwqxDto dsrwqxDto);

	/**
	 * 去除用户
	 */
	boolean toOptional(DsrwqxDto dsrwqxDto);

	/**
	 * 根据Ids查询角色里已选用户
	 */
	List<DsrwqxDto> getYxyhList(UserDto userDto);



	/**
	 * 用户查看页面查询所有
	 */
	List<DsrwqxDto> getAllByYhid(DsrwqxDto dsrwqxDto);

	/**
	 * 通过yhid查询用户
	 */
	List<DsrwqxDto> queryByYhid(String yhid);
}
