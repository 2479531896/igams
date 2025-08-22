package com.matridx.igams.common.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import com.matridx.igams.common.dao.entities.AuditResult;
import com.matridx.igams.common.dao.entities.BaseBasicModel;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShgcModel;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;

public interface IShgcService extends BaseBasicService<ShgcDto, ShgcModel>{
	/**
	 * 根据业务id列表将当前审核过程信息放入列表中
	 * @param modelList 用于放检索结果的列表
	 * @param auditType 审核类别
	 * @param ztName	状态属性名
	 * @param ywIdName	业务id属性名
	 * @param queryZts 需要去查询审核信息的状态值（一般为：提交审核、审核退回状态）
	 */
	 void addShgcxxByYwid(List<? extends BaseBasicModel> modelList,String auditType, 
			String ztName, String ywIdName, String[] queryZts) throws BusinessException;
	
	/**
	 * 检查审核配置及不弹窗提交操作
	 */
	 Map<String, Object> checkAndCommit(ShgcDto shgcDto,User user) throws BusinessException;
	
	/**
	 * 根据业务ID获取 审核过程信息
	 */
	 ShgcDto getDtoByYwid(String ywid);

	/**
	 * 根据业务ID获取 审核过程信息
	 */
	 List<ShgcDto> getDtoByYwids(List<String> ywids);


	/**
	 * 更新至下一个流程
	 */
	 Boolean updateNext(String ywid);
	
	/**
	 * 审核处理
	 */
	 AuditResult doAudit(ShxxDto dto, User user,HttpServletRequest request) throws Exception;
	
	/**
	 * 获取当前的审核人，如果是非委托的，则获得的是当前用户，如果是委托审核，则是委托人 
	 */
	 User getAuditUser(BaseBasicModel dto, User user);
	
	/**
	 * 导入用业务的提交(针对单个业务id)
	 */
	 void importBusinessCommit(String ywid, String shlb, User operator) throws BusinessException;
	
	/**
	 * 取消审核
	 */
	 boolean doCancleAudit(ShxxDto dto, User user) throws BusinessException;
	
	/**
	 * 审核回退到指定的审核流程，按照之前的审核通过实践允许自动多次回退
	 * @param dto 本次审核信息
	 * @param user 本次用户信息
	 * @param request 请求信息
	 * @param lastlcxh 回退到的最后流程
	 */
	 AuditResult doManyBackAudit(ShxxDto dto, User user, HttpServletRequest request, String lastlcxh, JSONObject obj) throws Exception;
	
	/**
	 * 批量审核处理
	 */
	 void doBatchAudit(ShxxDto dto, User user,HttpServletRequest request) throws Exception;
	
	/**
	 * 检查批量审核的进度
	 */
	 AuditResult checkAuditThreadStatus(ShxxDto shxxDto);
	
	/**
	 * 审核中途取消
	 */
	 boolean cancelAuditProcess(ShxxDto shxxDto);
	
	/**
	 * 根据审核类别，业务ID，删除审核过程表的数据，同时更新相应业务的状态
	 */
	 String updateAuditRecall(ShgcDto dto, User user) throws BusinessException;

	/**
	 * 根据ywids删除审核过程信息
	 */
	 boolean deleteByYwids(List<String> ids);
	
	/**
	 * 钉钉审批点击拒绝操作修改本地审核过程，添加审核信息
	 */
	 AuditResult DingtalkRecallAudit(ShxxDto dto, User user, HttpServletRequest request, String lastlcxh, JSONObject obj) throws Exception;

	/**
	 * 钉钉审批点击撤回操作修改本地审核过程
	 */
	 boolean terminateAudit(ShxxDto dto, User user, HttpServletRequest request, String lastlcxh, JSONObject obj)throws BusinessException;

	/**
	 * 点击列表查看审核历史信息时，去对应列表实现类中获取单位限制的机构id信息
	 */
    Map<String, Object> dealSpgwcy(ShgcDto shgcDto);

	/**
	 * 批量新增审核过程
	 */
	 boolean insertDtoList(List<ShgcDto> shgcDtos);

	/**
	 * 批量审核完成后的处理
	 */
	 boolean doBatchAuditEnd(AuditResult result,User user,ShxxDto dto,boolean isBatchOpe) throws Exception;

	/**
	 * 定时任务删除审核过程信息
	 */
	 boolean scheduledDeleteData(ShgcDto shgcDto);

	/**
	 * 定时任务-获取申请时间超过三个月的审核过程信息
	 */
	 List<ShgcDto> getOverThreeMonthsData(ShgcDto shgcDto);
	/*
		审核代办数统计
	 */
    List<Map<String, Object>> getAuditTaskWaitingCount(ShgcDto shgcDto);
	/*
		审核代办数据查询
	 */
	List<ShgcDto> getPagedAuditTaskWaitingList(ShgcDto shgcDto);
	/**
	 * 根据业务ID获取审批岗位成员信息
	 */
	List<ShgcDto> getSpgwcyByYwids(List<String> ywids);
}
