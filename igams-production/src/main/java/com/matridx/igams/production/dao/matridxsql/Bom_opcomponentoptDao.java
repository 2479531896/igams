package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Bom_opcomponentoptDto;
import com.matridx.igams.production.dao.entities.Bom_opcomponentoptModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface Bom_opcomponentoptDao extends BaseBasicDao<Bom_opcomponentoptDto, Bom_opcomponentoptModel>{

    /**
     * 批量添加
     */
    Integer insertList(List<Bom_opcomponentoptDto> list);
}
