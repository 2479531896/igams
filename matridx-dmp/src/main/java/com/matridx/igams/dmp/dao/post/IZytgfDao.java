package com.matridx.igams.dmp.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.dmp.dao.entities.ZytgfDto;
import com.matridx.igams.dmp.dao.entities.ZytgfModel;

@Mapper
public interface IZytgfDao extends BaseBasicDao<ZytgfDto, ZytgfModel>{

	/**
	 * 获取资源提供方信息
	 * @return
	 */
	List<ZytgfDto> getZytgfDtoList();

}
