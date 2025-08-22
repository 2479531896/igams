package com.matridx.igams.production.service.svcinterface;


import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.DdbxmxDto;
import com.matridx.igams.production.dao.entities.DdbxmxModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IDdbxmxService extends BaseBasicService<DdbxmxDto, DdbxmxModel> {
    /**
     * 批量更新
     */
    int updateList(List<DdbxmxDto> list);
    /**
     * 批量新增
     */
    int insertList(List<DdbxmxDto> list);
}
