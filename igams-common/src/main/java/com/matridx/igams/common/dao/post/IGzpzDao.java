package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.GzpzDto;
import com.matridx.igams.common.dao.entities.GzpzModel;

@Mapper
public interface IGzpzDao extends BaseBasicDao<GzpzDto, GzpzModel>{

}
