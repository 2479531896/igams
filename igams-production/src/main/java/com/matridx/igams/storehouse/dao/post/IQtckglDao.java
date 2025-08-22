package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.QtckglDto;
import com.matridx.igams.storehouse.dao.entities.QtckglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IQtckglDao extends BaseBasicDao<QtckglDto, QtckglModel>{
	/**
	 *  批量插入数据
	 * @param
	 * @return
	 */
	Integer insertQtckgls(List<QtckglDto> qtckglDtos);
}
