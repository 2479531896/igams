package com.matridx.igams.common.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.dao.entities.LscxqxDto;
import com.matridx.igams.common.dao.entities.LscxqxModel;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.post.ILscxqxDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ILscxqxService;



@Service
public class LscxqxServiceImpl extends BaseBasicServiceImpl<LscxqxDto, LscxqxModel, ILscxqxDao> implements ILscxqxService{
	/**
	 * 获取可选用户
	 */
	public List<UserDto> getPagedOptionalList(UserDto userDto){
		return dao.getPagedOptionalList(userDto);
	}
	/**
	 * 获取可选角色
	 */
	public List<UserDto> getPagedOptionalRoleList(UserDto userDto){
		return dao.getPagedOptionalRoleList(userDto);
	}
	/**
	 * 获取已选用户
	 */
	public List<UserDto> getPagedSelectedList(UserDto userDto){
		return dao.getPagedSelectedList(userDto);
	}
	/**
	 * 添加用户
	 */
	public boolean toSelected(LscxqxDto lscxqxDto){
		return dao.toSelected(lscxqxDto);
	}
	/**
	 * 去除用户
	 */
	public boolean toOptional(LscxqxDto lscxqxDto){
		return dao.toOptional(lscxqxDto);
	}
	/**
	 * 根据Ids查询角色里已选用户
	 */
	public List<LscxqxDto> getYxyhList(UserDto userDto){
		return dao.getYxyhList(userDto);
	}


	@Override
	public List<LscxqxDto> getAllByYhid(LscxqxDto lscxqxDto) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<LscxqxDto> queryByYhid(String yhid) {
		return dao.queryByYhid(yhid);
	}


}
