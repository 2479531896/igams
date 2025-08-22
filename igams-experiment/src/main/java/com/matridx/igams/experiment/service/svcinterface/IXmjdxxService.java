package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;
import com.matridx.igams.experiment.dao.entities.XmjdxxDto;
import com.matridx.igams.experiment.dao.entities.XmjdxxModel;

public interface IXmjdxxService extends BaseBasicService<XmjdxxDto, XmjdxxModel>{

	/**
	 * 根据项目ID查询阶段列表
	 */
	List<XmjdxxDto> selectStageByXmid(XmglDto xmglDto);



	/**
	 * 根据项目ID查询最小时间
	 */
	String selectMinTimeByXmid(XmglDto xmglDto);
	
	/**
	 * 查询最大的xh
	 */
	int getXh(XmjdxxDto xmjdxxDto);

	/**
	 * 项目阶段排序
	 */
	boolean stageSort(XmjdxxDto xmjdxxDto);
	
	/**
	 * 根据frwid查询子任务所在阶段
	 */
	List<XmjdxxDto> getJdsByFrwid(XmjdrwDto xmjdrwDto);

	/**
	 * 批量新增阶段
	 */
	boolean insertByDtos(List<XmjdxxDto> xmjdDtos);

	/**
	 * 编辑项目阶段信息
	 */
	boolean modSaveProjectStage(XmjdxxDto xmjdxxDto);

	/**
	 * 删除项目阶段
	 */
	boolean delStage(XmjdxxDto xmjdxxDto);
}
