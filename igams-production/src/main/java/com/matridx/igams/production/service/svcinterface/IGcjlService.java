package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.GcjlDto;
import com.matridx.igams.production.dao.entities.GcjlModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IGcjlService extends BaseBasicService<GcjlDto, GcjlModel> {
    /**
     * 观察记录
     */
    List<GcjlDto> getDtolistGcSampleStock(GcjlDto gcjlDto);
    /*
        观察记录保存
     */
    boolean inspectionrecordsSavePage(GcjlDto gcjlDto) throws BusinessException;
}
