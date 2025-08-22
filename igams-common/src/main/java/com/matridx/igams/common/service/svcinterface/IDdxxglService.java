package com.matridx.igams.common.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.DdxxglModel;
import com.matridx.igams.common.dao.entities.MQQueueModel;
import com.matridx.igams.common.service.BaseBasicService;

public interface IDdxxglService extends BaseBasicService<DdxxglDto, DdxxglModel>{

	/**
	 * 根据钉钉消息类型查询用户信息
	 */
	List<DdxxglDto> selectByDdxxlx(String ddxxlx);
	
	/**
	 * 查询系统用户
	 */
	List<DdxxglDto> getXtyh();

	/**
	 * 根据部门ID和审批类型查询审批流程
	 */
	List<DdxxglDto> getProcessByJgid(String jgid,String splx);

	/**
	 * @author lifan
	 * 发送MQ消息个数提醒(定时任务)
	 * 发送钉钉和微信提醒
	 */
	void sendMQMessageCount(List<MQQueueModel> queueList);

	/**
	 * 根据审批类型查询钉钉审批机构
	 */
	List<DdxxglDto> getDingtalkAuditDep(String splx);
	
	/**
	 * 根据审批ID获取钉钉审批流程信息
	 */
	DdxxglDto getProcessBySpid(String spid);
}
