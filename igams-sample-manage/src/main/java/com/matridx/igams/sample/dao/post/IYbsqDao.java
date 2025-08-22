package com.matridx.igams.sample.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.YbsqDto;
import com.matridx.igams.sample.dao.entities.YbsqModel;

@Mapper
public interface IYbsqDao extends BaseBasicDao<YbsqDto, YbsqModel>{
	/**
	 * 根据标本ID获取标本信息，用于标本申请用
	 */
    YbsqDto getYbDto(YbsqModel ybsqModel);
	
	/**
	 * 更新标本申请的审核状态
	 */
    int updateZt(YbsqModel ybsqModel);
	
	/**
	 * 获取标本申请审核的ID列表
	 */
    List<YbsqDto> getPagedAuditIdList(YbsqDto ybsqDto);
	
	/**
	 * 根据标本申请审核的ID列表获取审核列表详细信息
	 */
    List<YbsqDto> getAuditListByIds(List<YbsqDto> ybsqDtos);

}
