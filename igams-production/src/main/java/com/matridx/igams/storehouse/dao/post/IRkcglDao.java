package com.matridx.igams.storehouse.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.RkcglDto;
import com.matridx.igams.storehouse.dao.entities.RkcglModel;

import java.util.List;

@Mapper
public interface IRkcglDao extends BaseBasicDao<RkcglDto, RkcglModel>{
    /**
     * 查询入库类别
     * @param
     * @return
     */
    List<RkcglDto> getDtoRklb(String ryid);
}
