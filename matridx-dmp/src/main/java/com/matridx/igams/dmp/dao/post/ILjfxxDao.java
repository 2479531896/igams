package com.matridx.igams.dmp.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.dmp.dao.entities.LjfxxDto;
import com.matridx.igams.dmp.dao.entities.LjfxxModel;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ILjfxxDao extends BaseBasicDao<LjfxxDto, LjfxxModel>{


	/**
	 * 根据资源信息Dto新增连接方信息
	 * @param zyxxDto
	 * @return
	 */
	int insertLjfxxDtoByZyxxDto(ZyxxDto zyxxDto);

}
