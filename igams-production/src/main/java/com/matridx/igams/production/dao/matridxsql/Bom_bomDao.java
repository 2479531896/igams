package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Bom_bomDto;
import com.matridx.igams.production.dao.entities.Bom_bomModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Bom_bomDao extends BaseBasicDao<Bom_bomDto, Bom_bomModel>{


}
