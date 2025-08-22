package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.SjycStatisticsDto;
import com.matridx.igams.common.dao.entities.SjycStatisticsModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjycStatisticsDao extends BaseBasicDao<SjycStatisticsDto, SjycStatisticsModel> {
    int insertList(List<SjycStatisticsDto> list);


    int delByYcid(SjycStatisticsDto sjycStatisticsDto);

    List<SjycStatisticsDto>getByYcid(SjycStatisticsDto sjycStatisticsDto);
}
