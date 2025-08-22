package com.matridx.igams.warehouse.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.warehouse.dao.entities.GdzcglDto;
import com.matridx.igams.warehouse.dao.entities.GdzcglModel;


public interface IGdzcglService extends BaseBasicService<GdzcglDto, GdzcglModel>{

    /**
     * 删除
     * @param gdzcglDto
     
     */
    boolean deleteDto(GdzcglDto gdzcglDto);
}
