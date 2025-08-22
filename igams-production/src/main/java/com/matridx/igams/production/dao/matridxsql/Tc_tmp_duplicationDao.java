package com.matridx.igams.production.dao.matridxsql;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Tc_tmp_duplicationDto;
import com.matridx.igams.production.dao.entities.Tc_tmp_duplicationModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Tc_tmp_duplicationDao extends BaseBasicDao<Tc_tmp_duplicationDto, Tc_tmp_duplicationModel> {
}
