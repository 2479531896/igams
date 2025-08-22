package com.matridx.igams.sample.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.sample.dao.entities.YbllmxDto;
import com.matridx.igams.sample.dao.entities.YbllmxModel;
import com.matridx.igams.sample.dao.post.IYbllmxDao;
import com.matridx.igams.sample.service.svcinterface.IYbllmxService;
import org.springframework.stereotype.Service;

import java.util.List;


/*
 *@date 2022年06月13日17:08
 *
 */
@Service
public class YbllmxServiceImpl extends BaseBasicServiceImpl<YbllmxDto, YbllmxModel, IYbllmxDao> implements IYbllmxService {

    @Override
    public boolean insertYbllmxDtos(List<YbllmxDto> ybllmxDtos) {
        return dao.insertYbllmxDtos(ybllmxDtos);
    }

    @Override
    public boolean deleteByLlmxids(YbllmxDto ybllmxDto) {
        return dao.deleteByLlmxids(ybllmxDto);
    }

    @Override
    public void updateYdbjByLlmxids(YbllmxDto ybllmxDto_y) {
        dao.updateYdbjByLlmxids(ybllmxDto_y);
    }

    @Override
    public boolean deleteByLlids(YbllmxDto ybllmxDto) {
        return dao.deleteByLlids(ybllmxDto);
    }

    @Override
    public List<YbllmxDto> getDtoListWithPrint(String llid) {

        return dao.getDtoListWithPrint(llid);
    }
}
