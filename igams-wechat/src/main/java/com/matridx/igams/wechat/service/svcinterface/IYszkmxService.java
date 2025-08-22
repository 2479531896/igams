package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import com.matridx.igams.wechat.dao.entities.YszkmxDto;
import com.matridx.igams.wechat.dao.entities.YszkmxModel;

import java.util.List;
import java.util.Map;

public interface IYszkmxService extends BaseBasicService<YszkmxDto, YszkmxModel> {

    /**
     * 批量插入
     */
    int insertList(List<YszkmxDto> list);
    /**
     * 查询对账明细
     * @param params
     * @return
     */
    List<Map<String,Object>> getDtoListOptimize(Map<String, Object> params);

}
