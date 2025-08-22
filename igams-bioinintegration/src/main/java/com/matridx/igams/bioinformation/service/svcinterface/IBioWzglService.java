package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.BioWzglDto;
import com.matridx.igams.bioinformation.dao.entities.BioWzglModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;


public interface IBioWzglService extends BaseBasicService<BioWzglDto, BioWzglModel>{

    /**
     * 根据wzid/中文名称/英文名称 查找物种信息
     */
    BioWzglDto getWzxxByMc(BioWzglDto wzglDto);
    /**
     * 获取物种List
     */
    List<BioWzglDto> getWzList(BioWzglDto wzglDto);
}
