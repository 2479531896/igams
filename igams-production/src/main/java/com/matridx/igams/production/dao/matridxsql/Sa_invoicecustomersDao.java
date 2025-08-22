package com.matridx.igams.production.dao.matridxsql;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Sa_invoicecustomersDto;
import com.matridx.igams.production.dao.entities.Sa_invoicecustomersModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Sa_invoicecustomersDao extends BaseBasicDao<Sa_invoicecustomersDto, Sa_invoicecustomersModel> {
}
