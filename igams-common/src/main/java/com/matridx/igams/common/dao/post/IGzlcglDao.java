package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.GzlcglDto;
import com.matridx.igams.common.dao.entities.GzlcglModel;

@Mapper
public interface IGzlcglDao extends BaseBasicDao<GzlcglDto, GzlcglModel>{

	/**
	 * 根据工作ID查询流程信息
	 * gzid
	 * 
	 */
	List<GzlcglDto> getDtoListByGzid(String gzid);

	/**
	 * 新增流程信息
	 * gzglDto
	 * 
	 */
	int insertGzlcxxByQrrw(GzglDto gzglDto);

	/**
	 * 批量新增流程信息
	 * gzlcglDtos
	 * 
	 */
	int insertGzlcxxByQrrws(List<GzlcglDto> gzlcglDtos);
	/**
	 * 任务确认历史
	 * gzlcglDto
	 * 
	 */
	List<GzlcglDto> getPagedTaskConfirmedList(GzlcglDto gzlcglDto);

}
