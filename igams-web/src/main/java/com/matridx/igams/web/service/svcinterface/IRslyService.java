package com.matridx.igams.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.RslyDto;
import com.matridx.igams.web.dao.entities.RslyModel;

import java.util.List;

public interface IRslyService extends BaseBasicService<RslyDto, RslyModel> {
    /**
     * 获取审批id
     * @param rslyDto
     * @return
     */
    RslyDto getSpid(RslyDto rslyDto);

    /**
     * 点击已录用人数展示页面
     * @param rslyDto
     * @return
     */
    List<RslyDto> viewEmployDetails(RslyDto rslyDto);
}
