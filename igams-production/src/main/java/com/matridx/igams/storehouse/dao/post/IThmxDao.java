package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.ThmxDto;
import com.matridx.igams.storehouse.dao.entities.ThmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IThmxDao extends BaseBasicDao<ThmxDto, ThmxModel> {
    /*
    *   批量插入退货明细
    */
    boolean insertThmxList(List<ThmxDto> thmxDtos);

    boolean updateThsls(List<ThmxDto> thmxDtos);

    boolean deleteByThmxids(ThmxDto thmxDto);

    boolean updateThmxDtos(List<ThmxDto> thmxDtos);

    List<ThmxDto> getDtoListByIds(ThmxDto thmxDto);

    List<String> getHwidsByThmxids(ThmxDto thmxDto);
}
