package com.matridx.las.netty.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.dao.entities.SjlcModel;

import java.util.List;

public interface ISjlcService extends BaseBasicService<SjlcDto, SjlcModel>{
    public List<SjlcDto> getSjlcList(SjlcDto sjlcDto);

}
