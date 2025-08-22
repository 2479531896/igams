package com.matridx.igams.sample.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.sample.dao.entities.YbdbDto;
import com.matridx.igams.sample.dao.entities.YbdbModel;

import java.util.List;

public interface IYbdbService extends BaseBasicService<YbdbDto, YbdbModel> {

    /**
     * 样本调拨保存
     * @param ybdbDto
     * @return
     */
    boolean saveSampleAllot(YbdbDto ybdbDto) throws BusinessException;

    /**
     * 样本调拨删除
     * @param ybdbDto
     * @return
     */
    boolean delSampleAllot(YbdbDto ybdbDto) throws BusinessException;
    /*
        样本调拨审核列表
     */
    List<YbdbDto> getPagedAuditSampleAllot(YbdbDto ybdbDto);
}
