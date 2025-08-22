package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.JnsjhzxxDto;
import com.matridx.crf.web.dao.entities.JnsjhzxxModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

public interface IJnsjhzxxService extends BaseBasicService<JnsjhzxxDto, JnsjhzxxModel> {

    List<JcsjDto> getHospitailList(JnsjhzxxDto jnsjhzxxDto);
}
