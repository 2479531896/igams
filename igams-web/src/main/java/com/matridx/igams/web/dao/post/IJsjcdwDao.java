package com.matridx.igams.web.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.JsjcdwDto;
import com.matridx.igams.web.dao.entities.JsjcdwModel;

@Mapper
public interface IJsjcdwDao extends BaseBasicDao<JsjcdwDto, JsjcdwModel>{
		
	/**
	 * 新增角色检测单位
	 * @param list
	 * @return
	 */
    boolean insertJsjcdw(List<JsjcdwDto> list);
}
