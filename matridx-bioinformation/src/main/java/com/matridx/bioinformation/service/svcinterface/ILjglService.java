package com.matridx.bioinformation.service.svcinterface;


import com.matridx.bioinformation.dao.entities.LjglDto;
import com.matridx.bioinformation.dao.entities.LjglModel;
import com.matridx.igams.common.service.BaseBasicService;

public interface ILjglService extends BaseBasicService<LjglDto, LjglModel>{

    String[] fileList(String[] list);

}
