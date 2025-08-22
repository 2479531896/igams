package com.matridx.igams.production.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SO_SOMainDto;
import com.matridx.igams.production.dao.entities.SO_SOMainModel;


public interface ISO_SOMainService extends BaseBasicService<SO_SOMainDto, SO_SOMainModel> {

    /**
     * 通过ID获取SO_SOMainInfo
     */
    SO_SOMainDto getSO_SOMainInfo(SO_SOMainDto so_soMainDto);


    /**
     * 得到总条数
     */
    Integer getToTalNumber();


}
