package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.BommxDto;
import com.matridx.igams.production.dao.entities.BommxModel;

import java.util.List;

public interface IScbommxService extends BaseBasicService<BommxDto, BommxModel> {

    /**
     * @description 新增BOM明细
     */
    boolean insertBommxDtos(List<BommxDto> bommxDtos);
    /**
     * @description 修改BOM明细
     */
    boolean updateBommxDtos(List<BommxDto> bommxDtos);
    /**
     * @description 删除BOM明细
     */
    boolean deleteByBomIds(BommxDto bommxDto);
    /*
        获取生产领料bom明细
     */
    List<BommxDto> getProduceReceiveBom(BommxDto bommxDto);
}
