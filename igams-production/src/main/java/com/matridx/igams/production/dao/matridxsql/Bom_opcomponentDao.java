package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Bom_opcomponentDto;
import com.matridx.igams.production.dao.entities.Bom_opcomponentModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface Bom_opcomponentDao extends BaseBasicDao<Bom_opcomponentDto, Bom_opcomponentModel>{

    /**
     * 批量添加
     */
    Integer insertList(List<Bom_opcomponentDto> bom_opcomponentDtoList);

}
