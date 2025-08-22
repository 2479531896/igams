package com.matridx.igams.common.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShgcModel;

@Mapper
public interface IShgcDao extends BaseBasicDao<ShgcDto, ShgcModel>{
	/**
	 * 获取当前审核过程及最新的审核信息
	 * dto
	 *
	 */
	 List<ShgcDto> getCurrentShgcxxList(ShgcDto dto);
	
	/**
	 * 根据业务ID删除已有的审核过程信息
	 * ywid
	 * 
	 */
	 int deleteByYwid(String ywid);

	/**
	 * 更新至下一个流程
	 * ywid
	 * 
	 */
	 int updateNext(String ywid);
	
	/**
	 * 根据业务ID删除已有的审核过程信息
	 * ywid
	 * 
	 */
	 int deleteByYwids(List<String> ids);
	
	/**
	 * 更新审核过程（会更新申请时间）
	 * dto
	 * 
	 * 
	 */
	 int updateShgc(ShgcDto dto);
	
	/**
	 * 根据业务ID获取 审核过程信息
	 * ywid
	 * 
	 */
	 ShgcDto getDtoByYwid(String ywid);

	/**
	 * 根据业务ID获取 审核过程信息
	 * ywids
	 * 
	 */
	 List<ShgcDto> getDtoByYwids(List<String> ywids);
	
	/**
	 * 判断是否可以审核
	 * map
	 * 
	 */
	 boolean isAuditabled(Map<String,Object> map);

	/**
	 * 获取允许撤回的业务列表，用于判断是否可以允许撤回
	 * dto
	 * 
	 * 
	 */
	 List<ShgcDto> getRecallabledList(ShgcDto dto);

	/**
	 * 批量新增审核过程信息
	 * shgcDtos
	 * 
	 */
	 boolean insertDtoList(List<ShgcDto> shgcDtos);

	/**
	 * 获取当前审批流程的审核岗位
	 * shgcDto
	 * 
	 * 
	 */
	 ShgcDto getSpgwCurrent(ShgcDto shgcDto);

	/**
	 * 定时任务删除审核过程信息
	 * shgcDto
	 * 
	 */
	 boolean scheduledDeleteData(ShgcDto shgcDto);

	/**
	 * 定时任务-获取申请时间超过三个月的审核过程信息
	 * shgcDto
	 * 
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
