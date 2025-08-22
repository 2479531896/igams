package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Fa_ObjectsDto;
import com.matridx.igams.production.dao.entities.Fa_ObjectsModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Fa_ObjectsDao extends BaseBasicDao<Fa_ObjectsDto, Fa_ObjectsModel>{

    /**
     * 根据iObjectNum查询
     */
    Fa_ObjectsDto  getlMaxIDByiObjectNum(String str);
}
