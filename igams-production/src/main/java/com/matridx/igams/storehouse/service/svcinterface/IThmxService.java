package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.ThmxDto;
import com.matridx.igams.storehouse.dao.entities.ThmxModel;

import java.util.List;

public interface IThmxService extends BaseBasicService<ThmxDto, ThmxModel> {
    /*
     *   批量插入退货明细
     */
    boolean insertThmxList(List<ThmxDto> thmxDtos);


    boolean deleteByThmxids(ThmxDto thmxDto);

    boolean updateThmxDtos(List<ThmxDto> thmxDtos);

    List<ThmxDto> getDtoListByIds(ThmxDto thmxDto);

    List<String> getHwidsByThmxids(ThmxDto thmxDto);
}
