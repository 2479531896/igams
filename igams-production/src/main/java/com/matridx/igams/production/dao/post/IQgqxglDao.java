package com.matridx.igams.production.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.QgqxglDto;
import com.matridx.igams.production.dao.entities.QgqxglModel;

@Mapper
public interface IQgqxglDao extends BaseBasicDao<QgqxglDto, QgqxglModel>{

	/**
	 * 获取请购取消管理审核ID列表
	 */
	List<QgqxglDto> getPagedAuditQgqxgl(QgqxglDto qgqxglDto);
	
	/**
	 * 审核列表
	 */
	List<QgqxglDto> getAuditListQgqxgl(List<QgqxglDto> list);
	
}
