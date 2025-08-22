package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.igams.common.dao.entities.SyxxModel;

@Mapper
public interface ISyxxDao extends BaseBasicDao<SyxxDto, SyxxModel>{

	/**
	 * 根据文件类型查询水印信息
	 */
	SyxxDto getDtoByWjlb(String wjlb);
}
