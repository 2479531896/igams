package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XtszModel;

@Mapper
public interface IXtszDao extends BaseBasicDao<XtszDto, XtszModel>{

	@Cacheable(value="xtsz",key="#p0", cacheManager = "ehCacheCacheManager")
	XtszDto getDtoById(String id);

	/**
	 * 新增或修改系统设置
	 */
	boolean insertOrUpdateXtsz(XtszDto xtszDto);

	/**
	 * 根据ID查询系统设置信息
	 */
	XtszDto selectById(String szlb);
	
	/**
	 * 根据设置类别模糊查询系统设置信息
	 */
	List<XtszDto> getObscureDto(String szlb);

	/**
	 * 查找系统设置表中的所有数据
	 */
	List<XtszDto> getXtszList();
}
