package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.entities.ShtxDto;
import com.matridx.igams.common.dao.entities.ShtxModel;

public interface IShtxService extends BaseBasicService<ShtxDto, ShtxModel>{

	/**
	 * 插入一条审核提醒信息
	 */
	boolean insertshtx(ShtxDto shtxDto);

	/**
	 * 得到审批延期数据信息（全部审批和个人审批使用同一个sql）
	 */
	Map<String, Object> getPagedSpyq(ShtxDto shtxDto);

	/**
	 * 得到审批延期中选中的一条数据信息
	 */
	ShtxDto getShyqxxByShid(ShtxDto shtxDto);

	/**
	 * 定时任务显示个人延期任务列表
	 */
	Map<String, Object> getSpyqDdviewList(ShtxDto shtxDto);

	/**
	 * 显示个人审批延期列表
	 */
	Map<String, Object> getPagedPersonSpyq(ShtxDto shtxDto,String txlbs);

	/**
	 * 查找提醒类别
	 */
	List<ShtxDto> getTxlbList();

	/**
	 * postman测试定时任务使用
	 */
	boolean spyqRemind();
	/**
	 * 获取所有待审核任务
	 */
	List<ShtxDto> getAllAuditings(ShtxDto shtxDto);
}
