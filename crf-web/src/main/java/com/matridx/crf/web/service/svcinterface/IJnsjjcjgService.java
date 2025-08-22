package com.matridx.crf.web.service.svcinterface;

import com.matridx.crf.web.dao.entities.JnsjjcjgDto;
import com.matridx.crf.web.dao.entities.JnsjjcjgModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;

public interface IJnsjjcjgService extends BaseBasicService<JnsjjcjgDto, JnsjjcjgModel>{

    boolean insertList(List<JnsjjcjgDto> list);

}
