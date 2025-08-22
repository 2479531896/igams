package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.JccglDto;
import com.matridx.igams.storehouse.dao.entities.JccglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IJccglDao extends BaseBasicDao<JccglDto, JccglModel>{

	/**
	 * 借出车列表
	 * @param
	 * @return
	 */
	List<JccglDto> getJccDtoList(JccglDto jccglDto);
}
