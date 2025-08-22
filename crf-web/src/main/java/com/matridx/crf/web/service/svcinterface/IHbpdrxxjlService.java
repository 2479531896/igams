package com.matridx.crf.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.crf.web.dao.entities.HbpdrxxjlDto;
import com.matridx.crf.web.dao.entities.HbpdrxxjlModel;

import java.util.List;

public interface IHbpdrxxjlService extends BaseBasicService<HbpdrxxjlDto, HbpdrxxjlModel>{
    /**
     * 根据患者ID删除记录
     * @param hbpdrxxjlDto
     * @return
     */
    public boolean deleteByHzid(HbpdrxxjlDto hbpdrxxjlDto);

    /**
     * 通过HBP患者ID获取HBP记录信息
     * @param hbpdrxxjlDto
     * @return
     */
    List<HbpdrxxjlDto> getDtoListByhzid(HbpdrxxjlDto hbpdrxxjlDto);
}
