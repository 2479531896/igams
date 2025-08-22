package com.matridx.igams.experiment.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmglModel;

@Mapper
public interface IXmglDao extends BaseBasicDao<XmglDto, XmglModel>{
	
	/**
	 * 获取个人项目列表
	 */
	List<XmglDto> getPersionDtoList(XmglDto xmglDto);
	
	/**
	 * 删除项目或文件夹
	 */
	int deleteByXmid(XmglDto xmglDto);

	/**
	 * 根据项目ID修改父项目ID
	 */
	int updateFxmid(XmglDto xmglDto);
	
	/**
	 *根据输入内容查询系统用户信息
	 */
	List<XmglDto> selectXtyh(XmglDto xmglDto);

	/**
	 * 获取公开项目列表
	 */
	List<XmglDto> getPublicDtoList(XmglDto xmglDto);

	/**
	 * 彻底删除项目
	 */
	int completeDelProject(XmglDto xmglDto);

	/**
	 * 还原项目
	 */
	int recoverProject(XmglDto xmglDto);

	/**
	 * 还原项目到根目录
	 */
	int recoverProjecPath(XmglDto xmglDto);

	/**
	 * 查询父文件夹公开状态
	 */
	List<XmglDto> selectProjectStatus(XmglDto xmglDto);
	
	/**
	 * 修改父项目为公开状态
	 */
	int updateFxmOpenness(XmglDto xmglDto);

	/**
	 * 通过项目ID查询子项目列表
	 */
	List<XmglDto> selectByFxmid(XmglDto xmglDto);

	/**
	 * 修改项目为私有状态
	 */
	int updateToPrivate(XmglDto xmglDto);
	
	/**
	 * 查询父文件夹信息
	 */
	List<XmglDto> selectcatalogue(XmglDto xmglDto);

	/**
	 * 判断当前用户能否删除文件
	 */
	List<XmglDto> delPermission(XmglDto xmglDto);

	/**
	 * 查询未彻底删除子项目信息
	 */
	List<XmglDto> selectAllZxm(XmglDto xmglDto);
	
	/**
	 * 校验项目名称是否已存在
	 */
	List<XmglDto> selectXmByXmmc(XmglDto xmglDto);
}
