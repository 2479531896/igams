package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.CwglDto;
import com.matridx.igams.wechat.dao.entities.CwglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ICwglDao extends BaseBasicDao<CwglDto, CwglModel>{

	/**
	 * 错误列表
	 */
    List<CwglDto> getPagedDtoList(CwglDto cwglDto);
	/**
	 * 更新标本量申请的审核状态
	 * @param cwglModel
	 * @return
	 */
    int updateZt(CwglModel cwglModel);
	

	/**
	 * 获取标本申请审核的ID列表
	 * @param cwglDto
	 * @return
	 */
    List<CwglDto> getPagedAuditIdList(CwglDto cwglDto);
	
	/**
	 * 根据标本量申请审核的ID列表获取审核列表详细信息
	 * @param cwglDtos
	 * @return
	 */
    List<CwglDto> getAuditListByIds(List<CwglDto> cwglDtos);
}
