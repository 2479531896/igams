package com.matridx.igams.production.dao.matridxsql;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Customer_extradefineDto;
import com.matridx.igams.production.dao.entities.Customer_extradefineModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Customer_extradefineDao extends BaseBasicDao<Customer_extradefineDto, Customer_extradefineModel> {
}
