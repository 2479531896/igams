package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.enums.OpennessTypeEnum;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmglModel;

public interface IXmglService extends BaseBasicService<XmglDto, XmglModel>{
	
	/**
	 * 获取个人项目列表
	 */
	List<XmglDto> getPersionDtoList(XmglDto xmglDto);
	
	/**
	 * 删除项目或文件夹
	 */
	boolean deleteByXmid(XmglDto xmglDto);

	/**
	 * 项目合并到文件夹
	 */
	boolean projectPacking(XmglDto xmglDto);

	/**
	 * 项目移动到上级目录
	 */
	boolean projectReturnTop(XmglDto xmglDto);
	
	/**
	 * 新增项目
	 */
	boolean addSaveXm(XmglDto xmglDto);
	
	/**
	 * 根据输入内容查询系统用户信息
	 */
	List<XmglDto> selectXtyh(XmglDto xmglDto);

	/**
	 * 获取公开项目列表
	 */
	List<XmglDto> getPublicDtoList(XmglDto xmglDto);

	/**
	 * 获取项目公开类型
	 */
	List<OpennessTypeEnum> getOpennessType();

	/**
	 * 彻底删除项目
	 */
	boolean completeDelProject(XmglDto xmglDto);

	/**
	 * 还原项目
	 */
	boolean recoverProject(XmglDto xmglDto);

	/**
	 * 判断项目父文件夹是否存在
	 */
	boolean confirmFxmDto(XmglDto xmglDto);

	/**
	 * 还原项目到根目录
	 */
	boolean recoverProjecPath(XmglDto xmglDto);

	/**
	 * 修改父项目为公开状态
	 */
	boolean updateFxmOpenness(XmglDto xmglDto);

	/**
	 * 新增文件夹
	 */
	boolean addSaveFolder(XmglDto xmglDto);

	/**
	 * 编辑项目保存
	 */
	boolean modSaveProject(XmglDto xmglDto);
	
	/**
	 * 查询父文件夹信息
	 */
	List<XmglDto> selectcatalogue(XmglDto xmglDto);

	/**
	 * 判断当前用户是否有删除权限
	 */
	boolean delPermission(XmglDto xmglDto);
	
	/**
	 * 校验项目名称是否已存在
	 */
	boolean selectXmByXmmc(XmglDto xmglDto);

}
