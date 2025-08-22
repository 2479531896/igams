package com.matridx.las.netty.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.YqxxinfosxDto;
import com.matridx.las.netty.dao.entities.YqxxinfosxModel;

import java.util.List;


public interface IYqxxinfosxService extends BaseBasicService<YqxxinfosxDto, YqxxinfosxModel>{
    public void deleteAll();
    public List<YqxxinfosxDto> getYqxxinfoSxList(YqxxinfosxDto yqxxinfosxDto);
}
