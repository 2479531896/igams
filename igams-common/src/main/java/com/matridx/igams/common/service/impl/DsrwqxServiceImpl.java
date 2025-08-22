package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.DsrwqxDto;
import com.matridx.igams.common.dao.entities.DsrwqxModel;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.post.IDsrwqxDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDsrwqxService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DsrwqxServiceImpl extends BaseBasicServiceImpl<DsrwqxDto, DsrwqxModel, IDsrwqxDao> implements IDsrwqxService {
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
	public boolean toSelected(DsrwqxDto dsrwqxDto){
		return dao.toSelected(dsrwqxDto);
	}
	/**
	 * 去除用户
	 */
	public boolean toOptional(DsrwqxDto dsrwqxDto){
		return dao.toOptional(dsrwqxDto);
	}
	/**
	 * 根据Ids查询角色里已选用户
	 */
	public List<DsrwqxDto> getYxyhList(UserDto userDto){
		return dao.getYxyhList(userDto);
	}


	@Override
	public List<DsrwqxDto> getAllByYhid(DsrwqxDto dsrwqxDto) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<DsrwqxDto> queryByYhid(String yhid) {
		return dao.queryByYhid(yhid);
	}


}
