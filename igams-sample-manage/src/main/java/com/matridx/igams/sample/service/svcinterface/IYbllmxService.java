package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.YbllmxDto;
import com.matridx.igams.sample.dao.entities.YbllmxModel;
import java.util.List;

public interface IYbllmxService extends BaseBasicService<YbllmxDto, YbllmxModel> {


    boolean insertYbllmxDtos(List<YbllmxDto> ybllmxDtos);

    boolean deleteByLlmxids(YbllmxDto ybllmxDto);

    void updateYdbjByLlmxids(YbllmxDto ybllmxDto_y);

    boolean deleteByLlids(YbllmxDto ybllmxDto);

    List<YbllmxDto> getDtoListWithPrint(String llid);
}
