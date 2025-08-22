package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.XmcyDto;
import com.matridx.igams.experiment.dao.entities.XmcyModel;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;

public interface IXmcyService extends BaseBasicService<XmcyDto, XmcyModel>{

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
	boolean addMemberToFxm(XmglDto z_xmglDto);

	/**
	 * 查询用户列表
	 */
	List<String> selectYhidGroup(List<String> xmids);

	/**
	 * 根据用户ID更新项目成员
	 */
	boolean updateMemberByYhids(XmglDto f_xmglDto);

	/**
	 * 项目成员编辑保存
	 */
	boolean editSaveMember(XmglDto xmglDto);
	
	/**
	 * 根据yhids添加项目成员
	 */
	int insertXmcyByYhids(XmglDto xmglDto);
	
	/**
	 * 根据xmid删除项目成员
	 */
	boolean deleteXmcyByXmid(XmglDto xmglDto);
	
	/**
	 * 根据项目id查询项目成员
	 */
	List<XmcyDto> getxmcyByxmid(XmjdrwDto xmjdrwDto);
}
