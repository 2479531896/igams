package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.XxglDto;
import com.matridx.igams.common.dao.entities.XxglModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

@Mapper
public interface IXxglDao extends BaseBasicDao<XxglDto, XxglModel>{
	/**
	 * 获取列表
	 */
	@Override
	@Cacheable(value="xxgl",key="#p0" ,cacheManager = "ehCacheCacheManager")
	XxglDto getDtoById(String idid);

}
