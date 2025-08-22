package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.BioLczzznglDto;
import com.matridx.igams.bioinformation.dao.entities.BioLczzznglModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IBioLczzznglService extends BaseBasicService<BioLczzznglDto, BioLczzznglModel>{
    /**
     * 根据znids查询list
     */
    List<BioLczzznglDto> getDtoListByIds(BioLczzznglDto lczzznglDto);
    /**
     * 删除
     */
    boolean deleteDto(BioLczzznglDto bioLczzznglDto);
}
