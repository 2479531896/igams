package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.GL_mendDto;
import com.matridx.igams.production.dao.entities.GL_mendModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author:JYK
 */
@Mapper
public interface GL_mendDao extends BaseBasicDao<GL_mendDto, GL_mendModel> {
    /**
     * 查询bflag_PU
     */
    String queryBFlag_PU(String rq);
}
