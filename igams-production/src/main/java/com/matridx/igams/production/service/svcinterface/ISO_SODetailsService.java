package com.matridx.igams.production.service.svcinterface;


import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SO_SODetailsDto;
import com.matridx.igams.production.dao.entities.SO_SODetailsModel;

import java.util.List;

public interface ISO_SODetailsService extends BaseBasicService<SO_SODetailsDto, SO_SODetailsModel> {

    /**
     * 选择订单明细
     */
    List<SO_SODetailsDto> getSO_SODetailsInfo(SO_SODetailsDto so_soDetailsDto);
}
