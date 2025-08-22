package com.matridx.las.netty.dao.post;

import com.matridx.las.netty.dao.entities.MlsjDto;
import com.matridx.las.netty.dao.entities.MlsjModel;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;

@Mapper
public interface IMlsjDao extends BaseBasicDao<MlsjDto, MlsjModel>{

}
