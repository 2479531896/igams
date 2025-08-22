package com.matridx.web.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.web.dao.entities.CzdmDto;
import com.matridx.web.dao.entities.CzdmModel;

@Mapper
public interface ICzdmDao extends BaseBasicDao<CzdmDto, CzdmModel>{

}
