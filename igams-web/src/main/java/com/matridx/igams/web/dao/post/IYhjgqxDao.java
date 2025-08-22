package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.YhjgqxDto;
import com.matridx.igams.web.dao.entities.YhjgqxModel;

@Mapper
public interface IYhjgqxDao extends BaseBasicDao<YhjgqxDto, YhjgqxModel>{

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
    int deleteJgqxxxByYhid(YhjgqxDto yhjgqxDto);
	
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
	 * 多条添加用户机构权限
	 * @param yhjgqxList
	 * @return
	 */
    boolean insertList(List<YhjgqxDto> yhjgqxList);
	
	/**
	 * 通过yhid和jsid 查询机构
	 * @param yhjgqxDto
	 * @return
	 */
    List<YhjgqxDto>  getListByjsid(YhjgqxDto yhjgqxDto);
	
}
