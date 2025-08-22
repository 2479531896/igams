package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.JnsjcdiqyysDto;
import com.matridx.crf.web.dao.entities.JnsjcdiqyysModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IJnsjcdiqyysService extends BaseBasicService<JnsjcdiqyysDto, JnsjcdiqyysModel>{
    boolean insertList(List<JnsjcdiqyysDto> list);

}
