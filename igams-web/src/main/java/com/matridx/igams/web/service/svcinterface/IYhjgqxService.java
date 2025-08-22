package com.matridx.igams.web.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.YhjgqxDto;
import com.matridx.igams.web.dao.entities.YhjgqxModel;

public interface IYhjgqxService extends BaseBasicService<YhjgqxDto, YhjgqxModel>{

	/**
	 * 查询用户机构权限
	 * @return
	 */
    List<YhjgqxDto> getYhjgqxList();

	/**
	 * 根据Yhid删除用户机构权限信息
	 * @param yhjgqxDto
	 * @return
	 */
    boolean deleteJgqxxxByYhid(YhjgqxDto yhjgqxDto);
	
	/**
	 * 根据ids查询用户机构权限信息
	 * @param yhjgqxDto
	 * @return
	 */
    List<YhjgqxDto> selectYhqxjg(YhjgqxDto yhjgqxDto);
	
	/**
	 * 根据yhid查询当前用户的机构
	 * @param yhid
	 * @return
	 */
    YhjgqxDto getJgidByYhid(String yhid);
	
	/**
	 * 通过yhid和jsid 查询机构
	 * @param yhjgqxDto
	 * @return
	 */
    List<YhjgqxDto>  getListByjsid(YhjgqxDto yhjgqxDto);
}
