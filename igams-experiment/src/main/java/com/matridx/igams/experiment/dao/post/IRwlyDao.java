package com.matridx.igams.experiment.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.RwlyDto;
import com.matridx.igams.experiment.dao.entities.RwlyModel;

@Mapper
public interface IRwlyDao extends BaseBasicDao<RwlyDto, RwlyModel>{

}
