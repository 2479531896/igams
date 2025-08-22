package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.SjycStatisticsDto;
import com.matridx.igams.common.dao.entities.SjycStatisticsModel;
import com.matridx.igams.common.dao.post.ISjycStatisticsDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ISjycStatisticsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SjycStatisticsServiceImpl extends BaseBasicServiceImpl<SjycStatisticsDto, SjycStatisticsModel, ISjycStatisticsDao> implements ISjycStatisticsService {
    @Override
    public int insertList(List<SjycStatisticsDto> list) {
        return dao.insertList(list);
    }

    @Override
    public int delByYcid(SjycStatisticsDto sjycStatisticsDto) {
        return dao.delByYcid(sjycStatisticsDto);
    }

    @Override
    public List<SjycStatisticsDto> getByYcid(SjycStatisticsDto sjycStatisticsDto) {
        return dao.getByYcid(sjycStatisticsDto);
    }
}
