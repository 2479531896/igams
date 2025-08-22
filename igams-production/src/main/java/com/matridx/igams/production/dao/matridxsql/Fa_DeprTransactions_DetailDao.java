package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Fa_DeprTransactions_DetailDto;
import com.matridx.igams.production.dao.entities.Fa_DeprTransactions_DetailModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Fa_DeprTransactions_DetailDao extends BaseBasicDao<Fa_DeprTransactions_DetailDto, Fa_DeprTransactions_DetailModel>{

}
