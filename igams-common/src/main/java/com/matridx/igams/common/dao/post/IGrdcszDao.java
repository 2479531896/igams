package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.GrdcszDto;
import com.matridx.igams.common.dao.entities.GrdcszModel;

@Mapper
public interface IGrdcszDao extends BaseBasicDao<GrdcszDto, GrdcszModel>{

}
