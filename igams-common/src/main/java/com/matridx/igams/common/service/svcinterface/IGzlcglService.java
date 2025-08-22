package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.GzlcglDto;
import com.matridx.igams.common.dao.entities.GzlcglModel;

public interface IGzlcglService extends BaseBasicService<GzlcglDto, GzlcglModel>{

	/**
	 * 根据工作管理信息插入工作流程
	 */
	boolean insertBygzglDto(GzglDto gzglDto);

	/**
	 * 根据工作ID查询流程信息
	 */
	List<GzlcglDto> getDtoListByGzid(String gzid);

	/**
	 * 根据确认任务结果新增工作流程
	 */
	boolean insertGzlcxxByQrrw(GzglDto gzglDto);

	/**
	 * 批量任务确认新增流程
	 */
	boolean insertGzlcxxByQrrws(GzglDto gzglDto);

	/**
	 * 任务确认历史
	 */
	List<GzlcglDto> getPagedTaskConfirmedList(GzlcglDto gzlcglDto);

}
