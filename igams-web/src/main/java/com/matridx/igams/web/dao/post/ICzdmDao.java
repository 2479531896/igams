package com.matridx.igams.web.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.CzdmDto;
import com.matridx.igams.web.dao.entities.CzdmModel;

@Mapper
public interface ICzdmDao extends BaseBasicDao<CzdmDto, CzdmModel>{

}
