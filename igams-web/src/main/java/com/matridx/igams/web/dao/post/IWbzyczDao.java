package com.matridx.igams.web.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.WbzyczDto;
import com.matridx.igams.web.dao.entities.WbzyczModel;

@Mapper
public interface IWbzyczDao extends BaseBasicDao<WbzyczDto, WbzyczModel>{

}
