package com.matridx.web.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.web.dao.entities.ZyczbDto;
import com.matridx.web.dao.entities.ZyczbModel;

@Mapper
public interface IZyczbDao extends BaseBasicDao<ZyczbDto, ZyczbModel>{

}
