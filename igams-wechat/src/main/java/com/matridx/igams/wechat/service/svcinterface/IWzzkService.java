package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.WzzkDto;
import com.matridx.igams.wechat.dao.entities.WzzkModel;

import java.util.List;

public interface IWzzkService extends BaseBasicService<WzzkDto, WzzkModel> {

    /**
     * 批量新增或修改
     * @param list
     * @return
     */
    boolean insertOrUpdateList(List<WzzkDto>list);

    List<WzzkDto> getPcStatistics(WzzkDto wzzkDto);

    List<WzzkDto> getNcStatistics(WzzkDto wzzkDto);

    List<WzzkDto> getNcPcTable(WzzkDto wzzkDto);
}
