package com.matridx.igams.experiment.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.XmcyDto;
import com.matridx.igams.experiment.dao.entities.XmcyModel;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;

@Mapper
public interface IXmcyDao extends BaseBasicDao<XmcyDto, XmcyModel>{

	/**
	 * 查询项目已有成员
	 */
	List<XmcyDto> selectSelMember(XmglDto xmglDto);

	/**
	 * 查询项目未选用户
	 */
	List<XmcyDto> selectUnSelMember(XmglDto xmglDto);

	/**
	 * 将子项目的成员更新至父项目
	 */
	int addMemberToFxm(XmglDto z_xmglDto);

	/**
	 * 查询用户列表
	 */
	List<String> selectYhidGroup(List<String> xmids);

	/**
	 * 根据Ids新增项目成员
	 */
	int insertXmcyByYhids(XmglDto f_xmglDto);

	/**
	 * 根据项目ID删除项目成员
	 */
	boolean deleteXmcyByXmid(XmglDto f_xmglDto);

	/**
	 * 查询子项目所有用户列表
	 */
	List<String> selectYhidsByXmid(XmglDto xmglDto);

	/**
	 * 查询项目已有成员
	 */
	List<String> selectSelYhids(XmglDto xmglDto);
	
	/**
	 * 根据项目id查询项目成员
	 */
	List<XmcyDto> getxmcyByxmid(XmjdrwDto xmjdrwDto);

}
