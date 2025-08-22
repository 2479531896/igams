package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.DbxxDto;
import com.matridx.igams.production.dao.entities.DbxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface IDbxxDao  extends BaseBasicDao<DbxxDto, DbxxModel> {
    int insertList(List<DbxxDto> list);
}
