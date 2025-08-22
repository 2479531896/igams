package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.WtzyDto;
import com.matridx.igams.common.dao.entities.WtzyModel;

@Mapper
public interface IWtzyDao extends BaseBasicDao<WtzyDto, WtzyModel>{

}
