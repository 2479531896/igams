package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.GgglDto;
import com.matridx.igams.common.dao.entities.GgglModel;

@Mapper
public interface IGgglDao extends BaseBasicDao<GgglDto, GgglModel>{

}
