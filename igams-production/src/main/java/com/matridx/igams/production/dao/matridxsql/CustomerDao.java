package com.matridx.igams.production.dao.matridxsql;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.CustomerDto;
import com.matridx.igams.production.dao.entities.CustomerModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerDao extends BaseBasicDao<CustomerDto, CustomerModel> {
    //判断客户代码是否重复
    CustomerDto getDtoByKhdm(String cCusCode);
    //判断客户简称是否重复
    CustomerDto getDtoByKhjc(String cCusAbbName);
    //获取指定省份最大客户代码
    Integer countMax(CustomerDto customerDto);
}
