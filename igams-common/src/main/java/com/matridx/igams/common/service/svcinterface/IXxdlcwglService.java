package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.dao.entities.XxdlcwglModel;

import java.util.Map;

public interface IXxdlcwglService extends BaseBasicService<XxdlcwglDto, XxdlcwglModel>{

     boolean insert(XxdlcwglDto xxdlcwglDto);

    int deleteByDate(String date);

    void clearXxdlcwglDate(Map<String,Object> codeMap);
}
