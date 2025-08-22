package com.matridx.igams.storehouse.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.CpjgDto;
import com.matridx.igams.storehouse.dao.entities.CpjgModel;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ICpjgDao extends BaseBasicDao<CpjgDto, CpjgModel> {
}
