package com.matridx.las.netty.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.ProcessconfigDto;
import com.matridx.las.netty.dao.entities.ProcessconfigModel;

import java.util.List;


public interface IProcessconfigService extends BaseBasicService<ProcessconfigDto, ProcessconfigModel>{
  public List<ProcessconfigDto> getDtoByCsid(String csid);
}
