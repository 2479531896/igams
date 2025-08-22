package com.matridx.bioinformation.dao.post;


import com.matridx.bioinformation.dao.entities.LjglDto;
import com.matridx.bioinformation.dao.entities.LjglModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ILjglDao extends BaseBasicDao<LjglDto, LjglModel> {

}
