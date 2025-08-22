package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.GrbjDto;
import com.matridx.igams.common.dao.entities.GrbjModel;

@Mapper
public interface IGrbjDao extends BaseBasicDao<GrbjDto, GrbjModel>{

}
