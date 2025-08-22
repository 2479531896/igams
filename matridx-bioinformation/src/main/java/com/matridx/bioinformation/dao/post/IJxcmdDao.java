package com.matridx.bioinformation.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.bioinformation.dao.entities.JxcmdDto;
import com.matridx.bioinformation.dao.entities.JxcmdModel;

@Mapper
public interface IJxcmdDao extends BaseBasicDao<JxcmdDto, JxcmdModel>{

}
