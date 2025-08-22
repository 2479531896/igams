package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.HbszmxDto;
import com.matridx.igams.hrm.dao.entities.HbszmxModel;

import java.util.List;

public interface IHbszmxService extends BaseBasicService<HbszmxDto, HbszmxModel> {

    /**
     * @description 批量插入红包设置信息
     */
    boolean insertHbszmxDtos(List<HbszmxDto> hbszmxDtos);
    /**
     * @description 批量修改红包设置信息
     */
    boolean updateHbszmxDtos(List<HbszmxDto> hbszmxDtos);
    /**
     * @description 批量删除红包设置信息
     */
    boolean deleteByHbmxids(HbszmxDto hbszmxDto);
    /**
     * @description 批量修改红包设置剩余数量信息
     */
    void updateHbszmxSysl(List<HbszmxDto> hbszmxDtos);
}
