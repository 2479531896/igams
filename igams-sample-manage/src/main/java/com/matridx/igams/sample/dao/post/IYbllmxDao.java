package com.matridx.igams.sample.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.YbllmxDto;
import com.matridx.igams.sample.dao.entities.YbllmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYbllmxDao extends BaseBasicDao<YbllmxDto, YbllmxModel> {

    boolean insertYbllmxDtos(List<YbllmxDto> ybllmxDtos);

    boolean deleteByLlmxids(YbllmxDto ybllmxDto);

    void updateYdbjByLlmxids(YbllmxDto ybllmxDto_y);

    boolean deleteByLlids(YbllmxDto ybllmxDto);

    List<YbllmxDto> getDtoListWithPrint(String llid);
}
