package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.CljlxxDto;
import com.matridx.igams.production.dao.entities.CljlxxModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ICljlxxDao extends BaseBasicDao<CljlxxDto, CljlxxModel> {
}
