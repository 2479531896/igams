package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.WlxxDto;
import com.matridx.igams.storehouse.dao.entities.WlxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWlxxDao extends BaseBasicDao<WlxxDto, WlxxModel>{

    boolean insertList(List<WlxxDto> wlxxDtos);

    boolean updateList(List<WlxxDto> wlxxDtos);
}
