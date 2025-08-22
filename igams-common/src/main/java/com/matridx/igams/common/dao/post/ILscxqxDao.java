package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.LscxqxDto;
import com.matridx.igams.common.dao.entities.LscxqxModel;
import com.matridx.igams.common.dao.entities.UserDto;

@Mapper
public interface ILscxqxDao extends BaseBasicDao<LscxqxDto, LscxqxModel>{
	
	/**
	 * 获取可选用户
	 *userDto
	 *
	 */
	 List<UserDto> getPagedOptionalList(UserDto userDto);

	/**
	 * 获取可选角色
	 */
	 List<UserDto> getPagedOptionalRoleList(UserDto userDto);
	
	/**
	 * 获取已选用户
	 *userDto
	 *
	 */
	 List<UserDto> getPagedSelectedList(UserDto userDto);
	
	/**
	 * 添加用户
	 *lscxqxDto
	 *
	 */
	 boolean toSelected(LscxqxDto lscxqxDto);
	
	/**
	 * 去除用户
	 *lscxqxDto
	 *
	 */
	 boolean toOptional(LscxqxDto lscxqxDto);
	
	/**
	 * 根据Ids查询角色里已选用户
	 * 
	 */
	 List<LscxqxDto> getYxyhList(UserDto userDto);
	/**
	 * 通过yhid查询用户
	 * yhid
	 * 
	 */
	 List<LscxqxDto> queryByYhid(String yhid);


}
