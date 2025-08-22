package com.matridx.igams.sample.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.YbsqDto;
import com.matridx.igams.sample.dao.entities.YbsqModel;

public interface IYbsqService extends BaseBasicService<YbsqDto, YbsqModel>{
	/**
	 * 根据标本ID获取标本信息，用于标本申请用
	 */
    YbsqDto getYbDto(YbsqModel ybsqModel);
	
	/**
	 * 标本申请审核列表
	 */
    List<YbsqDto> getPagedAuditList(YbsqDto ybsqDto);

	/**
	 * 标本申请保存
	 */
    boolean addSaveSampApply(YbsqDto ybsqDto);

	/**
	 * 标本修改保存
	 */
    boolean modSaveSampApply(YbsqDto ybsqDto);
}
