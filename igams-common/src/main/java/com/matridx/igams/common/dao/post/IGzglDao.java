package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.GzglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IGzglDao extends BaseBasicDao<GzglDto, GzglModel>{

	/**
	 * 根据文件审核插入数据
	 * gzglDtos
	 * 
	 */
	int insertDtoByWjsh(List<GzglDto> gzglDtos);

	/**
	 * 更改任务日期
	 * list
	 * 
	 */
	int updateRwrqList(List<GzglDto> list);

	/**
	 * 根据工作ID查询附件信息
	 * gzid
	 * 
	 */
	List<FjcfbDto> selectFjcfbDtoByGzid(String gzid);

	/**
	 * 任务统计
	 * 
	 * 
	 */
	List<GzglDto> getTaskStatistics(GzglDto gzglDto);

	/**
	 * 任务部门统计
	 * 
	 * 
	 */
	List<GzglDto> getDepartTaskStatistics(GzglDto gzglDto);

	/**
	 * 查询负责人机构id
	 * gzglDto
	 * 
	 */
	GzglDto getYhssjg(GzglDto gzglDto);

	/**
	 * 根据ids查询任务信息
	 * ids
	 * 
	 */
	List<GzglDto> selectDtoByIds(List<String> ids);

	/**
	 * 根据输入信息查询负责人
	 * gzglDto
	 * 
	 */
	List<GzglDto> selectTaskFzr(GzglDto gzglDto);

	/**
	 * 查询负责人列表
	 * gzglDto
	 * 
	 */
	List<GzglDto> getPagedFzrList(GzglDto gzglDto);

	/**
	 * 查询客户列表
	 * gzglDto
	 * 
	 */
	List<GzglDto> getPagedKhList(GzglDto gzglDto);

	/**
	 * 查询任务确认列表
	 * 
	 */
	List<GzglDto> getPagedConfirmList(GzglDto gzglDto);
	/**
	 * 分页查询培训工作管理信息列表
	 * 
	 */
	List<GzglDto> getDtoTrainList(GzglDto gzglDto);

	/**
	 * 分页查询单个培训的已学习/未学习列表
	 * 
	 */
	List<GzglDto> getPagedTrainList(GzglDto gzglDto);

	/**
	 * 最终确认通过修改状态
	 * gzglDto
	 * 
	 */
	boolean updateZt(GzglDto gzglDto);
	/**
	 * 最终确认通过修改考试总分
	 * gzglDto
	 * 
	 */
	boolean updateZf(GzglDto gzglDto);


	/**
	 * 阶段确认通过修改确认人员
	 * gzglDto
	 * 
	 */
	boolean updateQrry(GzglDto gzglDto);

	/**
	 * 退回申请人时修改原数据状态
	 * gzglDto
	 * 
	 */
	boolean updateInitZt(GzglDto gzglDto);

	/**
	 * 根据负责人（用户ID）查询机构ID
	 * fzr
	 * 
	 */
	GzglDto getJgidByFzr(String fzr);

	/**
	 * 批量修改任务信息
	 * gzglDtos
	 * 
	 */
	boolean updateByGzglDtoList(List<GzglDto> gzglDtos);

	/**
	 * 根据业务ID删除工作任务
	 * ywids
	 * 
	 */
	boolean deleteByYwid(List<String> ywids);

	/**
	 * 根据业务ID删除工作任务
	 * gzglDto
	 * 
	 */
	boolean deleteByYwids(GzglDto gzglDto);
	
	/**
	 * 根据工作ID查询任务
	 * gzid
	 * 
	 */
	GzglDto selectDtoBygzid(String gzid);

	/**
	 * 根据用户id查询所属单位未完成的任务
	 * 
	 * 
	 */
	List<GzglDto> getStatisDtoList(GzglDto gzglDto);

	/**
	 * 根据用户id查询部门任务
	 * 
	 * 
	 */
	List<GzglDto> getTabInfo(GzglDto gzglDto);


	/**
	 * 根据ywid以及fzr查询任务
	 * gzglDto
	 * 
	 */
	List<GzglDto> verification(GzglDto gzglDto);

	
	/**
	 * 任务转发新增
	 * t_gzglDtos
	 * 
	 */
	boolean insertDtobyfzr(GzglDto t_gzglDtos);

	/**
	 * 查询系统用户信息
	 * yhid
	 * 
	 */
	GzglDto getXtyhByYhid(String yhid);

	/**
	 * 批量最终确认通过修改状态
	 * gzglDto
	 * 
	 */
	boolean updateZts(GzglDto gzglDto);

	/**
	 * 批量退回时修改原数据状态
	 * gzglDto
	 * 
	 */
	boolean updateInitZts(GzglDto gzglDto);
	
	/**
	 * 根据rwid删除工作任务
	 * gzglDto
	 * 
	 */
	boolean deletegzrwByrwid(GzglDto gzglDto);
	
	/**
	 * 根据rwids删除工作任务
	 * gzglDto
	 * 
	 */
	boolean deletegzrwByrwids(GzglDto gzglDto);
	
	/**
	 * 查询个人任务未完成的任务
	 * 
	 */
	List<GzglDto> selectGzglByzt();
	
	/**
	 * 查询出来所有逾期的条数
	 * 
	 */
	List<Map<String,String>> getYqGzgl();
	
	/**
	 * 查询当前期望完成时间
	 * gzglDto
	 * 
	 */
	List<GzglDto> getGzglByfzr(GzglDto gzglDto);
	
	/**
	 * 小程序分页查询用户列表
	 * gzglDto
	 * 
	 */
	List<GzglDto> getMiniFzrList(GzglDto gzglDto);

	/**
	 * 多条添加任务
	 * list
	 * 
	 */
	 boolean insertList(List<GzglDto> list);
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
	GzglDto getRwrq(GzglDto gzglDto);
	boolean modRwrqByRwidAndXmjdid(GzglDto gzglDto);
	boolean updateXmjdByRwid(GzglDto gzglDto);
	boolean insertRwly(GzglDto gzglDto);
	boolean insertRwrq(GzglDto gzglDto);
	GzglDto getXmjdxx(String xmjdid);
	/**
	 * 查询任务履历
	 */
	List<GzglDto> getRwlyDtos(GzglDto gzglDto);
	/**
	 * 查询任务阶段日期
	 */
	List<GzglDto> getRwrqDtos(GzglDto gzglDto);
	/**
	 * 批量更新任务阶段日期
	 */
	boolean updateRwrqDtos(List<GzglDto> list);
	/**
	 *
	 * gzglDto
	 * 
	 */
	GzglDto getVerificationDto(GzglDto gzglDto);

    GzglDto getGzInfoByFzrAndYwid(GzglDto gzglDto);

	GzglDto getGzDtoByGzid(String gzid);
	/**
	 * 获取统计数据
	 */
	Map<String,String> getStatistics(GzglDto gzglDto);
	/**
	 * 获取已分发人员的情况
	 * gzglDto
	 * 
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
	/**
	 * 根据工作ID任务ids
	 */
	List<GzglDto> getRwidList(GzglDto gzglDto);

	GzglDto getYhssjgandjgxx(GzglDto gzglDto);
	/**
	 * 获取流水号
	 */
	String getJlbhSerial(GzglDto gzglDto);
	/**
	 * 获取培训信息
	 */
	List<GzglDto> getTrainInfo(GzglDto gzglDto);
	/*
    *	获取工作任务统计
 	*/
	Map<String, Object> getHomePageTaskStatis(GzglDto gzglDto);
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

	String getDtoTrainXh(GzglDto gzglDto);

	GzglDto getDtoTrainFjid(GzglDto gzglDto);
	String getStringTrainFjid(GzglDto gzglDto);
}
