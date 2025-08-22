package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Bom_quepartDto;
import com.matridx.igams.production.dao.entities.Bom_quepartModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface Bom_quepartDao extends BaseBasicDao<Bom_quepartDto, Bom_quepartModel>{

    /**
     * 批量添加
     */
    Integer insertList(List<Bom_quepartDto> list);
}
