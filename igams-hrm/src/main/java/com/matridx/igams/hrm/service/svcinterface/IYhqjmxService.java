package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.YhqjmxDto;
import com.matridx.igams.hrm.dao.entities.YhqjmxModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IYhqjmxService extends BaseBasicService<YhqjmxDto, YhqjmxModel> {
    /**
     * 批量更新
     */
    boolean updateList(List<YhqjmxDto> list);
    /**
     * 批量插入
     */
    void insertList(List<YhqjmxDto> list);
}
