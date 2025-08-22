package com.matridx.igams.hrm.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.TkglDto;
import com.matridx.igams.hrm.dao.entities.TkglModel;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ITkglDao extends BaseBasicDao<TkglDto, TkglModel> {

}
