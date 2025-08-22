package com.matridx.igams.dmp.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.dmp.dao.entities.SjkxxDto;
import com.matridx.igams.dmp.dao.entities.SjkxxModel;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;

@Mapper
public interface ISjkxxDao extends BaseBasicDao<SjkxxDto, SjkxxModel>{

	/**
	 * 查询数据库信息
	 * @return
	 */
	List<SjkxxDto> getSjkxxDtoList();

	/**
	 * 根据资源信息Dto新增数据库信息
	 * @param zyxxDto
	 * @return
	 */
	int insertSjkxxDtoByZyxxDto(ZyxxDto zyxxDto);

}
