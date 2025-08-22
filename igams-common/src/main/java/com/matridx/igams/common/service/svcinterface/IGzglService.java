package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.GzglModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IGzglService extends BaseBasicService<GzglDto, GzglModel>{

	/**
	 * 根据文件审核插入数据
	 */
	 boolean insertDtoByWjsh(List<GzglDto> gzglDtos);

	/**
	 * 根据用户id查询所属单位未完成的任务
	 */
	List<GzglDto> getStatisDtoList(GzglDto gzglDto);

	/**
	 * 根据用户id查询部门任务
	 */
	List<GzglDto> getTabInfo(GzglDto gzglDto);

	/**
	 * 任务统计
	 */
	List<GzglDto> getTaskStatistics(GzglDto gzglDto);

	/**
	 * 任务部门统计
	 */
	List<GzglDto> getDepartTaskStatistics(GzglDto gzglDto);


	/**
	 * 进度提交保存
	 */
	 boolean progressSaveTask(GzglDto gzglDto,HttpServletRequest req);

	/**
	 * 更改任务日期
	 */
	boolean updateRwrqList(List<GzglDto> list);

	/**
	 * 根据工作ID查询任务
	 */
	GzglDto selectDtoBygzid(String gzid);

	/**
	 * 根据工作ID查询附件信息
	 */
	 List<FjcfbDto> selectFjcfbDtoByGzid(String gzid);
	
	/**
	 * 根据ids查询任务信息
	 */
	 List<GzglDto> selectDtoByIds(List<String> ids);

	/**
	 * 根据输入信息查询负责人
	 */
	 List<GzglDto> selectTaskFzr(GzglDto gzglDto);


	/**
	 * 新增培训任务
	 */
	 boolean insertDtobyfzr(GzglDto gzglDto);

	/**
	 * 根据ywid以及fzr查询任务
	 */
	List<GzglDto> verification(GzglDto gzglDto);

	/**
	 * 任务转交保存
	 */
	 boolean taskcareSaveTask(GzglDto gzglDto,HttpServletRequest req)throws BusinessException;

	/**
	 * 领取任务
	 * @param gzglDto
	 * @param isInProgress
	 * @param user
	 * @param req
	 * @return
	 * @throws BusinessException
	 */
	 boolean claimTask(GzglDto gzglDto,boolean isInProgress,User user,HttpServletRequest req)throws BusinessException;

	/**
	 * 查询负责人列表
	 */
	 List<GzglDto> getPagedFzrList(GzglDto gzglDto);

	/**
	 * 查询客户列表
	 */
	 List<GzglDto> getPagedKhList(GzglDto gzglDto);
	/**
	 * 分页查询培训工作管理信息列表
	 */
	List<GzglDto> getDtoTrainList(GzglDto gzglDto);
	/**
	 * 分页查询单个培训的已学习/未学习列表
	 */
	List<GzglDto> getPagedTrainList(GzglDto gzglDto);

	/**
	 * 查询任务确认列表
	 */
	 List<GzglDto> getPagedConfirmList(GzglDto gzglDto);

	/**
	 * 任务确认保存
	 */
	 boolean confirmSaveTask(GzglDto gzglDto,HttpServletRequest req);

	/**
	 * 根据业务ID删除工作任务
	 */
	 boolean deleteByYwid(List<String> ids);

	/**
	 * 任务批量确认保存
	 */
	 boolean batchconfirmSaveTask(GzglDto gzglDto);
	
	/*
	  查询任务到期时间
	 */
	// boolean timingTask();
	
	/**
	 * 查询当前期望完成时间
	 */
	List<GzglDto> getGzglByfzr(GzglDto gzglDto);
	
	/**
	 * 小程序分页查询用户列表
	 */
	List<GzglDto> getMiniFzrList(GzglDto gzglDto);
	/**
	 * 多条添加任务
	 */
	 boolean insertList(List<GzglDto> list);

	/**
	 * 确认转交
	 */
	 boolean updateConfirmPerson(GzglDto gzglDto, List<GzglDto> list, User user);

	/**
	 * 根据业务ID删除工作任务
	 */
	boolean deleteByYwids(GzglDto gzglDto);
	/**
	 * 查询未完成的工作任务
	 */
	List<GzglDto> getOverdueTasks(GzglDto gzglDto);
	/**
	 * 更新状态
	 */
    boolean updateGzglDtos(List<GzglDto> upGzglDtos);
	
	boolean insertGzgl(GzglDto gzglDto);
	/**
	 * 查询相应的任务日期
	 */
	GzglDto getRwrq(GzglDto gzglDto,String ryid);
	/**
	 * 查询任务履历
	 */
	List<GzglDto> getRwlyDtos(GzglDto gzglDto);
	/**
	 * 查询任务阶段日期
	 */
	List<GzglDto> getRwrqDtos(GzglDto gzglDto);
	boolean modRwrqByRwidAndXmjdid(GzglDto gzglDto);
	GzglDto getXmjdxx(String xmjdid);
	GzglDto getVerificationDto(GzglDto gzglDto);

    GzglDto getGzInfoByFzrAndYwid(GzglDto gzglDto);

	GzglDto getGzDtoByGzid(String gzid);
	/**
	 * 获取统计数据
	 */
	Map<String,String> getStatistics(GzglDto gzglDto);

	/**
	 * 获取已分发人员的情况
	 */
	List<GzglDto> selectDistributedDtos(GzglDto gzglDto);
	/**
	 * 获取任务名称
	 */
	List<GzglDto> selectTaskNames(GzglDto gzglDto);
	/**
	 * 获取机构
	 */
	List<GzglDto> selectInstitution(GzglDto gzglDto);

	GzglDto getYhssjgandjgxx(GzglDto gzglDto);
	/**
	 * 获取流水号
	 */
	String getJlbhSerial(GzglDto gzglDto);
	/**
	 * 获取培训信息
	 */
	List<GzglDto> getTrainInfo(GzglDto gzglDto);
	
	/**
	 * 获取工作任务统计
	 * @param gzglDto
	 * @return
	 */
    Map<String, Object> getHomePageTaskStatis(GzglDto gzglDto);
	
    /**
     * 调用主站删除相应任务信息
     * @param gzglDto
     * @param request
     * @return
     */
	boolean delete(GzglDto gzglDto,HttpServletRequest request);
	
	/**
	 * 根据工作ID任务ids
	 */
	List<GzglDto> getRwidList(GzglDto gzglDto);
	/*
		获取签到人员
	 */
    List<GzglDto> getDtoTrainSignInPeo(GzglDto gzglDto);
	/*
		删除签到工作记录
	 */
	boolean deleteByTrainSignIn(GzglDto gzglDto);

	/**
	 * 获取未完成任务
	 * @param gzglDto
	 * @return
	 */
	List<GzglDto> getConfirmList(GzglDto gzglDto);
	/*
        获取培训提醒部门负责人
     */
	List<Map<String,Object>> getRemindTrainInfo(GzglDto gzglDto);
	/*
		统计每个人距离期望完成时间还未完成的培训数量
	 */
	List<Map<String, Object>> getRemindTrainGroupPeo(GzglDto gzglDto);
	/*
		该机构下距离期望完成时间还未完成的培训列表
	 */
	List<Map<String, Object>> getRemindTrainList(GzglDto gzglDto);
	/**
	 * 删除
	 */
	boolean deleteDto(GzglDto gzglDto);

	String getDtoTrainXh(GzglDto gzglDto);
	GzglDto getDtoTrainFjid(GzglDto gzglDto);
	String getStringTrainFjid(GzglDto gzglDto);
}
