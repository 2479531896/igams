package com.matridx.igams.experiment.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.RwmbDto;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwModel;
import com.matridx.igams.experiment.dao.entities.XmjdxxDto;

@Mapper
public interface IXmjdrwDao extends BaseBasicDao<XmjdrwDto, XmjdrwModel>{

	/**
	 * 根据模板ID查询任务模板信息
	 */
	List<RwmbDto> selectRwmbByMbid(XmglDto xmgldto);

	/**
	 * 根据项目阶段ID查询项目阶段任务信息
	 */
	List<XmjdrwDto> selectTaskByXmjdid(String xmjdid);
	
	/**
	 * 查询子任务条数
	 */
	List<Map<String,String>> selectsum(XmjdrwDto xmjdrwDto);
	
	/**
	 * 删除任务
	 */
	boolean deleterw(XmjdrwDto xmjdrwDto);

	/**
	 * 项目任务重新排序
	 */
	int taskSort(XmjdrwDto xmjdrwDto);

	/**
	 * 项目任务重新排序
	 */
	int updateXmjdxx(List<XmjdrwDto> list);

	/**
	 * 修改任务阶段
	 */
	int updateXmjdByRwid(XmjdrwDto xmjdrwDto);
	
	/**
	 * 循环修改状态
	 */
	boolean updateZt(XmjdrwDto xmjdrwDto);
	
	/**
	 *修改gzgl列表任务状态
	 */
	boolean updateGzglZt(XmjdrwDto xmjdrwDto);
	
	/**
	 * 获取到负责人的钉钉id
	 */
	XmjdrwDto getYhmByFzr(String yhid);

	/**
	 * 根据项目阶段ID查询当前项目阶段任务信息
	 */
	List<XmjdrwDto> selectTaskByXmjdxx(XmjdxxDto xmjdxxDto);
	
	/**
	 * 项目研发列表查看项目详细信息
	 */
	List<XmjdrwDto> selectjdrwxx(XmglDto xmglDto);

	/**
	 * 根据项目阶段ID查询登录用户任务信息
	 */
	List<XmjdrwDto> selectMyTaskByXmjdxx(XmjdxxDto xmjdxxDto);
	
	/**
	 * 修改阶段
	 */
	boolean updateJdid(XmjdrwDto xmjdrwDto);
	
	/**
	 * 查询个人项目研发列表
	 */
	List<XmjdrwDto> getPagedSearchTaskList(XmjdrwDto xmjdrwDto);
	
	
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
	 * 项目研发列表转阶段时任务序号更新为阶段下已有任务的最大序号值加1
	 */
	int updateMaxxh(XmjdrwDto xmjdrwDto);
	
	/**
	 * 根据父任务ID删除相应的子任务
	 */
	int deleteZrw(XmjdrwDto xmjdrwDto);

	/**
	 * 新增项目阶段信息
	 */
	int addNewJdxx(XmjdrwDto xmjdrwDto);
	
	/**
	 * 通过项目查询当前任务所在阶段的序号是否为此项目中最后一个阶段
	 */
	List<XmjdrwDto> getXhIsLastJdXh(List<XmjdrwDto> list);
	
	/**
	 * 根据任务id查询阶段信息
	 */
	XmjdxxDto getjdxxByrwid(XmjdrwDto xmjdrwDto);
	
	/**
	 * 根据项目阶段id和任务id查询每个阶段的子任务
	 */
    List<XmjdrwDto> getListzrwbyjd(XmjdrwDto xmjdrwDto);
    
    /**
     * 项目研发列表根据任务ID删除子任务信息
     */
    int deleteXmyfzrws(XmjdrwDto xmjdrwDto);
    
    /**
     * 根据rwid查询项目研发列表任务信息
     */
    XmjdrwDto getSearchTaskRwDto(XmjdrwDto xmjdrwDto);

    /**
     * 查询任务模板不为空的子任务
     */
	List<String> selectSubTaskByRwid(XmjdrwDto xmjdrwDto);
	
	/**
	 * 查询当前任务以及该任务下的所有任务
	 */
	List<XmjdrwDto> getDqrwAndZrw(XmjdrwDto xmjdrwDto);
	
	/**
	 * 删除当前任务以及该任务下的所有任务
	 */
	int deleterwByRwid(XmjdrwDto xmjdrwDto);

	/**
	 * 获取任务所在项目的所有阶段
	 */
	List<XmjdrwDto> selectStageByRwid(XmjdrwDto xmjdrwDto);
	
	/**
	 * 项目研发列表查询所选任务以及所有子任务
	 */
	List<XmjdrwDto> getXzrwsAndZrws(XmjdrwDto xmjdrwDto);

	/**
	 * 查询项目阶段下全部任务
	 */
	List<XmjdrwDto> selectAllTaskByXmjdid(String xmjdid);

	/**
	 * 根据项目ID查询全部任务
	 */
	List<XmjdrwDto> getAllXmjdrwByXmid(String xmid);
}
