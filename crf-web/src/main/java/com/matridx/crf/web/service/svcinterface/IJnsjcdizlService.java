package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.JnsjcdizlDto;
import com.matridx.crf.web.dao.entities.JnsjcdizlModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IJnsjcdizlService extends BaseBasicService<JnsjcdizlDto, JnsjcdizlModel> {

    boolean inserList(List<JnsjcdizlDto> list);

}
