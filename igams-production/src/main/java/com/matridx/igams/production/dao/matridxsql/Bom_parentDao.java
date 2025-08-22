package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Bom_parentDto;
import com.matridx.igams.production.dao.entities.Bom_parentModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface Bom_parentDao extends BaseBasicDao<Bom_parentDto, Bom_parentModel>{

    /**
     * 批量添加
     */
    Integer insertList(List<Bom_parentDto> list);
}
