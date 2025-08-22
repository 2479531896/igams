package com.matridx.igams.experiment.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.RwmbDto;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwModel;
import com.matridx.igams.experiment.dao.entities.XmjdxxDto;

public interface IXmjdrwService extends BaseBasicService<XmjdrwDto, XmjdrwModel>
{

	/**
	 * 根据模板ID查询任务模板信息
	 */
	List<RwmbDto> selectRwmbByMbid(XmglDto xmgldto);

	/**
	 * 根据项目阶段ID查询项目阶段任务信息
	 */
	List<XmjdrwDto> selectTaskByXmjdid(String xmjdid);

	/**
	 * 根据任务id查询任务信息
	 */
	XmjdrwDto getDtoById(String rwid);

	/**
	 * 删除任务
	 */
	boolean deleterw(XmjdrwDto xmjdrwDto);

	/**
	 * 项目任务重新排序
	 */
	boolean taskSort(XmjdrwDto xmjdrwDto);

	/**
	 * 修改阶段timen
	 */
	boolean modJdtime(XmjdrwDto xmjdrwDto);

	/**
	 * 修改任务
	 */
	boolean updateGzglAndXmrw(XmjdrwDto xmjdrwDto);

	/**
	 * 修改完成状态
	 */
	boolean updateJdrwZt(XmjdrwDto xmjdrwDto);

	/**
	 * 开始任务
	 */
	boolean StartTask(XmjdrwDto xmjdrwDto);

	/**
	 * 项目研发列表查看项目详细信息
	 */
	List<XmjdrwDto> selectjdrwxx(XmglDto xmglDto);

	/**
	 * 查询个人项目研发列表
	 */
	List<XmjdrwDto> getPagedSearchTaskList(XmjdrwDto xmjdrwDto);

	/**
	 * 新增保存项目研发信息
	 */
	boolean saveSearchProject(XmjdrwDto xmjdrwDto);

	/**
	 * 删除项目研发任务至回收站
	 */
	boolean deleteSearchProjecttoHsz(XmjdrwDto xmjdrwDto);

	/**
	 * 删除项目研发任务
	 */
	boolean deleteXmyfrws(XmjdrwDto xmjdrwDto);

	/**
	 * 根据xmids查询项目任务信息
	 */
	List<XmjdrwDto> selectXmrwxxs(XmjdrwDto xmjdrwDto);
	/**
	 * 根据xmids查询项目任务信息
	 */
	List<XmjdrwDto> getXmrwxxs(XmjdrwDto xmjdrwDto);

	/**
	 * 修改项目研发任务信息
	 */
	boolean modXmyfrw(XmjdrwDto xmjdrwDto);

	/**
	 * 项目研发列表阶段转换以及项目任务排序
	 */
	boolean ResearchtaskSort(XmjdrwDto xmjdrwDto);
	
	/**
	 * 查询子任务通过阶段
	 */
	List<XmjdxxDto> getzrwByjd(XmjdrwDto xmjdrwDto);
	
	/**
	 * 通过项目查询当前任务所在阶段的序号是否为此项目中最后一个阶段
	 */
	List<XmjdrwDto> getXhIsLastJdXh(List<XmjdrwDto> list);
	
	/**
	 * 新建项目阶段任务
	 */
	boolean addSaveProjectTask(XmjdrwDto xmjdrwDto);
	
    /**
     * 根据rwid查询项目研发列表任务信息
     */
    XmjdrwDto getSearchTaskRwDto(XmjdrwDto xmjdrwDto);
    
    /**
     * 根据frwid查询子任务信息
     */
    List<XmjdrwDto> getDtoListById(String frwid);
	/**
	 * 获取项目进度任务信息
	 */
    List<XmjdrwDto> selectStageByRwid(XmjdrwDto xmjdrwDto);
}
