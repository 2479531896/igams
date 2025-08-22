package com.matridx.las.home.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.home.dao.entities.SysszDto;
import com.matridx.las.home.dao.entities.SysszModel;

@Mapper
public interface ISysszDao extends BaseBasicDao<SysszDto, SysszModel>{

}
