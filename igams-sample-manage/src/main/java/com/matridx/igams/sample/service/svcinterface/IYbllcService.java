package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.YbllcDto;
import com.matridx.igams.sample.dao.entities.YbllcModel;

import java.util.List;

public interface IYbllcService extends BaseBasicService<YbllcDto, YbllcModel> {
    //通过人员id获取领料明细
    List<YbllcDto> getYbLlcDtoList(YbllcDto ybllcDto);
}
