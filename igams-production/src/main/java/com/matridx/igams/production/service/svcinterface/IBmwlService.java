package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.BmwlDto;
import com.matridx.igams.production.dao.entities.BmwlModel;

import java.util.List;

public interface IBmwlService extends BaseBasicService<BmwlDto, BmwlModel>{
    //获取所有部门
    List<BmwlDto> getAllBm();
}
