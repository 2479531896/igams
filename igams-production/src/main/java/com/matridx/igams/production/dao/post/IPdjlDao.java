package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.PdjlDto;
import com.matridx.igams.production.dao.entities.PdjlModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author:JYK
 */
@Mapper
public interface IPdjlDao extends BaseBasicDao<PdjlDto, PdjlModel> {
}
