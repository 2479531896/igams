package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.JcsqmxDto;
import com.matridx.igams.wechat.dao.entities.JcsqmxModel;

import java.util.List;


public interface IJcsqmxService extends BaseBasicService<JcsqmxDto, JcsqmxModel> {

    /**
     * 批量新增
     * @param list
     * @return
     */
    boolean insertList(List<JcsqmxDto> list);

    /**
     * 验证标本
     * @param jcsqmxDto
     * @return
     */
    List<JcsqmxDto> verifySamples(JcsqmxDto jcsqmxDto);
}
