package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.PxglxxDto;
import com.matridx.igams.hrm.dao.entities.PxglxxModel;

import java.util.List;

public interface IPxglxxService extends BaseBasicService<PxglxxDto, PxglxxModel>{
    /**
     * @description 批量插入培训关联信息
     */
    boolean insertPxglxxDtos(List<PxglxxDto> pxglxxDtos);
    /**
     * @description 文件关联培训信息
     */
    List<PxglxxDto> getTrainByWj(PxglxxDto pxglxxDto);
}
