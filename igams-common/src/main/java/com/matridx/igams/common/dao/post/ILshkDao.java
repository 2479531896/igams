package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.LshkDto;
import com.matridx.igams.common.dao.entities.LshkModel;

@Mapper
public interface ILshkDao extends BaseBasicDao<LshkDto, LshkModel>{
	/*
	 * 根据前缀查找流水号库中相应的现有流水号
	 */
	//public List<LshkDto> getDtoListByPre(LshkDto lshkDto);
}
