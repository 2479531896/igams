package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.SjycStatisticsDto;
import com.matridx.igams.common.dao.entities.SjycStatisticsModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface ISjycStatisticsService extends BaseBasicService<SjycStatisticsDto, SjycStatisticsModel> {

    int insertList(List<SjycStatisticsDto> list);


    int delByYcid(SjycStatisticsDto sjycStatisticsDto);

    List<SjycStatisticsDto>getByYcid(SjycStatisticsDto sjycStatisticsDto);
}
