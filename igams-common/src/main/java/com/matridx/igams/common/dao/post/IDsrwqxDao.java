package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.DsrwqxDto;
import com.matridx.igams.common.dao.entities.DsrwqxModel;
import com.matridx.igams.common.dao.entities.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDsrwqxDao extends BaseBasicDao<DsrwqxDto, DsrwqxModel>{
	
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
	 boolean toSelected(DsrwqxDto dsrwqxDto);
	
	/**
	 * 去除用户
	 *lscxqxDto
	 *
	 */
	 boolean toOptional(DsrwqxDto dsrwqxDto);
	
	/**
	 * 根据Ids查询角色里已选用户
	 * 
	 */
	 List<DsrwqxDto> getYxyhList(UserDto userDto);
	/**
	 * 通过yhid查询用户
	 * yhid
	 * 
	 */
	 List<DsrwqxDto> queryByYhid(String yhid);


}
