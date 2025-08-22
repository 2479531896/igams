package com.matridx.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.web.dao.entities.YhjgqxDto;
import com.matridx.web.dao.entities.YhjgqxModel;

@Mapper
public interface IYhjgqxDao extends BaseBasicDao<YhjgqxDto, YhjgqxModel>{

	/**
	 * 查询用户机构权限
	 * @return
	 */
    List<YhjgqxDto> getYhjgqxList();

}
