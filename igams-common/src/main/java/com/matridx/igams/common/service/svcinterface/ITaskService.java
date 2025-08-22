package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;

import java.util.Map;

public interface ITaskService {
	
	/**
	 * 根据任务列表的任务提交信息更新相应业务信息
	 * @param params	参数信息
	 */
	 boolean updateByTask(Map<String,Object> params) throws BusinessException;
	
	/**
	 * 任务转交更新相应业务信息
	 */
	 boolean updateByTaskTransmit(Map<String,Object> params) throws BusinessException;
	
	/**
	 * 任务确认更新相应业务信息
	 */
	 boolean updateByTaskConfirm(Map<String,Object> params) throws BusinessException;
	/**
	 * 任务转交更新相应业务信息
	 */
	 boolean updateByClaimTaskTransmit(Map<String,Object> params) throws BusinessException;
	
}
