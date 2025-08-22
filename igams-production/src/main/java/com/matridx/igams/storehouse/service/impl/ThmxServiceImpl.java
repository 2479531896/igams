package com.matridx.igams.storehouse.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.ThmxDto;
import com.matridx.igams.storehouse.dao.entities.ThmxModel;
import com.matridx.igams.storehouse.dao.post.IThmxDao;
import com.matridx.igams.storehouse.service.svcinterface.IThmxService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThmxServiceImpl extends BaseBasicServiceImpl<ThmxDto, ThmxModel, IThmxDao> implements IThmxService {
    /*
     *   批量插入退货明细
     */
    @Override
    public boolean insertThmxList(List<ThmxDto> thmxDtos) {
       return dao.insertThmxList(thmxDtos);
    }

    @Override
    public boolean deleteByThmxids(ThmxDto thmxDto) {
        return dao.deleteByThmxids(thmxDto);
    }

    @Override
    public boolean updateThmxDtos(List<ThmxDto> thmxDtos) {
        return dao.updateThmxDtos(thmxDtos);
    }

    @Override
    public List<ThmxDto> getDtoListByIds(ThmxDto thmxDto) {
        return dao.getDtoListByIds(thmxDto);
    }

    @Override
    public List<String> getHwidsByThmxids(ThmxDto thmxDto) {
        return dao.getHwidsByThmxids(thmxDto);
    }

}
