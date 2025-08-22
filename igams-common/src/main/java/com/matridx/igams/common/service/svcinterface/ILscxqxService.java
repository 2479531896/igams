package com.matridx.igams.common.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.dao.entities.LscxqxDto;
import com.matridx.igams.common.dao.entities.LscxqxModel;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.service.BaseBasicService;

public interface ILscxqxService extends BaseBasicService<LscxqxDto, LscxqxModel>{
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
	 boolean toSelected(LscxqxDto lscxqxDto);
	
	/**
	 * 去除用户
	 */
	 boolean toOptional(LscxqxDto lscxqxDto);
	
	/**
	 * 根据Ids查询角色里已选用户
	 */
	 List<LscxqxDto> getYxyhList(UserDto userDto);
	

	
	/**
	 * 用户查看页面查询所有
	 */
	 List<LscxqxDto> getAllByYhid(LscxqxDto lscxqxDto);
	
	/**
	 * 通过yhid查询用户
	 */
	 List<LscxqxDto> queryByYhid(String yhid);

}
