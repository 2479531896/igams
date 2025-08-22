package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.ZdyszDto;
import com.matridx.server.wechat.dao.entities.ZdyszModel;

@Mapper
public interface IZdyszDao extends BaseBasicDao<ZdyszDto, ZdyszModel>{

	/**
	 * 根据wxid查询该用户保存的自定义设置方案
	 * @param zdyszdto
	 * @return
	 */
	List<ZdyszDto> getZdyProject(ZdyszDto zdyszdto);
	
	/**
	 * 设置应用方案
	 * @param list
	 * @return
	 */
	boolean setUseProject(List<ZdyszDto> list);
}
