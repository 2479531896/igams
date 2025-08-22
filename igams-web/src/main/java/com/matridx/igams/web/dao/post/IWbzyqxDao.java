package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.WbzyDto;
import com.matridx.igams.web.dao.entities.WbzyqxDto;
import com.matridx.igams.web.dao.entities.WbzyqxModel;

@Mapper
public interface IWbzyqxDao extends BaseBasicDao<WbzyqxDto, WbzyqxModel>{

	/**
	 * 根据用户角色查询外部资源权限
	 * @param wbzyDto
	 * @return
	 */
    List<WbzyqxDto> getWbzyqxList(WbzyDto wbzyDto);
	
	/**
	 * 根据用户角色和资源ID查询外部资源权限
	 * @param wbzyDto
	 * @return
	 */
    List<WbzyqxDto> getSecWbzyqxList(WbzyDto wbzyDto);
	
}
