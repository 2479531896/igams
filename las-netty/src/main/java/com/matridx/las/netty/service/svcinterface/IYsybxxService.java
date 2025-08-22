package com.matridx.las.netty.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.FrontMaterialModel;
import com.matridx.las.netty.dao.entities.YsybxxDto;
import com.matridx.las.netty.dao.entities.YsybxxModel;

import java.util.List;
import java.util.Map;

public interface IYsybxxService extends BaseBasicService<YsybxxDto, YsybxxModel>{
    public Map<String, Object> saveSample(YsybxxDto ysybxxDto);
    public Map<String,Object> implementSample();
    public  Map<String,Object> getTraySample();
    Map<String,Object> saveSampleList(List<YsybxxDto> list);
}
