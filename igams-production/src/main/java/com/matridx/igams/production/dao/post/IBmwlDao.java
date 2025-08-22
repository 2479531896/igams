package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.BmwlDto;
import com.matridx.igams.production.dao.entities.BmwlModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IBmwlDao extends BaseBasicDao<BmwlDto, BmwlModel>{
    //获取所有部门
    List<BmwlDto> getAllBm();
}
