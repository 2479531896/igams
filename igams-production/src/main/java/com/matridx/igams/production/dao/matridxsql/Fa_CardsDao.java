package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Fa_CardsDto;
import com.matridx.igams.production.dao.entities.Fa_CardsModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Fa_CardsDao extends BaseBasicDao<Fa_CardsDto, Fa_CardsModel>{

}
