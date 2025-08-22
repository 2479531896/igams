package com.matridx.igams.production.dao.matridxsql;

import java.util.List;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SCM_CommitEntryBufferDto;
import com.matridx.igams.production.dao.entities.SCM_CommitEntryBufferModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SCM_CommitEntryBufferDao extends BaseBasicDao<SCM_CommitEntryBufferDto,SCM_CommitEntryBufferModel>{
	/**
	 * 批量新增
	 */
	int insertList(List<SCM_CommitEntryBufferDto> list);
}
