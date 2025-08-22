package com.matridx.igams.storehouse.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.storehouse.dao.entities.CpjgmxDto;
import com.matridx.igams.storehouse.dao.entities.CpjgmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ICpjgmxDao extends BaseBasicDao<CpjgmxDto, CpjgmxModel> {

    Integer batchInsertDtoList(List<CpjgmxDto> cpjgmxDtos);
    Integer updateList(List<CpjgmxDto> cpjgmxDtos);
}
