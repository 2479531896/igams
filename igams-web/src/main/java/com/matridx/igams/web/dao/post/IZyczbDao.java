package com.matridx.igams.web.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.ZyczbDto;
import com.matridx.igams.web.dao.entities.ZyczbModel;

@Mapper
public interface IZyczbDao extends BaseBasicDao<ZyczbDto, ZyczbModel>{

}
