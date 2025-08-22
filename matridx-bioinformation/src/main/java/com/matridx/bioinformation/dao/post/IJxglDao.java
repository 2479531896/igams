package com.matridx.bioinformation.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.bioinformation.dao.entities.JxglDto;
import com.matridx.bioinformation.dao.entities.JxglModel;

@Mapper
public interface IJxglDao extends BaseBasicDao<JxglDto, JxglModel>{

}
